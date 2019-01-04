import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class FileDAO implements DAO {

    private static final String shopsFileName = "shops.csv";
    private static final String catalogFileName = "catalog.csv";

    private int lastShopID = 0;

    private List<Shop> shops;
    private Map<Product, List<Tuple>> tuples;

    public FileDAO() throws IOException {
        shops = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(shopsFileName));
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
        for (CSVRecord csvRecord : csvParser) {
            lastShopID = Integer.max(lastShopID, Integer.parseInt(csvRecord.get(0)));
            shops.add(new Shop(Integer.parseInt(csvRecord.get(0)), csvRecord.get(1), csvRecord.get(2)));
        }
        csvParser.close();
        reader.close();

        tuples = new HashMap<>();
        reader = new BufferedReader(new FileReader(catalogFileName));
        csvParser = new CSVParser(reader, CSVFormat.DEFAULT);
        for (CSVRecord csvRecord : csvParser) {
            Product product = new Product(csvRecord.get(0));
            if (csvRecord.size() == 1) {
                tuples.put(product, new ArrayList<>());
            }
            else {
                for (int i = 1; i < csvRecord.size(); i += 3) {
                    try {
                        int shopID = Integer.parseInt(csvRecord.get(i));
                        int amount = Integer.parseInt(csvRecord.get(i + 1));
                        BigDecimal cost = new BigDecimal(csvRecord.get(i + 2));
                        if (tuples.containsKey(product)) {
                            tuples.get(product).add(new Tuple(shopID, amount, cost));
                        } else {
                            tuples.put(product, new ArrayList<>());
                            tuples.get(product).add(new Tuple(shopID, amount, cost));
                        }
                    }
                    catch(IndexOutOfBoundsException e){
                        throw new IOException("Incorrect input file");
                    }
                }
            }
        }
        csvParser.close();
        reader.close();
    }

    private void rewrite(String filename) throws IOException {
        new File(filename).delete();
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        switch (filename) {
            case shopsFileName : {
                for (Shop shop : shops) {
                    csvPrinter.printRecord(Integer.toString(shop.getID()), shop.getName(), shop.getAddress());
                }
                break;
            }
            case catalogFileName : {
                List<String> newLineList = new ArrayList<>();
                for (Product product : tuples.keySet()) {
                    newLineList.clear();
                    newLineList.add(product.getName());
                    for (Tuple tuple : tuples.get(product)) {
                        newLineList.add(Integer.toString(tuple.getShopID()));
                        newLineList.add(Integer.toString(tuple.getAmount()));
                        newLineList.add(tuple.getCost().toString());
                    }
                    if (!newLineList.isEmpty()) {
                        csvPrinter.printRecord(newLineList);
                    }
                }
                break;
            }
            default : {
                throw new IOException();
            }
        }
        csvPrinter.close();
        writer.close();
    }

    @Override
    public void createShop(Shop shop) throws IOException {
        shops.add(new Shop(++lastShopID, shop.getName(), shop.getAddress()));
        this.rewrite(shopsFileName);
    }

    @Override
    public void createProduct(Product product) throws IOException {
        tuples.put(product, new ArrayList<>());
        this.rewrite(catalogFileName);
    }

    @Override
    public void addProduct(CatalogRecord inputCatalogRecord) throws IOException {
        for (Tuple tuple : tuples.get(inputCatalogRecord.getProduct())) {
            if (tuple.getShopID() == inputCatalogRecord.getShopID()) {
                tuple.increaseAmount(inputCatalogRecord.getAmount());
                tuple.setCost(inputCatalogRecord.getCost());
                this.rewrite(catalogFileName);
                return;
            }
        }
        Tuple newTuple = new Tuple(inputCatalogRecord.getShopID(),
                                    inputCatalogRecord.getAmount(),
                                    inputCatalogRecord.getCost());
        tuples.get(inputCatalogRecord.getProduct()).add(newTuple);
        this.rewrite(catalogFileName);
    }

    @Override
    public BigDecimal checkProduct(CatalogRecord inputCatalogRecord) {
        for (Tuple tuple : tuples.get(inputCatalogRecord.getProduct())) {
            if (tuple.getShopID() == inputCatalogRecord.getShopID()) {
                return tuple.getAmount() < inputCatalogRecord.getAmount()
                        ? null
                        : tuple.getCost().multiply(BigDecimal.valueOf(inputCatalogRecord.getAmount()));
            }
        }
        return null;
    }

    @Override
    public Shop getShop(int shopID) {
        for (Shop shop : shops) {
            if (shop.getID() == shopID) {
                return shop;
            }
        }
        return null;
    }

    @Override
    public Shop getShop(String shopName, String shopAddress) {
        for (Shop shop : shops) {
            if (shop.getName().equals(shopName) && shop.getAddress().equals(shopAddress)) {
                return shop;
            }
        }
        return null;
    }

    @Override
    public List<Shop> getShops() {
        return new ArrayList<>(shops);
    }

    @Override
    public List<CatalogRecord> getCatalogRecords() {
        List<CatalogRecord> response = new ArrayList<>();
        for (Product product : tuples.keySet()) {
            for (Tuple tuple : tuples.get(product)) {
                response.add(new CatalogRecord(tuple.getShopID(),
                                                product.getName(),
                                                tuple.getCost(),
                                                tuple.getAmount()));
            }
        }
        return response;
    }

    @Override
    public List<CatalogRecord> getCatalogRecords(int shopID) {
        return getCatalogRecords().stream()
                .filter(record -> record.getShopID() == shopID)
                .collect(Collectors.toList());
    }

    @Override
    public List<CatalogRecord> getCatalogRecords(Product product) {
        return getCatalogRecords().stream()
                .filter(record -> record.getProduct().equals(product))
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getProducts() {
        return new ArrayList<>(tuples.keySet());
    }
}
