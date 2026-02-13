import java.time.Duration;

public class Movie {

    private final String title;
    private final Duration duration;

    public Movie(String title, Duration duration) {
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Movie title cannot be blank");
        if (duration == null || duration.isNegative() || duration.isZero()) {
            throw new IllegalArgumentException("Movie duration must be positive");
        }
        this.title = title;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return title + " (" + duration.toMinutes() + " min)";
    }

}
