import java.math.BigDecimal;

public class CatalogRecord {

    private int ID;
    private int shopID;
    private Product product;
    private BigDecimal cost;
    private int amount;

    public CatalogRecord(String productName) {
        this.product = new Product(productName);
    }

    public CatalogRecord(int shopID, String productName, int amount) {
        this.shopID = shopID;
        this.product = new Product(productName);
        this.amount = amount;
    }

    public CatalogRecord(int shopID, String productName, BigDecimal cost, int amount) {
        this.shopID = shopID;
        this.product = new Product(productName);
        this.cost = cost;
        this.amount = amount;
    }

    public CatalogRecord(int ID, int shopID, String productName, BigDecimal cost, int amount) {
        this.ID = ID;
        this.shopID = shopID;
        this.product = new Product(productName);
        this.cost = cost;
        this.amount = amount;
    }

    public int getID() {
        return ID;
    }

    public int getShopID() {
        return shopID;
    }

    public Product getProduct() {
        return product;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public int getAmount() {
        return amount;
    }

    public void increaseAmount(int additional) {
        this.amount += additional;
    }

    public void setNewCost(BigDecimal newCost) {
        this.cost = newCost;
    }

}
