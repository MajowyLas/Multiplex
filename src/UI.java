
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class UI {
    private final CinemaSystem system;
    private final Scanner scanner = new Scanner(System.in);

    public UI(CinemaSystem system) {
        this.system = system;
    }

    public void run() {
        Cinema cinema = chooseCinema(system);

        if (cinema.getProgrammeForNextWeek().isEmpty()) {
            System.out.println("This cinema has no screenings next week.");
            return;
        }

        cinema.printMoviesForNextWeek();

        Movie movie = chooseMovie(cinema);

        Screening screening = chooseScreening(cinema, movie);

        List<String> chosenSeats = chooseSeats(screening);

        Reservation r1 = screening.reservePlaces(chosenSeats.toArray(new String[0]));
        System.out.println(" ");
        System.out.println(r1.formatSummary());


    }

    private Cinema chooseCinema(CinemaSystem system) {
        List<Cinema> cinemas = system.getCinemas();
        if (cinemas.isEmpty()) throw new IllegalStateException("No cinemas configured");

        System.out.println("Choose cinema:");
        for (int i = 0; i < cinemas.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, cinemas.get(i).getName());
        }

        int idx = readIntInRange(1, cinemas.size());
        return cinemas.get(idx - 1);
    }

    private Movie chooseMovie(Cinema cinema) {
        while (true) {
            System.out.print("Type movie title (or part of it): ");
            String query = scanner.nextLine().trim();

            if (query.isBlank()) {
                System.out.println("Empty input.Try again.");
                continue;
            }

            Movie found = cinema.findMovie(query);
            if (found != null) {
                System.out.println("Selected movie: " + found);
                return found;
            }
            System.out.println("Movie not found. Try again.");
        }
    }

    private Screening chooseScreening(Cinema cinema, Movie movie) {
        List<Screening> candidates = cinema.getProgramme(LocalDate.now(), 7).stream()
                .filter(s -> s.getMovie() == movie)
                .sorted(Comparator.comparing(Screening::getStart))
                .toList();

        if (candidates.isEmpty()) {
            throw new IllegalStateException("No screenings for this movie in the next 7 days.");
        }

        System.out.println("Choose screening:");
        for (int i = 0; i < candidates.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, candidates.get(i).formatLine());
        }

        int idx = readIntInRange(1, candidates.size());
        Screening chosen = candidates.get(idx - 1);

        System.out.println(chosen.printScreenings());
        return chosen;
    }

    private List<String> chooseSeats(Screening screening) {
        List<String> available = screening.getAvailableSeatCodes();
        if (available.isEmpty()) throw new IllegalStateException("No seats available.");

        System.out.println("Available seats (sample): " +
                available.stream().limit(30).collect(Collectors.toList()) +
                (available.size() > 30 ? " ..." : ""));

        System.out.print("Enter seat codes separated by comma (e.g., R1S01,R1S02): ");
        String line = scanner.nextLine().trim();

        List<String> chosen = Arrays.stream(line.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();

        if (chosen.isEmpty()) {
            System.out.println("No seats provided. Try again.");
            return chooseSeats(screening);
        }

        Set<String> availableSet = new HashSet<>(available);
        for (String seat : chosen) {
            if (!availableSet.contains(seat)) {
                System.out.println("Seat not available or invalid: " + seat);
                System.out.println("Try again.");
                return chooseSeats(screening);
            }
        }

        return chosen;
    }

    private int readIntInRange(int min, int max) {
        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            try {
                int v = Integer.parseInt(line);
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException ignored) {}
            System.out.printf("Please enter a number between %d and %d.%n", min, max);
        }
    }

    }


