import java.util.Objects;

public class Hall {

    private final String name;
    private final SeatMap seatMap;

    public Hall(String name, SeatMap seatMap) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Hall name cannot be blank");
        this.name = name;
        this.seatMap = Objects.requireNonNull(seatMap);
    }

    public String getName() {
        return name;
    }

    public SeatMap getSeatMap() {
        return seatMap;
    }

    @Override
    public String toString() {
        return "Hall{" + name + '}';
    }

}
