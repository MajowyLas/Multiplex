import java.util.Objects;

public class Seat {

    private final String code;
    private final SeatType type;

    public Seat(String code, SeatType type) {
        if (code == null || code.isBlank()) throw new IllegalArgumentException("Seat code cannot be blank");
        this.code = code;
        this.type = Objects.requireNonNull(type);
    }

    public String getCode() {
        return code;
    }

    public SeatType getType() {
        return type;
    }

    @Override
    public String toString() {
        return code + " (" + type + ")";
    }
}
