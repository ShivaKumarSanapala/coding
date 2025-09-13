package interviews.wayfair;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * FoodOrderingSystem.java
 *
 * Single-file demo implementation for the LLD problem:
 * - In-memory stores for restaurants, products, and orders
 * - Pluggable SelectionStrategy
 * - Concurrency-safe order assignment (synchronized per-restaurant)
 *
 * Notes:
 * - No persistence (in-memory only)
 * - Menu updates: add or update price (no delete)
 * - Orders are assigned only if a single restaurant can fulfill all items
 */
public class FoodOrderingSystem {

    /* ------------------------------- Models ------------------------------- */

    public static class MenuItem {
        public final String name;
        private BigDecimal price;

        public MenuItem(String name, BigDecimal price) {
            this.name = Objects.requireNonNull(name);
            this.price = Objects.requireNonNull(price);
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal newPrice) {
            this.price = newPrice;
        }

        @Override
        public String toString() {
            return name + ":" + price.toPlainString();
        }
    }

    public static class Restaurant {
        public final String name;
        private final Map<String, MenuItem> menu = new HashMap<>(); // itemName -> MenuItem
        private final double rating; // 0..5
        private final int maxConcurrentOrders;
        private int currentOrders = 0; // guarded by synchronized(this)

        public Restaurant(String name, double rating, int maxConcurrentOrders) {
            this.name = Objects.requireNonNull(name);
            this.rating = rating;
            this.maxConcurrentOrders = maxConcurrentOrders;
        }

        public synchronized boolean tryReserveSlot() {
            if (currentOrders < maxConcurrentOrders) {
                currentOrders++;
                return true;
            } else {
                return false;
            }
        }

        public synchronized void completeOrderAndFreeSlot() {
            if (currentOrders > 0) currentOrders--;
        }

        public synchronized int getCurrentOrders() {
            return currentOrders;
        }

        public double getRating() {
            return rating;
        }

        public Map<String, MenuItem> getMenuSnapshot() {
            // return a copy to avoid external modifications
            return new HashMap<>(menu);
        }

        public void addOrUpdateMenuItem(String itemName, BigDecimal price) {
            menu.compute(itemName, (k, v) -> {
                if (v == null) return new MenuItem(itemName, price);
                v.setPrice(price);
                return v;
            });
        }

        public boolean servesAllItems(Map<String, Integer> items) {
            for (String i : items.keySet()) {
                MenuItem mi = menu.get(i);
                if (mi == null) return false;
            }
            return true;
        }

        public BigDecimal computeOrderPrice(Map<String, Integer> items) {
            BigDecimal total = BigDecimal.ZERO;
            for (Map.Entry<String, Integer> e : items.entrySet()) {
                MenuItem mi = menu.get(e.getKey());
                if (mi == null) throw new IllegalArgumentException("Item not present");
                BigDecimal qty = BigDecimal.valueOf(e.getValue());
                total = total.add(mi.getPrice().multiply(qty));
            }
            return total;
        }

        @Override
        public String toString() {
            return String.format("Restaurant{name='%s', rating=%.2f, max=%d, current=%d}", name, rating, maxConcurrentOrders, currentOrders);
        }
    }

    public enum OrderStatus {
        CREATED, ACCEPTED, COMPLETED, REJECTED
    }

    public static class Order {
        public final long id;
        public final String user;
        public final Map<String, Integer> items; // itemName -> quantity
        public final SelectionType selectionType;
        public OrderStatus status;
        public String assignedRestaurant; // name of restaurant or null
        public BigDecimal assignedPrice; // computed price at assignment time

        public Order(long id, String user, Map<String, Integer> items, SelectionType selectionType) {
            this.id = id;
            this.user = Objects.requireNonNull(user);
            this.items = new HashMap<>(Objects.requireNonNull(items));
            this.selectionType = selectionType;
            this.status = OrderStatus.CREATED;
        }

        @Override
        public String toString() {
            return String.format("Order{id=%d,user=%s,items=%s,sel=%s,status=%s,restaurant=%s,price=%s}",
                    id, user, items, selectionType, status, assignedRestaurant, assignedPrice);
        }
    }

    /* ------------------------------- Exceptions ------------------------------- */

    public static class OrderException extends Exception {
        public OrderException(String msg) { super(msg); }
    }

    public static class CannotFulfillOrderException extends OrderException {
        public CannotFulfillOrderException(String msg) { super(msg); }
    }

    public static class NoAvailableRestaurantException extends OrderException {
        public NoAvailableRestaurantException(String msg) { super(msg); }
    }

    /* ------------------------------- Selection Strategy ------------------------------- */

    public enum SelectionType { LOWEST_COST, HIGHEST_RATING }

    public interface SelectionStrategy {
        /**
         * Given candidate restaurants that can fulfill the order, return them in preference order.
         * The implementation must not reserve slots — reservation happens later atomically.
         */
        List<Restaurant> rankCandidates(List<Restaurant> candidates, Order order);
    }

    public static class LowestCostStrategy implements SelectionStrategy {
        @Override
        public List<Restaurant> rankCandidates(List<Restaurant> candidates, Order order) {
            List<Restaurant> copy = new ArrayList<>(candidates);
            // sort by total price ascending, tie-breaker higher rating
            copy.sort(Comparator.comparing((Restaurant r) -> r.computeOrderPrice(order.items))
                    .thenComparing((Restaurant r) -> -r.getRating()));
            return copy;
        }
    }

    public static class HighestRatingStrategy implements SelectionStrategy {
        @Override
        public List<Restaurant> rankCandidates(List<Restaurant> candidates, Order order) {
            List<Restaurant> copy = new ArrayList<>(candidates);
            // sort by rating desc, tie-breaker lower cost
            copy.sort(Comparator.<Restaurant, Double>comparing(r -> -r.getRating())
                    .thenComparing(r -> r.computeOrderPrice(order.items)));
            return copy;
        }
    }

    /* ------------------------------- Service ------------------------------- */

    public static class OrderingService {
        private final Map<String, Restaurant> restaurants = new ConcurrentHashMap<>(); // name -> restaurant
        private final Map<Long, Order> orders = new ConcurrentHashMap<>();
        private final AtomicLong orderIdGenerator = new AtomicLong(1);
        private final Map<SelectionType, SelectionStrategy> strategies = new HashMap<>();

        public OrderingService() {
            // register default strategies
            strategies.put(SelectionType.LOWEST_COST, new LowestCostStrategy());
            strategies.put(SelectionType.HIGHEST_RATING, new HighestRatingStrategy());
        }

        public void registerStrategy(SelectionType type, SelectionStrategy strategy) {
            strategies.put(type, strategy);
        }

        public void onboardRestaurant(String name, double rating, int maxConcurrentOrders, Map<String, BigDecimal> menu) {
            if (restaurants.containsKey(name)) throw new IllegalArgumentException("Restaurant already exists: " + name);
            Restaurant r = new Restaurant(name, rating, maxConcurrentOrders);
            if (menu != null) {
                menu.forEach(r::addOrUpdateMenuItem);
            }
            restaurants.put(name, r);
        }

        public void updateMenu(String restaurantName, String itemName, BigDecimal price) {
            Restaurant r = restaurants.get(restaurantName);
            if (r == null) throw new IllegalArgumentException("Unknown restaurant: " + restaurantName);
            r.addOrUpdateMenuItem(itemName, price);
        }

        /**
         * Place an order — will attempt to auto-assign the order to a single restaurant that can fulfill all items.
         * This method is concurrency-safe: it will try candidates in preference order and attempt to reserve capacity atomically.
         */
        public Order placeOrder(String user, Map<String, Integer> items, SelectionType selectionType) throws OrderException {
            long id = orderIdGenerator.getAndIncrement();
            Order order = new Order(id, user, items, selectionType);

            // find candidate restaurants that serve all items
            List<Restaurant> candidates = new ArrayList<>();
            for (Restaurant r : restaurants.values()) {
                if (r.servesAllItems(items)) candidates.add(r);
            }
            if (candidates.isEmpty()) {
                order.status = OrderStatus.REJECTED;
                orders.put(order.id, order);
                throw new CannotFulfillOrderException("No single restaurant can fulfill all items for order " + id);
            }

            // rank candidates using strategy
            SelectionStrategy strat = strategies.get(selectionType);
            if (strat == null) throw new IllegalStateException("No strategy registered for " + selectionType);
            List<Restaurant> ranked = strat.rankCandidates(candidates, order);

            // Try to atomically reserve a slot on restaurants in order
            for (Restaurant r : ranked) {
                boolean reserved;
                synchronized (r) {
                    reserved = r.tryReserveSlot(); // this checks capacity and increments if possible
                }
                if (reserved) {
                    // assign order to r
                    order.assignedRestaurant = r.name;
                    order.status = OrderStatus.ACCEPTED;
                    order.assignedPrice = r.computeOrderPrice(items);
                    orders.put(order.id, order);
                    System.out.printf("Order %d assigned to %s (price=%s, strategy=%s)%n", order.id, r.name, order.assignedPrice, selectionType);
                    return order;
                } else {
                    // couldn't reserve because capacity full; try next candidate
                }
            }

            // none available due to capacity
            order.status = OrderStatus.REJECTED;
            orders.put(order.id, order);
            throw new NoAvailableRestaurantException("No restaurants with available capacity for order " + id);
        }

        /**
         * Restaurant marks order as COMPLETED. Only the restaurant that accepted the order can complete it.
         */
        public void completeOrder(String restaurantName, long orderId) throws OrderException {
            Order order = orders.get(orderId);
            if (order == null) throw new OrderException("Unknown order: " + orderId);
            if (!Objects.equals(order.assignedRestaurant, restaurantName)) {
                throw new OrderException("Order " + orderId + " not assigned to restaurant " + restaurantName);
            }
            if (order.status != OrderStatus.ACCEPTED) {
                throw new OrderException("Order " + orderId + " not in ACCEPTED state");
            }
            Restaurant r = restaurants.get(restaurantName);
            if (r == null) throw new OrderException("Unknown restaurant: " + restaurantName);

            // complete and free capacity
            synchronized (r) {
                r.completeOrderAndFreeSlot();
            }
            order.status = OrderStatus.COMPLETED;
            System.out.printf("Order %d completed by %s. Restaurant available slots now: %d%n", orderId, restaurantName, r.getCurrentOrders());
        }

        public Optional<Order> getOrder(long id) {
            return Optional.ofNullable(orders.get(id));
        }

        public Optional<Restaurant> getRestaurant(String name) {
            return Optional.ofNullable(restaurants.get(name));
        }
    }

    /* ------------------------------- Demo / Tests ------------------------------- */

    public static void main(String[] args) {
        OrderingService svc = new OrderingService();

        // 1) Onboard restaurants (sample data)
        svc.onboardRestaurant("R1", 4.5, 5, Map.of(
                "Veg Biryani", new BigDecimal("100"),
                "Paneer Butter Masala", new BigDecimal("150")
        ));
        svc.onboardRestaurant("R2", 4.0, 5, Map.of(
                "Paneer Butter Masala", new BigDecimal("175"),
                "Idli", new BigDecimal("10"),
                "Dosa", new BigDecimal("50"),
                "Veg Biryani", new BigDecimal("80")
        ));
        svc.onboardRestaurant("R3", 4.9, 1, Map.of(
                "Gobi Manchurian", new BigDecimal("150"),
                "Idli", new BigDecimal("15"),
                "Paneer Butter Masala", new BigDecimal("175"),
                "Dosa", new BigDecimal("30")
        ));

        // 2) Update menus
        svc.updateMenu("R1", "Chicken65", new BigDecimal("250")); // ADD
        svc.updateMenu("R2", "Paneer Butter Masala", new BigDecimal("150")); // UPDATE

        // 3) Place orders (sample sequence)
        try {
            // Order1: 3 Idli, 1 Dosa, Lowest cost -> expected R3 (because R3 price: 3*15 + 1*30 = 75; R2: 3*10 + 1*50 = 80)
            Map<String, Integer> o1items = new HashMap<>();
            o1items.put("Idli", 3);
            o1items.put("Dosa", 1);
            Order o1 = svc.placeOrder("Ashwin", o1items, SelectionType.LOWEST_COST);
            System.out.println("Order1 -> " + o1);

            // Order2: same items, Lowest cost -> expected R2 (R3 capacity is 1 so it is now full)
            Order o2 = svc.placeOrder("Harish", o1items, SelectionType.LOWEST_COST);
            System.out.println("Order2 -> " + o2);

            // Order3: 3 Veg Biryani, Highest rating -> expected R1 (or R3 if R3 served and cost matched - but R1 has rating 4.5, R3 4.9 but R3 doesn't serve Veg Biryani)
            Map<String, Integer> o3items = Map.of("Veg Biryani", 3);
            Order o3 = svc.placeOrder("Shruthi", o3items, SelectionType.HIGHEST_RATING);
            System.out.println("Order3 -> " + o3);

            // R3 completes Order1
            svc.completeOrder("R3", o1.id);

            // Order4: same as Order1, Lowest cost -> now R3 should again be available
            Order o4 = svc.placeOrder("Harish", o1items, SelectionType.LOWEST_COST);
            System.out.println("Order4 -> " + o4);

            // Order5: 1 Paneer Tikka (not available anywhere) + 1 Idli -> should be rejected (CannotFulfillOrderException)
            Map<String, Integer> o5items = Map.of("Paneer Tikka", 1, "Idli", 1);
            try {
                svc.placeOrder("xyz", o5items, SelectionType.LOWEST_COST);
            } catch (CannotFulfillOrderException e) {
                System.out.println("Order5 -> cannot be fulfilled: " + e.getMessage());
            }

        } catch (OrderException e) {
            System.err.println("Order error: " + e.getMessage());
        }
    }
}

