
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        CinemaSystem system = new CinemaSystem();

        Cinema cinema1 = new Cinema("Kino Super Tarasy", "ul. Akademicka 5");
        Cinema cinema2 = new Cinema("Kino Centrum", "Rynek 1");

        system.addCinema(cinema1);
        system.addCinema(cinema2);

        Scanner scanner = new Scanner(System.in);
        Cinema cinema = chooseCinema(system, scanner);


        Hall hallA = new Hall("Hall A", SeatMap.generate(5, 10, 1));
        cinema1.addHall(hallA);

        Hall hallB = new Hall ("Hall B", SeatMap.generate(6,12,1));

        Movie movie1 = new Movie("James Bond", Duration.ofMinutes(120));
        cinema1.addMovie(movie1);

        Movie movie2 = new Movie ("Avengers",Duration.ofMinutes(156));
        cinema1.addMovie(movie2);

        Screening screening1 = new Screening(
                movie1,
                hallA,
                LocalDateTime.now().plusDays(1).withHour(19).withMinute(0).withSecond(0).withNano(0),
                ScreeningType.THREE_D
        );

        Screening screening2 = new Screening (
                movie2,
                hallB,
                LocalDateTime.now().plusDays(1).withHour(22).withMinute(0).withSecond(0).withNano(0),
                ScreeningType.STANDARD
        );
        cinema1.addScreening(screening1);
        cinema1.addScreening(screening2);

        cinema1.printProgramme(LocalDate.now(), 7);

        Screening screening = cinema1.getProgrammeForNextWeek().get(0);
       System.out.println(cinema1.formatScreenings());

        Movie movie = chooseMovie(cinema, scanner);

        Screening screeningg = chooseScreening(cinema, movie, scanner);
        List<String> chosenSeats = chooseSeats(screening, scanner);

        Reservation r1 = screening.reservePlaces(chosenSeats.toArray(new String[0]));
        System.out.println(r1.formatSummary());

//        try {
//            screening.buyTickets("R1S01");
//        } catch (Exception e) {
//            System.out.println("Expected error: " + e.getMessage());
//            System.out.println(" ");
//        }
//
//        //przykład zakpu bez konta
//        Order guestOrder = screening.buyTickets("R2S01", "R2S02");
//        System.out.println("Guset ordered tickets:");
//        for (Ticket t : guestOrder.getTickets()) System.out.println("  " + t);
//        System.out.println(" ");
//
//        // przykład zakupu po zalogowaniu się na konto
//        Customer ania = new Customer("c1", "Ania");
//        Order aniaOrder = screening.buyTickets(ania, "R3S01");
//        System.out.println("Ania's purchased tickets:");
//        for (Ticket t : ania.getTickets()) System.out.println("  " + t);
    }
    private static Cinema chooseCinema(CinemaSystem system, Scanner sc) {
        List<Cinema> cinemas = system.getCinemas();
        if (cinemas.isEmpty()) throw new IllegalStateException("No cinemas configured");

        System.out.println("Choose cinema:");
        for (int i = 0; i < cinemas.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, cinemas.get(i).getName());
        }

        int idx = readIntInRange(sc, 1, cinemas.size());
        return cinemas.get(idx - 1);
    }

    private static int readIntInRange(Scanner sc, int min, int max) {
        while (true) {
            System.out.print("> ");
            String line = sc.nextLine().trim();
            try {
                int v = Integer.parseInt(line);
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException ignored) {}
            System.out.printf("Please enter a number between %d and %d.%n", min, max);
        }
    }
    private static Movie chooseMovie(Cinema cinema, Scanner sc) {
        while (true) {
            System.out.print("Type movie title (or part of it): ");
            String query = sc.nextLine().trim();

            Movie found = cinema.findMovie(query);
            if (found != null) {
                System.out.println("Selected movie: " + found);
                return found;
            }
            System.out.println("Movie not found. Try again.");
        }
    }

    private static Screening chooseScreening(Cinema cinema, Movie movie, Scanner sc) {
        List<Screening> candidates = cinema.getProgramme(LocalDate.now(), 7).stream()
                .filter(s -> s.getMovie().getTitle().equalsIgnoreCase(movie.getTitle()))
                .sorted(Comparator.comparing(Screening::getStart))
                .toList();

        if (candidates.isEmpty()) {
            throw new IllegalStateException("No screenings for this movie in the next 7 days.");
        }

        System.out.println("Choose screening:");
        for (int i = 0; i < candidates.size(); i++) {
            Screening s = candidates.get(i);
            // jeśli masz formatLine() to użyj:
            System.out.printf("%d) %s%n", i + 1, s.formatLine());
        }

        int idx = readIntInRange(sc, 1, candidates.size());
        Screening chosen = candidates.get(idx - 1);
        System.out.println(chosen.printScreenings());
        return chosen;
    }

    private static List<String> chooseSeats(Screening screening, Scanner sc) {
        // pokaż dostępne (możesz to potem zrobić ładniej jako siatkę)
        List<String> available = screening.getAvailableSeatCodes();
        if (available.isEmpty()) throw new IllegalStateException("No seats available.");

        System.out.println("Available seats (sample): " +
                available.stream().limit(30).collect(Collectors.toList()) +
                (available.size() > 30 ? " ..." : ""));

        System.out.print("Enter seat codes separated by comma (e.g., R1S01,R1S02): ");
        String line = sc.nextLine().trim();

        List<String> chosen = Arrays.stream(line.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();

        if (chosen.isEmpty()) {
            System.out.println("No seats provided. Try again.");
            return chooseSeats(screening, sc);
        }

        // walidacja: czy wszystkie są dostępne
        Set<String> availableSet = new HashSet<>(available);
        for (String seat : chosen) {
            if (!availableSet.contains(seat)) {
                System.out.println("Seat not available or invalid: " + seat);
                System.out.println("Try again.");
                return chooseSeats(screening, sc);
            }
        }

        return chosen;
    }
;}
