import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class OrderBook {
    private SortedSet<BuyOrder> buyLimitOrderBook = new TreeSet<>();
    private SortedSet<SellOrder> sellLimitOrderBook = new TreeSet<>();

    public static void main(String[] args) {

        OrderBook orderBook = new OrderBook();

        orderBook.add(20.0, 5, Side.BUY, null);
        orderBook.add(30.0, 3, Side.BUY, null);
        orderBook.add(33.0, 9, Side.BUY, null);
        orderBook.add(35.0, 3, Side.BUY, null);//should be first buy order
        orderBook.add(100.0, 4, Side.SELL, null);//should be first sell order
        orderBook.add(200.00, 10, Side.SELL, null);
        orderBook.add(120.0, 7, Side.SELL, null);
        System.out.println("buyLimitOrderBook Order Book " + orderBook.buyLimitOrderBook);
        System.out.println("sellLimitOrderBook Order Book " + orderBook.sellLimitOrderBook);

        //modify buy order for 33.0rand to quantity 6
        orderBook.buyLimitOrderBook.stream().filter(o -> o.getPrice().equals(33.0)).findFirst()
                .ifPresent(order -> orderBook.modify(order.getId(), 6));

        System.out.println("buyLimitOrderBook Order Book " + orderBook.buyLimitOrderBook);
        //delete first sell order -> 100rand
        //now first sell order should be 120rand
        orderBook.sellLimitOrderBook.stream().filter(o -> o.getPrice().equals(100.0)).findFirst()
                .ifPresent(orderBook::delete);
        System.out.println("sellLimitOrderBook Order Book " + orderBook.sellLimitOrderBook);
        //add more orders
        orderBook.add(90.0, 7, Side.SELL, null);
        orderBook.add(50.0, 8, Side.SELL, null);
        orderBook.add(20.0, 3, Side.SELL, null);
        orderBook.add(12.9, 4, Side.SELL, null);//new first sell order
        orderBook.add(30.0, 3, Side.BUY, null);
        System.out.println("Final buy order Book " + orderBook.buyLimitOrderBook);
        System.out.println("Final sell order Book " + orderBook.sellLimitOrderBook);
        System.out.println("buy order Book by price and side " + orderBook.getOrderByPriceAndSide(30.0, Side.BUY));
        System.out.println("sell order Book by price and side " + orderBook.getOrderByPriceAndSide(12.9, Side.SELL));

        //should print an error message.
        orderBook.add(30.0, 0, Side.BUY, null);
    }

    private void add(Double price, int quantity, Side side, LocalDateTime previousCreationTime) {
        try {
            if (Side.findByName(side.name()) != null && quantity > 0) {
                switch (side) {
                    case BUY:
                        BuyOrder buyOrder = new BuyOrder(price, quantity, side, previousCreationTime);
                        buyLimitOrderBook.add(buyOrder);
                        break;
                    case SELL:
                        SellOrder sellOrder = new SellOrder(price, quantity, side, previousCreationTime);
                        sellLimitOrderBook.add(sellOrder);
                        break;
                    default:
                        System.out.println("Error invalid side.");
                }
            } else {
                System.out.println("Error: invalid quantity or side input. Please check values and try again.");
            }
        } catch (Exception e) {
            System.out.println("Error adding order.");
        }
    }

    private void delete(Order order) {
        try {
            switch (order.getSide()) {
                case BUY:
                    buyLimitOrderBook.removeIf(o -> o.getId().equals(order.getId()) && buyLimitOrderBook.contains(o));
                    break;
                case SELL:
                    sellLimitOrderBook.removeIf(o -> o.getId().equals(order.getId()) && sellLimitOrderBook.contains(o));
                    break;
                default:
                    System.out.println("Error invalid side.");
            }
        } catch (Exception e) {
            System.out.println("Error removing order.");
        }
    }

    private void modify(UUID id, int newQuantity) {
        try {
            Order order = buyLimitOrderBook.stream().filter(o -> o.getId().equals(id)).findFirst().orElse(null);

            if (order != null) {
                delete(order);
                int quantity = order.getQuantity() == newQuantity ? order.getQuantity() : newQuantity;
                add(order.getPrice(), quantity, order.getSide(), order.getCreationTime());
            } else {
                System.out.println("Error: order does not exist.");
            }
        } catch (Exception e) {
            System.out.println("Error modifying order.");
        }
    }

    private Set<Order> getOrderByPriceAndSide(double price, Side side) {
        try {
            switch (side) {
                case BUY:
                    return buyLimitOrderBook.stream().filter(o -> o.getPrice().equals(price)).collect(Collectors.toSet());
                case SELL:
                    return sellLimitOrderBook.stream().filter(o -> o.getPrice().equals(price)).collect(Collectors.toSet());
                default:
                    System.out.println("Error invalid side.");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving orders.");
        }
        return null;
    }
}