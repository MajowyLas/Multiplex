import java.util.*;

public class SeatMap {

    private final List<Seat> seats;
    private final Map<String, Seat> seatsByCode;

    public SeatMap(List<Seat> seats) {
        if (seats == null || seats.isEmpty()) throw new IllegalArgumentException("SeatMap must have seats");
        this.seats = List.copyOf(seats);
        Map<String, Seat> tmp = new HashMap<>();
        for (Seat s : seats) {
            if (tmp.containsKey(s.getCode())) {
                throw new IllegalArgumentException("Duplicate seat code: " + s.getCode());
            }
            tmp.put(s.getCode(), s);
        }
        this.seatsByCode = Map.copyOf(tmp);
    }

    public Seat getSeat(String code) {
        Seat seat = seatsByCode.get(code);
        if (seat == null) throw new IllegalArgumentException("Seat not found: " + code);
        return seat;
    }

    public List<Seat> getAllSeats() {
        return seats;
    }

    // Helper do Main/demo
    public static SeatMap generate(int rows, int seatsPerRow, int vipRowsFromFront) {
        List<Seat> seats = new ArrayList<>();
        for (int r = 1; r <= rows; r++) {
            for (int s = 1; s <= seatsPerRow; s++) {
                String code = "R" + r + "S" + String.format("%02d", s);
                SeatType type = (r <= vipRowsFromFront) ? SeatType.VIP : SeatType.NORMAL;
                seats.add(new Seat(code, type));
            }
        }
        return new SeatMap(seats);

    }
}