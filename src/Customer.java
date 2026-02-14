import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Customer {

    private final String id;
    private final String name;
    private final List<Order> orders = new ArrayList<>();

    public Customer(String id, String name) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Customer id cannot be blank");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Customer name cannot be blank");
        this.id = id;
        this.name = name;
    }

    public void addOrder(Order order) {
        orders.add(Objects.requireNonNull(order));
    }

    public List<Ticket> getTickets() {
        List<Ticket> out = new ArrayList<>();
        for (Order o : orders) out.addAll(o.getTickets());
        return out;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
