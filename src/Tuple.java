import java.math.BigDecimal;

public class Tuple {
    private int shopID;
    private int amount;
    private BigDecimal cost;

    public Tuple(int shopID, int amount, BigDecimal cost) {
        this.shopID = shopID;
        this.amount = amount;
        this.cost = cost;
    }

    public int getShopID() {
        return shopID;
    }

    public int getAmount() {
        return amount;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public void increaseAmount(int amount) {
        this.amount += amount;
    }
}
