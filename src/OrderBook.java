import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class OrderBook {
    private SortedSet<BuyOrder> buyLimitOrderBook = new TreeSet<>();
    private SortedSet<SellOrder> sellLimitOrderBook = new TreeSet<>();

    public static void main(String[] args) {

        OrderBook orderBook = new OrderBook();

        orderBook.add(20.0, 5, Side.BUY);
        orderBook.add(30.0, 3, Side.BUY);
        orderBook.add(33.0, 9, Side.BUY);
        orderBook.add(35.0, 3, Side.BUY);//should be first buy order
        orderBook.add(100.0, 4, Side.SELL);//should be first sell order
        orderBook.add(200.00, 10, Side.SELL);
        orderBook.add(120.0, 7, Side.SELL);
        System.out.println("buyLimitOrderBook Order Book " + orderBook.buyLimitOrderBook);
        System.out.println("sellLimitOrderBook Order Book " + orderBook.sellLimitOrderBook);

//        //modify buy order for 33.0rand to quantity 6
//        orderBook.buyLimitOrderBook.stream().filter(o -> o.getPrice().equals(33.0)).findFirst()
//                .ifPresent(order -> orderBook.modify(order.getId(), 6));

//        System.out.println("buyLimitOrderBook Order Book " + orderBook.buyLimitOrderBook);
        //delete first sell order -> 100rand
        //now first sell order should be 120rand
//        orderBook.sellLimitOrderBook.stream().filter(o -> o.getPrice().equals(100.0)).findFirst()
//                .ifPresent(orderBook::delete);
//        System.out.println("sellLimitOrderBook Order Book " + orderBook.sellLimitOrderBook);
        //add more orders
        orderBook.add(90.0, 7, Side.SELL);
        orderBook.add(50.0, 8, Side.SELL);
        orderBook.add(20.0, 3, Side.SELL);
        orderBook.add(12.9, 4, Side.SELL);
        orderBook.add(30.0, 3, Side.BUY);
        orderBook.add(90.0, 3, Side.SELL);
        System.out.println("Final buy order Book before matching " + orderBook.buyLimitOrderBook);
        System.out.println("Final sell order Book before matching " + orderBook.sellLimitOrderBook);
        System.out.println("sell order Book by price and side " + orderBook.getOrderByPriceAndSide(90.0, Side.SELL));
        System.out.println("buy order Book by price and side " + orderBook.getOrderByPriceAndSide(30.0, Side.BUY));

        //should print an error message and not add to order book due to quantity being 0
        //Error: invalid quantity or side input. Please check values and try again.
        orderBook.add(30.0, 0, Side.BUY);
        Order sellOrder = new SellOrder(30.0, 10, Side.SELL);
        orderBook.match(sellOrder);
        System.out.println("Final buy order Book after matching " + orderBook.buyLimitOrderBook);
        System.out.println("Remaining to be matched by price after matching " + sellOrder);

        Order buyOrder = new BuyOrder(90.0, 11, Side.BUY);
        orderBook.match(buyOrder);
        //first entry should be an open buy order at 90rand with quantity of 1
        System.out.println("Final sell order Book after matching " + orderBook.sellLimitOrderBook);
        System.out.println("Remaining to be matched by price after matching " + buyOrder);

    }

    protected void match(Order order) {
        try {
            Side oppositeSide = Side.getOppositeSide(order.getSide());
            SortedSet<Order> orders = getOrderByPriceAndSide(order.getPrice(), oppositeSide);
            System.out.println("orders " + orders);
            if (orders != null) {
                for (Order matchOrder : orders) {
                    int currentQuantity = matchOrder.getCurrentQuantity();
                    int quantity = order.getCurrentQuantity();
                    modify(matchOrder.getId(), quantity);

                    order.setCurrentQuantity(Math.max(quantity - currentQuantity, 0));
                    order.setModifiedTime(LocalDateTime.now());
                    if (order.getCurrentQuantity() == 0) { //once order quantity fully match exit
                        break;
                    }
                }
                //if order not fully matched add it to the order book ordering by price and
                //modified date, so it can than be matched as well
                if (order.getCurrentQuantity() != 0) {
                    add(order.getPrice(), order.getCurrentQuantity(), order.getSide());
                }
            }
        } catch (Exception e) {
            System.out.println("Error removing order.");
        }
    }

    private void add(Double price, int quantity, Side side) {
        try {
            if (Side.findByName(side.name()) != null && quantity > 0) {
                switch (side) {
                    case BUY:
                        BuyOrder buyOrder = new BuyOrder(price, quantity, side);
                        buyLimitOrderBook.add(buyOrder);
                        break;
                    case SELL:
                        SellOrder sellOrder = new SellOrder(price, quantity, side);
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
            Order order = sellLimitOrderBook.stream().filter(o -> o.getId().equals(id)).findFirst().orElse(null);
            if (order == null)
                order = buyLimitOrderBook.stream().filter(o -> o.getId().equals(id)).findFirst().orElse(null);

            if (order != null) {
                order.setCurrentQuantity(Math.max(order.getCurrentQuantity() - newQuantity, 0));
                order.setModifiedTime(LocalDateTime.now());
                if (order.getCurrentQuantity() == 0) {
                    order.setState(State.FULLY_FILLED);
                    delete(order);
                } else {
                    order.setState(State.PARTIALLY_FILLED);
                }
            } else {
                System.out.println("Error: order does not exist.");
            }
        } catch (Exception e) {
            System.out.println("Error modifying order.");
        }
    }

    private SortedSet<Order> getOrderByPriceAndSide(Double price, Side side) {

        try {
            switch (side) {
                case BUY:
                    return buyLimitOrderBook.parallelStream().filter(o -> Double.compare(o.getPrice(), price) == 0)
                            .collect(Collectors.toCollection(TreeSet::new));
                case SELL:
                    return sellLimitOrderBook.parallelStream().filter(o -> Double.compare(o.getPrice(), price) == 0)
                            .collect(Collectors.toCollection(TreeSet::new));
                default:
                    throw new IllegalArgumentException("Error Invalid side: " + side);
            }
        } catch (Exception e) {
            System.out.println("Error retrieving orders.");
        }
        return null;
    }
}