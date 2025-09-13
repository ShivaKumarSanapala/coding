package interviews.wayfair;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * CouponDisplaySystem
 *
 * - Precomputes best coupon per category (ignoring future-dated coupons).
 * - findBestCoupon(category) -> Coupon or null
 * - getDiscountedPrice(productName) -> String (formatted price with 2 decimals)
 *
 * Note:
 * - Product lookup uses exact ProductName match (case-sensitive). If you want fuzzy matching, change the lookup logic.
 */
public class CouponDisplaySystem {

    // Data classes
    public static class Coupon {
        public final String categoryName;
        public final String couponName;
        public final LocalDate dateModified;  // may be null if not supplied
        public final String discount;        // e.g. "10%", "$15", "15.5%", "15"

        public Coupon(String categoryName, String couponName, String dateModifiedStr, String discount) {
            this.categoryName = categoryName;
            this.couponName = couponName;
            this.discount = discount;
            if (dateModifiedStr == null) {
                this.dateModified = null;
            } else {
                // parse date in ISO format YYYY-MM-DD
                try {
                    this.dateModified = LocalDate.parse(dateModifiedStr);
                } catch (DateTimeParseException ex) {
                    throw new IllegalArgumentException("Invalid date format for coupon: " + dateModifiedStr);
                }
            }
        }

        @Override
        public String toString() {
            return couponName + " (" + discount + (dateModified != null ? ", " + dateModified : "") + ")";
        }
    }

    /**
     * @param parentName null means top-level
     */
    public record Category(String name, String parentName) {}

    public static class Product {
        public final String productName;
        public final BigDecimal price;
        public final String categoryName;

        public Product(String productName, String priceStr, String categoryName) {
            this.productName = productName;
            this.price = new BigDecimal(priceStr).setScale(2, RoundingMode.HALF_EVEN);
            this.categoryName = categoryName;
        }
    }

    // Core storage
    private final Map<String, Category> categoryByName = new HashMap<>();
    private final Map<String, List<Coupon>> couponsByCategory = new HashMap<>();
    private final Map<String, Product> productByName = new HashMap<>();

    // Precomputed best coupon per category (null if none found in hierarchy)
    private final Map<String, Coupon> bestCouponForCategory = new HashMap<>();

    // "today" used to filter future-dated coupons. Injected so tests can control it.
    private final LocalDate today;

    // ------------------ Constructor & init ------------------

    /**
     * Build the system and precompute best coupons.
     *
     * @param categories list of Category objects
     * @param coupons    list of Coupon objects
     * @param products   list of Product objects
     * @param today      LocalDate to treat as "now" (pass LocalDate.now() in production)
     */
    public CouponDisplaySystem(List<Category> categories, List<Coupon> coupons, List<Product> products, LocalDate today) {
        this.today = today == null ? LocalDate.now() : today;

        // load categories
        for (Category c : categories) {
            categoryByName.put(c.name, c);
        }
        // load coupons
        for (Coupon cp : coupons) {
            couponsByCategory.computeIfAbsent(cp.categoryName, k -> new ArrayList<>()).add(cp);
        }
        // load products
        for (Product p : products) {
            productByName.put(p.productName, p);
        }

        // Precompute best coupon per category using memoized DFS/iteration
        precomputeBestCoupons();
    }

    // ------------------ Precomputation ------------------

    private void precomputeBestCoupons() {
        // For every category, compute and memoize
        for (String categoryName : categoryByName.keySet()) {
            computeBestCouponFor(categoryName, new HashSet<>());
        }
    }

    /**
     * Recursively compute best coupon for a category and cache result.
     * Uses memoization and cycle detection.
     */
    private Coupon computeBestCouponFor(String categoryName, Set<String> visiting) {
        if (bestCouponForCategory.containsKey(categoryName)) {
            return bestCouponForCategory.get(categoryName);
        }

        if (visiting.contains(categoryName)) {
            // cycle detected - break it by returning null (or could throw)
            bestCouponForCategory.put(categoryName, null);
            return null;
        }
        visiting.add(categoryName);

        // 1) check if this category has any valid coupon and pick best among them
        Coupon bestLocal = bestValidCouponForCategory(categoryName);

        if (bestLocal != null) {
            // exact requirement: if a Category has a coupon it should NOT move up the hierarchy
            bestCouponForCategory.put(categoryName, bestLocal);
            visiting.remove(categoryName);
            return bestLocal;
        }

        // 2) else inherit from parent (if any)
        Category cat = categoryByName.get(categoryName);
        if (cat == null || cat.parentName == null) {
            bestCouponForCategory.put(categoryName, null);
            visiting.remove(categoryName);
            return null;
        }

        Coupon parentCoupon = computeBestCouponFor(cat.parentName, visiting);
        bestCouponForCategory.put(categoryName, parentCoupon);
        visiting.remove(categoryName);
        return parentCoupon;
    }

    /**
     * From couponsByCategory, choose the most recent coupon whose DateModified <= today.
     * If date is null treat as valid (or you can choose to treat null as very old).
     * If multiple coupons have the same date, picks the first encountered (deterministic on insertion order).
     */
    private Coupon bestValidCouponForCategory(String categoryName) {
        return Optional.ofNullable(couponsByCategory.get(categoryName))
                .orElse(Collections.emptyList())
                .stream()
                .filter(c -> c.dateModified == null || !c.dateModified.isAfter(today))
                .max(Comparator.comparing(c -> c.dateModified,
                        Comparator.nullsFirst(Comparator.naturalOrder())))
                .orElse(null);
    }

    // ------------------ Public API ------------------

    /**
     * Returns coupon name for a category. null if no coupon in entire hierarchy.
     */
    public String findBestCouponNameForCategory(String categoryName) {
        Coupon c = bestCouponForCategory.get(categoryName);
        return c == null ? null : c.couponName;
    }

    /**
     * Returns the Coupon object for the category (precomputed). null if none.
     */
    public Coupon findBestCouponForCategory(String categoryName) {
        return bestCouponForCategory.get(categoryName);
    }

    /**
     * Takes a product name and returns the discounted price as formatted string with two decimals.
     * If product not found => returns null.
     * If no coupon applicable => returns original price formatted.
     */
    public String getDiscountedPriceForProduct(String productName) {
        Product p = productByName.get(productName);
        if (p == null) return null;

        Coupon coupon = findBestCouponForCategory(p.categoryName);
        if (coupon == null) {
            return formatMoney(p.price);
        }

        BigDecimal discounted = applyDiscount(p.price, coupon.discount);
        return formatMoney(discounted);
    }

    /**
     * Return applied coupon name for a product (or null).
     */
    public String getAppliedCouponForProduct(String productName) {
        Product p = productByName.get(productName);
        if (p == null) return null;
        Coupon coupon = findBestCouponForCategory(p.categoryName);
        return coupon == null ? null : coupon.couponName;
    }

    // ------------------ Discount parsing & application ------------------

    /**
     * Apply discount string to price. Discount formats supported:
     * - "10%" (percentage)
     * - "25" or "$25" (fixed amount)
     * - "$15.75"
     *
     * Returns price >= 0
     */
    private BigDecimal applyDiscount(BigDecimal price, String discountStr) {
        if (discountStr == null || discountStr.trim().isEmpty()) return price;
        String s = discountStr.trim();

        try {
            if (s.endsWith("%")) {
                String num = s.substring(0, s.length() - 1).trim();
                BigDecimal pct = new BigDecimal(num);
                BigDecimal multiplier = BigDecimal.ONE.subtract(pct.divide(new BigDecimal("100"), 8, RoundingMode.HALF_EVEN));
                BigDecimal result = price.multiply(multiplier);
                if (result.compareTo(BigDecimal.ZERO) < 0) result = BigDecimal.ZERO;
                return result.setScale(2, RoundingMode.HALF_EVEN);
            } else {
                // fixed amount; allow leading $ or nothing
                if (s.startsWith("$")) s = s.substring(1).trim();
                BigDecimal fixed = new BigDecimal(s);
                BigDecimal result = price.subtract(fixed);
                if (result.compareTo(BigDecimal.ZERO) < 0) result = BigDecimal.ZERO;
                return result.setScale(2, RoundingMode.HALF_EVEN);
            }
        } catch (NumberFormatException ex) {
            // invalid discount string -> return original price (safe fallback)
            return price.setScale(2, RoundingMode.HALF_EVEN);
        }
    }

    private String formatMoney(BigDecimal m) {
        return m.setScale(2, RoundingMode.HALF_EVEN).toPlainString();
    }

    // ------------------ Example main / tests ------------------

    public static void main(String[] args) {
        // Example data from the prompt (Follow-up 3 dataset for coupons includes discount)
        List<Coupon> coupons = Arrays.asList(
                new Coupon("Comforter Sets", "Comforters Sale", "2020-01-01", "10%"),
                new Coupon("Comforter Sets", "Cozy Comforter Coupon", "2020-01-01", "$15"),
                new Coupon("Bedding", "Best Bedding Bargains", "2019-01-01", "35%"),
                new Coupon("Bedding", "Savings on Bedding", "2019-01-01", "25%"),
                new Coupon("Bed & Bath", "Low price for Bed & Bath", "2018-01-01", "50%"),
                new Coupon("Bed & Bath", "Bed & Bath extravaganza", "2019-01-01", "75%")
                // note: if you want to add future-dated coupons, they will be ignored when today's date is before the date
        );

        List<Category> categories = Arrays.asList(
                new Category("Comforter Sets", "Bedding"),
                new Category("Bedding", "Bed & Bath"),
                new Category("Bed & Bath", null),
                new Category("Soap Dispensers", "Bathroom Accessories"),
                new Category("Bathroom Accessories", "Bed & Bath"),
                new Category("Toy Organizers", "Baby And Kids"),
                new Category("Baby And Kids", null)
        );

        List<Product> products = Arrays.asList(
                new Product("Cozy Comforter Sets", "100.00", "Comforter Sets"),
                new Product("All-in-one Bedding Set", "50.00", "Bedding"),
                new Product("Infinite Soap Dispenser", "500.00", "Bathroom Accessories"),
                new Product("Rainbow Toy Box", "257.00", "Baby And Kids")
        );

        // Use today's date as 2025-09-09 for deterministic result demonstration (change as needed)
        LocalDate today = LocalDate.now(); // or LocalDate.of(2025, 9, 9);

        CouponDisplaySystem system = new CouponDisplaySystem(categories, coupons, products, today);

        // Examples (Category lookups)
        System.out.println("\"Comforter Sets\" => " + system.findBestCouponNameForCategory("Comforter Sets"));
        System.out.println("\"Bedding\" => " + system.findBestCouponNameForCategory("Bedding"));
        System.out.println("\"Bathroom Accessories\" => " + system.findBestCouponNameForCategory("Bathroom Accessories"));
        System.out.println("\"Soap Dispensers\" => " + system.findBestCouponNameForCategory("Soap Dispensers"));
        System.out.println("\"Toy Organizers\" => " + system.findBestCouponNameForCategory("Toy Organizers"));

        // Product discounts
        System.out.println("\"Cozy Comforter Sets\" discounted => " + system.getDiscountedPriceForProduct("Cozy Comforter Sets")
                + " applied coupon: " + system.getAppliedCouponForProduct("Cozy Comforter Sets"));
        System.out.println("\"All-in-one Bedding Set\" discounted => " + system.getDiscountedPriceForProduct("All-in-one Bedding Set")
                + " applied coupon: " + system.getAppliedCouponForProduct("All-in-one Bedding Set"));
        System.out.println("\"Infinite Soap Dispenser\" discounted => " + system.getDiscountedPriceForProduct("Infinite Soap Dispenser")
                + " applied coupon: " + system.getAppliedCouponForProduct("Infinite Soap Dispenser"));
        System.out.println("\"Rainbow Toy Box\" discounted => " + system.getDiscountedPriceForProduct("Rainbow Toy Box")
                + " applied coupon: " + system.getAppliedCouponForProduct("Rainbow Toy Box"));
    }
}
