import java.util.Objects;
import java.util.UUID;

public class Ticket {
    private final String id;
    private final Screening screening;
    private final Seat seat;

    public Ticket(Screening screening, Seat seat) {
        this.id = UUID.randomUUID().toString();
        this.screening = Objects.requireNonNull(screening);
        this.seat = Objects.requireNonNull(seat);
    }

    public String getId() {
        return id;
    }

    public Screening getScreening() {
        return screening;
    }

    public Seat getSeat() {
        return seat;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id='" + id + '\'' +
                ", movie=" + screening.getMovie().getTitle() +
                ", start=" + screening.getStart() +
                ", hall=" + screening.getHall().getName() +
                ", seat=" + seat +
                ", type=" + screening.getType() +
                '}';
    }

}
