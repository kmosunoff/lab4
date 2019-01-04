import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseDAO implements DAO {

    private Connection connection;
    private Statement statement;

    public DatabaseDAO() throws SQLException{
        this.connection = getConnection();
        this.statement = connection.createStatement();
    }

    public Connection getConnection() throws SQLException {
        Properties connectionProps = new Properties();
        connectionProps.put("user", "user");
        connectionProps.put("password", "user");

        return DriverManager.getConnection(
                "jdbc:sqlserver://127.0.0.1:1433;database=ShopsAndProducts",
                connectionProps
        );
    }

    @Override
    public void createShop(Shop shop) throws SQLException{
        String query = "INSERT INTO Shops VALUES ('" + shop.getName() + "', '" + shop.getAddress() + "')";
        statement.executeUpdate(query);
    }

    @Override
    public void createProduct(Product product) throws SQLException{
        String query = "INSERT INTO Products VALUES ('" + product.getName() + "')";
        statement.executeUpdate(query);
    }

    @Override
    public void addProduct(CatalogRecord catalogRecord) throws SQLException{
        String query = "SELECT * FROM ShopsProducts WHERE ShopID = " + catalogRecord.getShopID()
                        + " AND ProductName = '" + catalogRecord.getProduct().getName() + "'";
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            query = "UPDATE ShopsProducts SET Amount = Amount + " + catalogRecord.getAmount()
                    + " WHERE ShopID = " + catalogRecord.getShopID() + ""
                    + " AND ProductName = '" + catalogRecord.getProduct().getName() + "'";
            statement.executeUpdate(query);
            query = "UPDATE ShopsProducts SET Cost = " + catalogRecord.getCost()
                    + " WHERE ShopID = " + catalogRecord.getShopID() + ""
                    + " AND ProductName = '" + catalogRecord.getProduct().getName() + "'";
            statement.executeUpdate(query);
        }
        else {
            query = "INSERT INTO ShopsProducts VALUES ("
                    + catalogRecord.getShopID() + ", '"
                    + catalogRecord.getProduct().getName() + "', "
                    + catalogRecord.getCost() + ", "
                    + catalogRecord.getAmount() + ")";
            statement.executeUpdate(query);
        }
    }

    @Override
    public BigDecimal checkProduct(CatalogRecord catalogRecord) throws SQLException {
        String query = "SELECT * FROM ShopsProducts WHERE ShopID = " + catalogRecord.getShopID()
                        + " AND ProductName = '" + catalogRecord.getProduct().getName() + "'";
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            if (resultSet.getInt("Amount") < catalogRecord.getAmount()) {
                return null;
            }
            else {
                return new BigDecimal(resultSet.getString("Cost"))
                        .multiply(BigDecimal.valueOf(catalogRecord.getAmount()));
            }
        }
        return null;
    }

    @Override
    public Shop getShop(int shopID) throws SQLException {
        String query = "SELECT * FROM Shops WHERE ShopID = " + shopID;
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return new Shop(resultSet.getInt("ShopID"),
                            resultSet.getString("ShopName"),
                            resultSet.getString("ShopAddress"));
        }
        else {
            return null;
        }
    }

    @Override
    public Shop getShop(String shopName, String shopAddress) throws SQLException {
        String query = "SELECT * FROM Shops WHERE ShopName = '" + shopName
                        + "' AND ShopAddress = '" + shopAddress + "'";
        ResultSet resultSet = statement.executeQuery(query);
        if (resultSet.next()) {
            return new Shop(resultSet.getInt("ShopID"),
                    resultSet.getString("ShopName"),
                    resultSet.getString("ShopAddress"));
        }
        else {
            return null;
        }
    }

    @Override
    public List<Shop> getShops()  throws SQLException{
        List<Shop> response = new ArrayList<>();
        String query = "SELECT * FROM Shops";
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            Shop shop = new Shop(resultSet.getInt("ShopID"),
                                    resultSet.getString("ShopName"),
                                    resultSet.getString("ShopAddress"));
            response.add(shop);
        }
        return response;
    }

    @Override
    public List<CatalogRecord> getCatalogRecords() throws SQLException{
        List<CatalogRecord> response = new ArrayList<>();
        String query = "SELECT * FROM ShopsProducts";
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            CatalogRecord catalogRecord = new CatalogRecord(
                    resultSet.getInt("ShopID"),
                    resultSet.getString("ProductName"),
                    new BigDecimal(resultSet.getString("Cost")),
                    resultSet.getInt("Amount"));
            response.add(catalogRecord);
        }
        return response;
    }

    @Override
    public List<CatalogRecord> getCatalogRecords(int shopID) throws SQLException {
        List<CatalogRecord> response = new ArrayList<>();
        String query = "SELECT * FROM ShopsProducts WHERE ShopID = " + shopID;
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            CatalogRecord catalogRecord = new CatalogRecord(
                    resultSet.getInt("ShopID"),
                    resultSet.getString("ProductName"),
                    new BigDecimal(resultSet.getString("Cost")),
                    resultSet.getInt("Amount"));
            response.add(catalogRecord);
        }
        return response;
    }

    @Override
    public List<CatalogRecord> getCatalogRecords(Product product) throws SQLException{
        List<CatalogRecord> response = new ArrayList<>();
        String query = "SELECT * FROM ShopsProducts WHERE ProductName = '" + product.getName() + "'";
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            CatalogRecord catalogRecord = new CatalogRecord(
                    resultSet.getInt("ShopsProductsID"),
                    resultSet.getInt("ShopID"),
                    resultSet.getString("ProductName"),
                    new BigDecimal(resultSet.getString("Cost")),
                    resultSet.getInt("Amount"));
            response.add(catalogRecord);
        }
        return response;
    }

    @Override
    public List<Product> getProducts() throws SQLException{
        List<Product> response = new ArrayList<>();
        String query = "SELECT * FROM Products";
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            Product product = new Product(resultSet.getString("ProductName"));
            response.add(product);
        }
        return response;
    }
}
