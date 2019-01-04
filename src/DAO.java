import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface DAO{
    void createShop(Shop shop) throws SQLException, IOException;
    void createProduct(Product product) throws SQLException, IOException;
    void addProduct(CatalogRecord catalogRecord) throws SQLException, IOException;
    BigDecimal checkProduct(CatalogRecord catalogRecord) throws SQLException, IOException;
    Shop getShop(int shopID) throws SQLException, IOException;
    Shop getShop(String shopName, String shopAddress) throws SQLException, IOException;
    List<Shop> getShops() throws SQLException, IOException;
    List<CatalogRecord> getCatalogRecords() throws SQLException, IOException;
    List<CatalogRecord> getCatalogRecords(int shopID) throws SQLException, IOException;
    List<CatalogRecord> getCatalogRecords(Product product) throws SQLException, IOException;
    List<Product> getProducts() throws SQLException, IOException;

}
