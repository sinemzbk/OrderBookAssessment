import java.time.LocalDateTime;

public class SellOrder extends Order implements Comparable<SellOrder> {

    public SellOrder(Double price, int initialQuantity, Side side) {
        super(price, initialQuantity, side);
    }

    @Override
    public int compareTo(SellOrder o) {
        //ASC
        if (this.getPrice().compareTo(o.getPrice()) != 0) {
            return this.getPrice().compareTo(o.getPrice());
        } else {
            return this.getModifiedTime().compareTo(o.getModifiedTime());
        }
    }
}
