package interviews.wayfair;

import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/* Interfaces */
interface IPerson {
}

interface IEventInfo {
    String eventName();
    LocalDate eventDate();
    int capacity();
    boolean canceled();
    Set<IPerson> registration();
    Set<IPerson> attendees();

    boolean register(IPerson person);
    boolean attend(IPerson person);
}

interface IEventManager {
    boolean addEvent(IEventInfo event);
    boolean register(String eventName, IPerson person);
    boolean attend(String eventName, IPerson person);

    Map<Integer, YearStats> getYearStats();
}

/* Records */
record Person(String firstName, String lastName) implements IPerson {}
record YearStats(int events, int registrations, int attendees) {}

/* Implementations */
class EventInfo implements IEventInfo {
    private final String eventName;
    private final LocalDate eventDate;
    private final int capacity;
    private final boolean canceled;
    private final Set<IPerson> registration;
    private final Set<IPerson> attendees;

    public EventInfo(String eventName, LocalDate eventDate, int capacity, boolean canceled) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.capacity = capacity;
        this.canceled = canceled;
        this.registration = new HashSet<>();
        this.attendees = new HashSet<>();
    }

    @Override public String eventName() { return eventName; }
    @Override public LocalDate eventDate() { return eventDate; }
    @Override public int capacity() { return capacity; }
    @Override public boolean canceled() { return canceled; }
    @Override public Set<IPerson> registration() { return registration; }
    @Override public Set<IPerson> attendees() { return attendees; }

    @Override
    public boolean register(IPerson person) {
        if (canceled) return false;
        if (registration.size() >= capacity) return false;
        return registration.add(person);
    }

    @Override
    public boolean attend(IPerson person) {
        if (canceled) return false;
        if (!registration.contains(person)) return false;
        return attendees.add(person);
    }
}

class EventManager implements IEventManager {
    private final List<IEventInfo> events;
    private final Map<String, IEventInfo> eventByName;

    public EventManager() {
        this.events = new ArrayList<>();
        this.eventByName = new HashMap<>();
    }

    @Override
    public boolean addEvent(IEventInfo event) {
        if (eventByName.containsKey(event.eventName())) return false;
        events.add(event);
        eventByName.put(event.eventName(), event);
        return true;
    }

    @Override
    public boolean register(String eventName, IPerson person) {
        IEventInfo e = eventByName.get(eventName);
        return (e != null) && e.register(person);
    }

    @Override
    public boolean attend(String eventName, IPerson person) {
        IEventInfo e = eventByName.get(eventName);
        return (e != null) && e.attend(person);
    }

    @Override
    public Map<Integer, YearStats> getYearStats() {
        Map<Integer, YearStats> stats = new TreeMap<>();
        for (IEventInfo e : events) {
            int year = e.eventDate().getYear();
            YearStats old = stats.getOrDefault(year, new YearStats(0, 0, 0));
            YearStats updated = new YearStats(
                    old.events() + 1,
                    old.registrations() + e.registration().size(),
                    old.attendees() + e.attendees().size()
            );
            stats.put(year, updated);
        }
        return stats;
    }

    public IEventInfo getEventByIndex(int idx) {
        if (idx < 0 || idx >= events.size()) return null;
        return events.get(idx);
    }
}

/* Main with input/output */
public class EventManagementSystem {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // People
        int n = sc.nextInt();
        sc.nextLine();
        List<IPerson> people = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String[] parts = sc.nextLine().trim().split("\\s+");
            people.add(new Person(parts[0], parts[1]));
        }

        // Events
        int m = sc.nextInt();
        sc.nextLine();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        EventManager manager = new EventManager();
        List<IEventInfo> createdEventsInOrder = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            String[] parts = sc.nextLine().trim().split("\\s+");
            String eventName = parts[0];
            LocalDate eventDate = LocalDate.parse(parts[1], fmt);
            int capacity = Integer.parseInt(parts[2]);
            boolean canceled = parts[3].equals("1");
            EventInfo e = new EventInfo(eventName, eventDate, capacity, canceled);
            manager.addEvent(e);
            createdEventsInOrder.add(e);
        }

        // Operations
        int k = sc.nextInt();
        for (int i = 0; i < k; i++) {
            int action = sc.nextInt();
            int personIdx = sc.nextInt();
            int eventIdx = sc.nextInt();
            if (personIdx < 0 || personIdx >= people.size()) continue;
            if (eventIdx < 0 || eventIdx >= createdEventsInOrder.size()) continue;
            IPerson p = people.get(personIdx);
            IEventInfo ev = createdEventsInOrder.get(eventIdx);
            if (action == 1) ev.register(p);
            else if (action == 2) ev.attend(p);
        }

        // Results
        printResult(manager);
    }

    private static void printResult(EventManager manager) {
        Map<Integer, YearStats> stats = manager.getYearStats();

        System.out.println("Event Count:");
        stats.forEach((y, s) -> System.out.println(y + " - " + s.events()));
        System.out.println("Registrations:");
        stats.forEach((y, s) -> System.out.println(y + " - " + s.registrations()));
        System.out.println("Attendees:");
        stats.forEach((y, s) -> System.out.println(y + " - " + s.attendees()));
    }
}
