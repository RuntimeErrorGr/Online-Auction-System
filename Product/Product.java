package Product;
import Auction.AuctionHouse;

public abstract class Product {
    private int id;
    private boolean sold;
    private String name;
    private double sellingPrice;
    private double minimumPrice;
    private int year;
    private boolean activeAuction;

    Product() {}

    Product(int id, String name, double minimumPrice, int year) {
        this.id = id;
        this.name = name;
        this.minimumPrice = minimumPrice;
        this.year = year;
        this.activeAuction = false;
        this.sellingPrice = -1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder()
                .append(id)
                .append(" ")
                .append(name)
                .append(" YEAR:")
                .append(year);
        if (sellingPrice > 0) {
            sb.append(" SOLD_FOR:").append(sellingPrice).append(" ");
        } else {
            sb.append(" STARTING_PRICE:").append(minimumPrice).append(" ");
        }
        return sb.toString();
    }

    /**
     * Pe baza unui id, cauta produsul in lista de produse a casei de licitatii.
     * @param id id-ul produsului cautat
     * @return produsul.
     */
    public static Product getProductByID(int id) {
        for (Product p: AuctionHouse.getInstance().getProducts()) {
            if(p.getId() == id) {
                return p;
            }
        }
        return AuctionHouse.getInstance().getProducts().get(0);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public double getMinimumPrice() {
        return minimumPrice;
    }

    public int getYear() {
        return year;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public void setMinimumPrice(double minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isActiveAuction() {
        return activeAuction;
    }

    public void setActiveAuction(boolean activeAuction) {
        this.activeAuction = activeAuction;
    }

    public boolean isSold() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold = sold;
    }
}

