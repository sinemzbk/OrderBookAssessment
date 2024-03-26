public enum Side {
    BUY("Buy"), //ordered by price DESC
    SELL("Sell"); //ordered by price ASC

    public final String description;

    private Side(String description) {
        this.description = description;
    }

    public static Side findByName(String name) {
        Side result = null;
        for (Side side : values()) {
            if (side.name().equalsIgnoreCase(name)) {
                result = side;
                break;
            }
        }
        return result;
    }
    public static Side getOppositeSide(Side side){
        switch (side) {
            case BUY:
                return Side.SELL;
            case SELL:
                return Side.BUY;
            default:
                return null;
        }
    }
}
