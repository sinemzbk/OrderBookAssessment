public class SellOrder extends Order implements Comparable<SellOrder> {

    public SellOrder(Double price, int quantity, Side side) {
        super(price, quantity, side);
    }

    @Override
    public int compareTo(SellOrder o) {
        //ASC
        if (this.getPrice().compareTo(o.getPrice()) != 0) {
            return this.getPrice().compareTo(o.getPrice());
        } else {
            return this.getCreationTime().compareTo(o.getCreationTime());
        }
    }
}
