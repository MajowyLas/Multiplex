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

    public void printProgramme(LocalDate from, int days) {
        System.out.println("Programme for: " + " --> " + name + " (" + address + ")" + "<----");

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
        LocalDate to = from.plusDays(days);
        List<Screening> out = new ArrayList<>();
        for (Screening s : screenings) {
            LocalDate d = s.getStart().toLocalDate();
            if ((d.isEqual(from) || d.isAfter(from)) && d.isBefore(to)) out.add(s);
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

    public void printProgramme() {
        printProgramme(LocalDate.now(), 7);
    }

    public Screening[] getScreenings() {
        return screenings.toArray(new Screening[0]);
    }
    public String formatScreenings() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n========== PROGRAMME ==========\n");

        for (Screening s : screenings) {
            sb.append(s.printScreenings());
            sb.append("--------------------------------\n");
        }

        return sb.toString();
    }

}
