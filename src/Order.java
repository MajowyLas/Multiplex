import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Order {

    private final String id;
    private final Customer customer; // może być null (zakup bez konta)
    private final List<Ticket> tickets;
    private final LocalDateTime purchaseAt;

    public Order(Customer customer, List<Ticket> tickets, LocalDateTime purchaseAt) {
        this.id = UUID.randomUUID().toString();
        this.customer = customer; // nullable
        this.tickets = new ArrayList<>(Objects.requireNonNull(tickets));
        this.purchaseAt = Objects.requireNonNull(purchaseAt);
    }

    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Ticket> getTickets() {
        return List.copyOf(tickets);
    }

    public LocalDateTime getPurchaseAt() {
        return purchaseAt;
    }
}
