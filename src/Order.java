import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Order {
    private UUID Id;
    private Double price;
    private int quantity;
    private Side side;
    private LocalDateTime creationTime;
    private LocalDateTime modifiedTime;

    public Order(Double price, int quantity, Side side, LocalDateTime previousCreationTime) {
        this.Id = UUID.randomUUID();
        this.price = price;
        this.quantity = quantity;
        this.side = side;
        if (creationTime == null) { //new order
            this.creationTime = LocalDateTime.now();
            this.modifiedTime = this.creationTime;
        } else {
            this.creationTime = previousCreationTime;
            this.modifiedTime = LocalDateTime.now();
        }
    }

    public UUID getId() {
        return Id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public LocalDateTime getModifiedTime() {
        return modifiedTime;
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
                ", quantity=" + quantity +
                ", side=" + side +
                ", creationTime=" + creationTime +
                ", modifiedTime=" + modifiedTime +
                '}';
    }
}
