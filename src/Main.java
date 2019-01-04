import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        try {
            Service service = new Service();
            service.createShop("Shop4", "Address41");
//            service.createProduct("Product4");
            List<Consignment> consignments = new ArrayList<>();
            consignments.add(new Consignment("Product4", 20, new BigDecimal(20)));
//            consignments.add(new Consignment("Product3", 15, new BigDecimal(25)));
            service.addProducts("Shop1", "Address11", consignments);
            consignments.clear();
//            List<Consignment> consignmentsList = new ArrayList<>();
//            consignmentsList.add(new Consignment("Product1", 5));
//            consignmentsList.add(new Consignment("Product2", 5));
//            System.out.println(service.checkProducts(1, consignmentsList));
//
//            Consignment consignment = service.getHowMany(1, BigDecimal.valueOf(10)).get(0);
//            System.out.println(consignment.getProduct().getName() + " " + consignment.getAmount());
//            Shop shop = service.findShopWithCheapestProduct(new Product("Product3"));
//            System.out.println(shop.getName());
//
//            consignmentsList.clear();
//            consignmentsList.add(new Consignment("Product1", 5));
//            consignmentsList.add(new Consignment("Product2", 5));
//
//            shop = service.findShopWithCheapestSet(consignmentsList);
//            System.out.println(shop.getName() + " " + shop.getAddress());
        }
        catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
