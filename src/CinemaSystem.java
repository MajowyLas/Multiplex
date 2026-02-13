import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CinemaSystem {

    private final List<Cinema> cinemas = new ArrayList<>();

    public void addCinema(Cinema cinema) {
        cinemas.add(Objects.requireNonNull(cinema));
    }

    public List<Cinema> getCinemas() {
        return List.copyOf(cinemas);
    }
}
