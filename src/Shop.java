public class Shop {

    private int ID;
    private String name;
    private String address;

    public Shop() {
        this.ID = 0;
        this.name = "";
        this.address = "";
    }

    public Shop(int ID, String name, String address) {
        this.ID = ID;
        this.name = name;
        this.address = address;
    }

    public Shop(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

}
