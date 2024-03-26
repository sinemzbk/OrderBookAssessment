import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Order {
    private UUID Id;
    private Double price;
    private int initialQuantity;
    private int currentQuantity;
    private State state;
    private Side side;
    private LocalDateTime creationTime;
    private LocalDateTime modifiedTime;

    public Order(Double price, int initialQuantity, Side side) {
        this.Id = UUID.randomUUID();
        this.price = price;
        this.initialQuantity = initialQuantity;
        this.currentQuantity = this.initialQuantity;
        this.side = side;
        this.state = State.OPEN;
        this.creationTime = LocalDateTime.now();
        this.modifiedTime = this.creationTime;
    }

    public UUID getId() {
        return Id;
    }

    public Double getPrice() {
        return price;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(int currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public Side getSide() {
        return side;
    }

    public void setState(State state) {
        this.state = state;
    }

    public LocalDateTime getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(LocalDateTime modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(Id, order.Id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "Id=" + Id +
                ", price=" + price +
                ", initialQuantity=" + initialQuantity +
                ", currentQuantity=" + currentQuantity +
                ", state=" + state +
                ", side=" + side +
                ", creationTime=" + creationTime +
                ", modifiedTime=" + modifiedTime +
                '}';
    }
}
