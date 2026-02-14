import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Reservation {

    private final String id;
    private final Customer customer; // może być null (guest)
    private final Screening screening;
    private final List<Seat> seats;

    private ReservationStatus status;

    private final LocalDateTime createdAt;
    private final LocalDateTime expiresAt;

    public Reservation(Customer customer,
                       Screening screening,
                       List<Seat> seats,
                       LocalDateTime createdAt,
                       LocalDateTime expiresAt) {

        this.id = UUID.randomUUID().toString();
        this.customer = customer; // nullable
        this.screening = Objects.requireNonNull(screening, "screening cannot be null");

        if (seats == null || seats.isEmpty()) {
            throw new IllegalArgumentException("seats cannot be null/empty");
        }
        this.seats = List.copyOf(seats);

        this.createdAt = Objects.requireNonNull(createdAt, "createdAt cannot be null");
        this.expiresAt = Objects.requireNonNull(expiresAt, "expiresAt cannot be null");

        if (!expiresAt.isAfter(createdAt)) {
            throw new IllegalArgumentException("expiresAt must be after createdAt");
        }

        this.status = ReservationStatus.ACTIVE;
    }

    // ===== getters =====

    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Screening getScreening() {
        return screening;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    // ===== domain rules =====

    public boolean isExpired(LocalDateTime now) {
        Objects.requireNonNull(now, "now cannot be null");
        return !now.isBefore(expiresAt); // now >= expiresAt
    }

    public boolean isActive(LocalDateTime now) {
        return status == ReservationStatus.ACTIVE && !isExpired(now);
    }

    /** Zmienia status na EXPIRED tylko jeśli rezerwacja jest ACTIVE i TTL minął. */
    public boolean expireIfNeeded(LocalDateTime now) {
        if (status == ReservationStatus.ACTIVE && isExpired(now)) {
            status = ReservationStatus.EXPIRED;
            return true;
        }
        return false;
    }

    public void cancel() {
        if (status == ReservationStatus.CONVERTED) {
            throw new IllegalStateException("Cannot cancel a converted reservation");
        }
        status = ReservationStatus.CANCELLED;
    }

    public void convert() {
        if (status != ReservationStatus.ACTIVE) {
            throw new IllegalStateException("Only ACTIVE reservation can be converted");
        }
        status = ReservationStatus.CONVERTED;
    }

    // ===== pretty print =====

    public String formatSummary() {
        String customerInfo =
                (customer == null)
                        ? "GUEST"
                        : customer.getName() + " (" + customer.getId() + ")";

        var seatCodes = seats.stream()
                .map(Seat::getCode)
                .toList();

        return """
                ============== YOUR RESERVATION ==========
                Id       : %s
                Customer : %s
                Status   : %s
                Created  : %s
                Expires  : %s
                Seats    : %s
                ========================================
                """.formatted(
                id,
                customerInfo,
                status,
                createdAt,
                expiresAt,
                seatCodes
        );
    }

    //dla logów
    @Override
    public String toString() {
        return "Reservation{" +
                "id='" + id + '\'' +
                ", status=" + status +
                ", expiresAt=" + expiresAt +
                ", seats=" + seats.size() +
                '}';
    }
}
