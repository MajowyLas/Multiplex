import java.time.Duration;
import java.time.LocalDateTime;

public class DemoData {

    public static CinemaSystem buildSystem() {

        CinemaSystem system = new CinemaSystem();

        Cinema cinema1 = new Cinema("Kino Super Tarasy", "ul. Akademicka 5");
        Cinema cinema2 = new Cinema("Kino Centrum", "Rynek 1");

        Hall hallA = new Hall("Hall A", SeatMap.generate(8, 10, 1));
        Hall hallB = new Hall("Hall B", SeatMap.generate(6, 12, 1));
        Hall hallC = new Hall("Hall C", SeatMap.generate(10, 12, 2));

        Movie movie1 = new Movie("James Bond", Duration.ofMinutes(120));
        Movie movie2 = new Movie("Avengers", Duration.ofMinutes(156));
        Movie movie3 = new Movie("Aviator", Duration.ofMinutes(134));
        Movie movie4 = new Movie("Avatar", Duration.ofMinutes(180));


        cinema1.addHall(hallA);
        cinema2.addHall(hallB);
        cinema2.addHall(hallC);


        cinema1.addMovie(movie1);
        cinema1.addMovie(movie2);
        cinema2.addMovie(movie3);
        cinema2.addMovie(movie4);

        Screening screening1 = new Screening(
                movie1,
                hallA,
                LocalDateTime.now().plusDays(1).withHour(19).withMinute(0).withSecond(0).withNano(0),
                ScreeningType.STANDARD
        );

        Screening screening11 = new Screening(
                movie1,
                hallA,
                LocalDateTime.now().plusDays(1).withHour(17).withMinute(0).withSecond(0).withNano(0),
                ScreeningType.STANDARD
        );

        Screening screening111 = new Screening(
                movie1,
                hallA,
                LocalDateTime.now().plusDays(1).withHour(20).withMinute(30).withSecond(0).withNano(0),
                ScreeningType.STANDARD
        );


        Screening screening2 = new Screening(
                movie2,
                hallA,
                LocalDateTime.now().plusDays(1).withHour(22).withMinute(0).withSecond(0).withNano(0),
                ScreeningType.STANDARD
        );

        Screening screening3 = new Screening(
                movie3,
                hallB,
                LocalDateTime.now().plusDays(2).withHour(17).withMinute(30).withSecond(0).withNano(0),
                ScreeningType.STANDARD
        );

        Screening screening4 = new Screening(
                movie4,
                hallB,
                LocalDateTime.now().plusDays(1).withHour(20).withMinute(20).withSecond(0).withNano(0),
                ScreeningType.STANDARD
        );


        Screening screening5 = new Screening(
                movie4,
                hallB,
                LocalDateTime.now().plusDays(1).withHour(20).withMinute(20).withSecond(0).withNano(0),
                ScreeningType.THREE_D
        );

        cinema1.addScreening(screening1);
        cinema1.addScreening(screening2);
        cinema1.addScreening(screening11);
        cinema1.addScreening(screening111);
        cinema2.addScreening(screening3);
        cinema2.addScreening(screening4);
        cinema2.addScreening(screening5);

        system.addCinema(cinema1);
        system.addCinema(cinema2);

        return system;


    }
}