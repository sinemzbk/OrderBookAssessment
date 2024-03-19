public class BuyOrder extends Order implements Comparable<BuyOrder> {
    public BuyOrder(Double price, int quantity, Side side) {
        super(price, quantity, side);
    }

    @Override
    public int compareTo(BuyOrder o) {
        //DESC
        if (o.getPrice().compareTo(this.getPrice()) != 0) {
            return o.getPrice().compareTo(this.getPrice());
        } else {
            return o.getCreationTime().compareTo(this.getCreationTime());
        }
    }
}
