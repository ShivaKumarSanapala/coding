package interviews.wayfair;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * VehicleRentalService.java
 *
 * Single-file runnable implementation for:
 *  - addBranch(branchName)
 *  - allocatePrice(branchName, vehicleType, price)
 *  - addVehicle(vehicleId, vehicleType, branchName)
 *  - bookVehicle(vehicleType, startTime, endTime)
 *  - viewVehicleInventory(startTime, endTime)
 *
 * Strategy pattern used for allocation; default is lowest price strategy.
 */
public class VehicleRentalService {

    /* ------------------------------- Models ------------------------------- */

    public enum VehicleType { SEDAN, HATCHBACK, SUV }

    /**
     * @param end exclusive
     */
    public record TimeSlot(LocalDateTime start, LocalDateTime end) {
        public TimeSlot {
            if (start == null || end == null || !start.isBefore(end)) {
                throw new IllegalArgumentException("Invalid timeslot: start must be before end");
            }
        }

            public boolean overlaps(TimeSlot other) {
                // overlap if start < other.end && other.start < end
                return this.start.isBefore(other.end) && other.start.isBefore(this.end);
            }

            @Override
            public String toString() {
                DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                return "[" + start.format(f) + " -> " + end.format(f) + ")";
            }
        }

    public static class Booking {
        public final UUID bookingId;
        public final String vehicleId;
        public final TimeSlot slot;

        public Booking(String vehicleId, TimeSlot slot) {
            this.bookingId = UUID.randomUUID();
            this.vehicleId = vehicleId;
            this.slot = slot;
        }
    }

    public static class Vehicle {
        public final String vehicleId;
        public final VehicleType type;
        public final String branchName;
        // Simple in-memory bookings list; synchronized when mutated/read
        private final List<Booking> bookings = new ArrayList<>();

        public Vehicle(String vehicleId, VehicleType type, String branchName) {
            this.vehicleId = vehicleId;
            this.type = type;
            this.branchName = branchName;
        }

        public synchronized boolean isAvailable(TimeSlot slot) {
            for (Booking b : bookings) {
                if (b.slot.overlaps(slot)) return false;
            }
            return true;
        }

        public synchronized Booking addBooking(TimeSlot slot) {
            if (!isAvailable(slot)) return null;
            Booking b = new Booking(vehicleId, slot);
            bookings.add(b);
            return b;
        }

        public synchronized List<Booking> getBookingsSnapshot() {
            return new ArrayList<>(bookings);
        }

        @Override
        public String toString() {
            return vehicleId + "(" + type + ")";
        }
    }

    public static class Branch {
        public final String name;
        // price per vehicle type (may be missing)
        private final Map<VehicleType, Integer> pricePerType = new EnumMap<>(VehicleType.class);
        // vehicles grouped by type
        private final Map<VehicleType, List<Vehicle>> vehiclesByType = new EnumMap<>(VehicleType.class);

        public Branch(String name) {
            this.name = name;
            for (VehicleType vt : VehicleType.values()) vehiclesByType.put(vt, new ArrayList<>());
        }

        public void setPrice(VehicleType type, int price) {
            if (price < 0) throw new IllegalArgumentException("Price must be >= 0");
            pricePerType.put(type, price);
        }

        public Optional<Integer> getPrice(VehicleType type) {
            return Optional.ofNullable(pricePerType.get(type));
        }

        public void addVehicle(Vehicle v) {
            vehiclesByType.get(v.type).add(v);
        }

        public List<Vehicle> getVehiclesOfType(VehicleType type) {
            return vehiclesByType.get(type);
        }

        @Override
        public String toString() {
            return "Branch{" + name + "}";
        }
    }

    /* ------------------------------- Strategy ------------------------------- */

    public interface AllocationStrategy {
        /**
         * Given a list of branches which have a price for the requested type,
         * return the branches in preference order.
         */
        List<Branch> sortBranches(List<Branch> branches, VehicleType type);
    }

    public static class LowestPriceStrategy implements AllocationStrategy {
        @Override
        public List<Branch> sortBranches(List<Branch> branches, VehicleType type) {
            List<Branch> copy = new ArrayList<>(branches);
            copy.sort(Comparator.comparingInt(b -> b.getPrice(type).orElse(Integer.MAX_VALUE)));
            return copy;
        }
    }

    /* ------------------------------- Service ------------------------------- */

    public static class RentalService {
        // in-memory stores
        private final Map<String, Branch> branches = new ConcurrentHashMap<>();
        private final Map<String, Vehicle> vehicles = new ConcurrentHashMap<>();

        // allocation strategy (pluggable)
        private AllocationStrategy allocationStrategy = new LowestPriceStrategy();

        public void setAllocationStrategy(AllocationStrategy strategy) {
            Objects.requireNonNull(strategy);
            this.allocationStrategy = strategy;
        }

        // addBranch
        public void addBranch(String branchName) {
            if (branchName == null || branchName.trim().isEmpty()) throw new IllegalArgumentException("branchName required");
            if (branches.containsKey(branchName)) throw new IllegalArgumentException("branch already exists: " + branchName);
            branches.put(branchName, new Branch(branchName));
            System.out.println("Added branch: " + branchName);
        }

        // allocatePrice
        public void allocatePrice(String branchName, VehicleType type, int price) {
            Branch b = branches.get(branchName);
            if (b == null) throw new IllegalArgumentException("Unknown branch: " + branchName);
            b.setPrice(type, price);
            System.out.printf("Allocated price %d for %s at branch %s%n", price, type, branchName);
        }

        // addVehicle
        public void addVehicle(String vehicleId, VehicleType type, String branchName) {
            if (vehicles.containsKey(vehicleId)) throw new IllegalArgumentException("vehicle already exists: " + vehicleId);
            Branch b = branches.get(branchName);
            if (b == null) throw new IllegalArgumentException("Unknown branch: " + branchName);
            Vehicle v = new Vehicle(vehicleId, type, branchName);
            vehicles.put(vehicleId, v);
            b.addVehicle(v);
            System.out.printf("Added vehicle %s (type=%s) to branch %s%n", vehicleId, type, branchName);
        }

        // bookVehicle
        public synchronized Optional<Booking> bookVehicle(VehicleType type, TimeSlot slot) {
            // gather candidate branches which have price for this type
            List<Branch> priced = new ArrayList<>();
            for (Branch b : branches.values()) {
                if (b.getPrice(type).isPresent()) priced.add(b);
            }
            if (priced.isEmpty()) {
                System.out.println("No branch has price defined for " + type);
                return Optional.empty();
            }

            // sort branches by strategy
            List<Branch> ordered = allocationStrategy.sortBranches(priced, type);

            // try each branch in order, trying to reserve any available vehicle in that branch
            for (Branch b : ordered) {
                List<Vehicle> vehiclesOfType = b.getVehiclesOfType(type);
                // try to find an available vehicle inside this branch
                for (Vehicle v : vehiclesOfType) {
                    // synchronize on vehicle when checking and booking
                    synchronized (v) {
                        if (v.isAvailable(slot)) {
                            Booking booking = v.addBooking(slot);
                            if (booking != null) {
                                int price = b.getPrice(type).orElseThrow(IllegalStateException::new);
                                System.out.printf("Booked vehicle %s from branch %s for slot %s at price/hr=%d%n", v.vehicleId, b.name, slot, price);
                                return Optional.of(booking);
                            }
                        }
                    }
                }
                // none available in this branch, try next branch
            }

            System.out.println("NO " + type + " AVAILABLE for slot " + slot);
            return Optional.empty();
        }

        // viewVehicleInventory
        public void viewInventory(TimeSlot slot) {
            System.out.println("Inventory snapshot for slot " + slot);
            for (Branch b : branches.values()) {
                System.out.println("Branch: " + b.name);
                for (VehicleType vt : VehicleType.values()) {
                    List<Vehicle> list = b.getVehiclesOfType(vt);
                    if (list.isEmpty()) continue;
                    for (Vehicle v : list) {
                        boolean available;
                        synchronized (v) { available = v.isAvailable(slot); }
                        System.out.printf("  %s %s %s%n", vt, v.vehicleId, (available ? "Available" : "Booked"));
                    }
                }
            }
        }
    }

    /* ------------------------------- Demo (main) ------------------------------- */

    public static void main(String[] args) {
        RentalService svc = new RentalService();

        // sample branches & prices (as in problem statement)
        svc.addBranch("Vasanth Vihar");
        svc.addBranch("Cyber City");

        svc.allocatePrice("Vasanth Vihar", VehicleType.SEDAN, 100);
        svc.allocatePrice("Vasanth Vihar", VehicleType.HATCHBACK, 80);
        svc.allocatePrice("Cyber City", VehicleType.SEDAN, 200);
        svc.allocatePrice("Cyber City", VehicleType.HATCHBACK, 50);

        // add vehicles
        svc.addVehicle("DL 01 MR 9310", VehicleType.SEDAN, "Vasanth Vihar");
        svc.addVehicle("DL 01 MR 9311", VehicleType.SEDAN, "Cyber City");
        svc.addVehicle("DL 01 MR 9312", VehicleType.HATCHBACK, "Cyber City");

        // helper to create times
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime t1s = LocalDateTime.parse("2020-02-29 10:00", fmt);
        LocalDateTime t1e = LocalDateTime.parse("2020-02-29 13:00", fmt); // 10->13
        LocalDateTime t2s = LocalDateTime.parse("2020-02-29 14:00", fmt);
        LocalDateTime t2e = LocalDateTime.parse("2020-02-29 15:00", fmt);

        // bookings from sample
        Optional<Booking> b1 = svc.bookVehicle(VehicleType.SEDAN, new TimeSlot(t1s, t1e));
        System.out.println("Order result: " + (b1.isPresent() ? b1.get().vehicleId + " booked" : "no booking"));

        Optional<Booking> b2 = svc.bookVehicle(VehicleType.SEDAN, new TimeSlot(t2s, t2e));
        System.out.println("Order result: " + (b2.isPresent() ? b2.get().vehicleId + " booked" : "no booking"));

        Optional<Booking> b3 = svc.bookVehicle(VehicleType.SEDAN, new TimeSlot(t2s, t2e));
        System.out.println("Order result: " + (b3.isPresent() ? b3.get().vehicleId + " booked" : "no booking"));

        Optional<Booking> b4 = svc.bookVehicle(VehicleType.SEDAN, new TimeSlot(t2s, t2e));
        System.out.println("Order result: " + (b4.isPresent() ? b4.get().vehicleId + " booked" : "no booking"));

        // view inventory for 14:00 - 15:00
        System.out.println();
        svc.viewInventory(new TimeSlot(t2s, t2e));

        // view inventory for 16:00 - 17:00 (all free)
        System.out.println();
        LocalDateTime t3s = LocalDateTime.parse("2020-02-29 16:00", fmt);
        LocalDateTime t3e = LocalDateTime.parse("2020-02-29 17:00", fmt);
        svc.viewInventory(new TimeSlot(t3s, t3e));
    }
}
