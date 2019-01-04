import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Service{

    private DAO data;

    public Service() throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new FileReader("mode.property"));
        String mode = reader.readLine();
        reader.close();
        if (mode.equals("database")) {
            data = new DatabaseDAO();
        }
        else if (mode.equals("file")) {
            data = new FileDAO();
        }
        else {
            throw new IOException("Incorrect mode");
        }
    }

    public void createShop(String shopName, String shopAddress) throws SQLException, IOException {
        Shop shop = new Shop(shopName, shopAddress);
        data.createShop(shop);
    }

    public void createProduct(String productName) throws SQLException, IOException {
        Product product = new Product(productName);
        data.createProduct(product);
    }

    public void addProducts(String shopName, String shopAddress, List<Consignment> consignments) throws SQLException, IOException {
        Shop shop = data.getShop(shopName, shopAddress);
        if (shop == null) {
            throw new IllegalArgumentException("shopName");
        }
        for (Consignment consignment : consignments) {
            data.addProduct(new CatalogRecord(shop.getID(),
                                                consignment.getProduct().getName(),
                                                consignment.getCost(),
                                                consignment.getAmount()));
        }
    }

    public BigDecimal checkProducts(int shopID, List<Consignment> consignments) throws SQLException, IOException {
        BigDecimal sum = new BigDecimal(0);
        for (Consignment consignment : consignments) {
            BigDecimal currentCost = data.checkProduct(new CatalogRecord(
                                                    shopID,
                                                    consignment.getProduct().getName(),
                                                    consignment.getAmount()));
            if (currentCost == null) {
                return null;
            }
            else {
                sum.add(currentCost);
            }
        }
        return sum;
    }

    public Shop findShopWithCheapestProduct(Product product) throws SQLException, IOException {
        List<CatalogRecord> catalogRecords = data.getCatalogRecords(product);
        BigDecimal minCost = new BigDecimal(Double.toString(Double.MAX_VALUE));
        int shopID = 0;
        for (CatalogRecord catalogRecord : catalogRecords) {
            if (catalogRecord.getCost().compareTo(minCost) < 0) {
                minCost = catalogRecord.getCost();
                shopID = catalogRecord.getShopID();
            }
        }
        if (shopID > 0) {
            return data.getShop(shopID);
        }
        return null;
    }

    public List<Consignment> getHowMany(int shopID, BigDecimal sum) throws SQLException, IOException {
        List<CatalogRecord> catalogRecords = data.getCatalogRecords(shopID);
        List<Consignment> response = new ArrayList<>();
        for (CatalogRecord catalogRecord : catalogRecords) {
            response.add(new Consignment(catalogRecord.getProduct().getName(), sum.divide(catalogRecord.getCost()).intValue(), catalogRecord.getCost()));
        }
        return response;
    }

    public Shop findShopWithCheapestSet(List<Consignment> consignments) throws SQLException, IOException {
        Shop response = null;
        BigDecimal minCost = new BigDecimal(Double.toString(Double.MAX_VALUE));
        for (Shop shop : data.getShops()) {
            BigDecimal currentCostForShop = new BigDecimal(0);
            for (Consignment consignment : consignments) {
                BigDecimal costOfConsignment = data.checkProduct(new CatalogRecord(
                                                                shop.getID(),
                                                                consignment.getProduct().getName(),
                                                                consignment.getAmount()));
                if (costOfConsignment != null) {
                    currentCostForShop.add(costOfConsignment);
                }
                else {
                    currentCostForShop = null;
                    break;
                }
            }
            if (currentCostForShop != null && currentCostForShop.compareTo(minCost) < 0) {
                minCost = currentCostForShop;
                response = shop;
            }
        }
        return response;
    }

}
