import java.time.LocalDateTime;

public class BuyOrder extends Order implements Comparable<BuyOrder> {

    public BuyOrder(Double price, int initialQuantity, Side side) {
        super(price, initialQuantity, side);
    }

    @Override
    public int compareTo(BuyOrder o) {
        //DESC
        if (o.getPrice().compareTo(this.getPrice()) != 0) {
            return o.getPrice().compareTo(this.getPrice());
        } else {
            return o.getModifiedTime().compareTo(this.getModifiedTime());
        }
    }
}
