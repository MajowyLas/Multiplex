import java.time.LocalDate;
import java.util.*;

public class Cinema {

    private final String name;
    private final String address;
    private final List<Hall> halls = new ArrayList<>();
    private final List<Screening> screenings = new ArrayList<>();
    private final List<Movie> movies = new ArrayList<>();

    public Cinema(String name, String address) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Cinema name cannot be blank");
        if (address == null || address.isBlank()) throw new IllegalArgumentException("Address cannot be blank");
        this.name = name;
        this.address = address;
    }

    public void addHall(Hall hall) {
        halls.add(Objects.requireNonNull(hall));
    }

    public void addMovie(Movie movie) {
        movies.add(Objects.requireNonNull(movie));
    }

    public void addScreening(Screening screening) {
        screenings.add(Objects.requireNonNull(screening));
    }

    public List<Movie> getMoviesForNextWeek() {
        List<Screening> programme = getProgrammeForNextWeek();

        return programme.stream()
                .map(Screening::getMovie)
                .distinct()
                .toList();
    }


    public void printMoviesForNextWeek() {
        System.out.println("Movies for: --> " + name + " (" + address + ") <----");

        List<Movie> movies = getMoviesForNextWeek();

        if (movies.isEmpty()) {
            System.out.println("(no movies)");
            return;
        }

        for (Movie m : movies) {
            System.out.println(" - " + m);
        }
    }



    public Movie findMovie(String query) {
        if (query == null || query.isBlank()) throw new IllegalArgumentException("Query cannot be blank");
        String q = query.toLowerCase(Locale.ROOT);
        for (Movie m : movies) {
            if (m.getTitle().toLowerCase(Locale.ROOT).contains(q)) return m;
        }
        return null;
    }

    public List<Screening> getProgrammeForNextWeek() {
        return getProgramme(LocalDate.now(), 7);
    }

    public List<Screening> getProgramme(LocalDate from, int days) {
        LocalDate toExclusive = from.plusDays(days);
        List<Screening> out = new ArrayList<>();

        for (Screening s : screenings) {
            LocalDate d = s.getStart().toLocalDate();
            if (!d.isBefore(from) && d.isBefore(toExclusive)) {
                out.add(s);
            }
        }

        out.sort(Comparator.comparing(Screening::getStart));
        return out;
    }

    public List<Hall> getHalls() {
        return List.copyOf(halls);
    }


    public String getName() {
        return name;
    }


}
