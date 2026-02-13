
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        CinemaSystem system = new CinemaSystem();

        Cinema cinema1 = new Cinema("Kino Super Tarasy", "ul. Akademicka 5");
        Cinema cinema2 = new Cinema("Kino Centrum", "Rynek 1");

        system.addCinema(cinema1);
        system.addCinema(cinema2);

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

        Reservation r1 = screening.reservePlaces("R1S01", "R1S02", "R1S03");
        System.out.println(r1.formatSummary());

        try {
            screening.buyTickets("R1S01");
        } catch (Exception e) {
            System.out.println("Expected error: " + e.getMessage());
            System.out.println(" ");
        }

        //przykład zakpu bez konta
        Order guestOrder = screening.buyTickets("R2S01", "R2S02");
        System.out.println("Guset ordered tickets:");
        for (Ticket t : guestOrder.getTickets()) System.out.println("  " + t);
        System.out.println(" ");

        // przykład zakupu po zalogowaniu się na konto
        Customer ania = new Customer("c1", "Ania");
        Order aniaOrder = screening.buyTickets(ania, "R3S01");
        System.out.println("Ania's purchased tickets:");
        for (Ticket t : ania.getTickets()) System.out.println("  " + t);
    }
}
