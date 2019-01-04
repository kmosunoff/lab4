import java.math.BigDecimal;

public class Consignment {

    private Product product;
    private int amount;
    private BigDecimal cost;

    public Consignment(String productName, int amount, BigDecimal cost) {
        this.product = new Product(productName);
        this.amount =  amount;
        this.cost = cost;
    }

    public Product getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }

    public BigDecimal getCost() {
        return cost;
    }
}
