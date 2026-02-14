import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class Screening {

    private final Movie movie;
    private final Hall hall;
    private final LocalDateTime start;
    private final ScreeningType type;


    private final Map<String, SeatStatus> seatStatuses = new HashMap<>();


    private final Map<String, Reservation> reservationsById = new HashMap<>();
    private final Map<String, String> seatReservationIdBySeatCode = new HashMap<>();

    private final Duration reservationTtl = Duration.ofMinutes(15);

    public Screening(Movie movie, Hall hall, LocalDateTime start, ScreeningType type) {
        this.movie = Objects.requireNonNull(movie);
        this.hall = Objects.requireNonNull(hall);
        this.start = Objects.requireNonNull(start);
        this.type = Objects.requireNonNull(type);

        for (Seat seat : hall.getSeatMap().getAllSeats()) {
            seatStatuses.put(seat.getCode(), SeatStatus.AVAILABLE);
        }
    }

    public Movie getMovie() {
        return movie;
    }

    public Hall getHall() {
        return hall;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public ScreeningType getType() {
        return type;
    }

    public Reservation reservePlaces(String... seatCodes) {
        return reservePlaces(null, seatCodes);
    }

    public Reservation reservePlaces(Customer customer, String... seatCodes) {
        Objects.requireNonNull(seatCodes);
        cleanupExpired(LocalDateTime.now());

        List<Seat> seats = resolveSeatsOrThrow(seatCodes);


        for (Seat s : seats) {
            SeatStatus st = seatStatuses.get(s.getCode());
            if (st == null) throw new IllegalArgumentException("Unknown seat code: " + s.getCode());
            if (st != SeatStatus.AVAILABLE) {
                throw new IllegalStateException("Seat not available: " + s.getCode() + " (status=" + st + ")");
            }
        }

        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = new Reservation(customer, this, seats, now, now.plus(reservationTtl));
        reservationsById.put(reservation.getId(), reservation);


        for (Seat s : seats) {
            seatStatuses.put(s.getCode(), SeatStatus.RESERVED);
            seatReservationIdBySeatCode.put(s.getCode(), reservation.getId());
        }

        return reservation;
    }

    public Order buyTickets(String... seatCodes) {
        return buyTickets(null, seatCodes);
    }

    public Order buyTickets(Customer customer, String... seatCodes) {
        Objects.requireNonNull(seatCodes);
        cleanupExpired(LocalDateTime.now());

        List<Seat> seats = resolveSeatsOrThrow(seatCodes);


        for (Seat s : seats) {
            SeatStatus st = seatStatuses.get(s.getCode());
            if (st != SeatStatus.AVAILABLE) {
                throw new IllegalStateException("Cannot buy seat " + s.getCode() + " because status=" + st);
            }
        }


        for (Seat s : seats) {
            seatStatuses.put(s.getCode(), SeatStatus.SOLD);
            seatReservationIdBySeatCode.remove(s.getCode());
        }

        List<Ticket> tickets = new ArrayList<>();
        for (Seat s : seats) {
            tickets.add(new Ticket(this, s));
        }

        Order order = new Order(customer, tickets, LocalDateTime.now());
        if (customer != null) customer.addOrder(order);
        return order;
    }


    private List<Seat> resolveSeatsOrThrow(String... seatCodes) {
        List<Seat> seats = new ArrayList<>();
        for (String code : seatCodes) {
            if (code == null || code.isBlank()) throw new IllegalArgumentException("Seat code cannot be blank");
            seats.add(hall.getSeatMap().getSeat(code));
        }
        return seats;
    }

    private void cleanupExpired(LocalDateTime now) {

        List<String> toExpire = new ArrayList<>();
        for (Reservation r : reservationsById.values()) {
            if (r.getStatus() == ReservationStatus.ACTIVE && r.isExpired(now)) {
                toExpire.add(r.getId());
            }
        }

        for (String rid : toExpire) {
            Reservation r = reservationsById.get(rid);
            r.expireIfNeeded(now);

            for (Seat s : r.getSeats()) {
                String code = s.getCode();

                if (seatStatuses.get(code) == SeatStatus.RESERVED) {
                    String currentRid = seatReservationIdBySeatCode.get(code);
                    if (rid.equals(currentRid)) {
                        seatStatuses.put(code, SeatStatus.AVAILABLE);
                        seatReservationIdBySeatCode.remove(code);
                    }
                }
            }
        }
    }

    public List<String> getAvailableSeatCodes() {
        cleanupExpired(LocalDateTime.now());
        List<String> out = new ArrayList<>();
        for (Map.Entry<String, SeatStatus> e : seatStatuses.entrySet()) {
            if (e.getValue() == SeatStatus.AVAILABLE) out.add(e.getKey());
        }
        Collections.sort(out);
        return out;
    }

    @Override
    public String toString() {
        return "Screening: " +
                "movie title: " + movie.getTitle() +
                ", hall -> " + hall.getName() +
                ", date: " + start +
                ", type: " + type
                ;
    }
    public String printScreenings() {
        return """
            Movie : %s
            Type  : %s
            Date  : %s
            Hall  : %s
            Free seats: %d
            """.formatted(
                movie.getTitle(),
                type,
                start,
                hall.getName(),
                getAvailableSeatCodes().size()
        );
    }
    public String formatLine() {
        return String.format(
                "%s | %s | %s | %s",
                start,
                hall.getName(),
                type,
                movie.getTitle()
        );
    }
}