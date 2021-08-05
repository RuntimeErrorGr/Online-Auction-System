package Product;

public class JewelryBuilder {
    private String material;
    private boolean gem;
    private int id;
    private String name;
    private double sellingPrice;
    private double minimumPrice;
    private int year;

    public JewelryBuilder setMaterial(String material) {
        this.material = material;
        return this;
    }

    public JewelryBuilder setGem(boolean gem) {
        this.gem = gem;
        return this;
    }

    public JewelryBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public JewelryBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public JewelryBuilder setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
        return this;
    }

    public JewelryBuilder setMinimumPrice(double minimumPrice) {
        this.minimumPrice = minimumPrice;
        return this;
    }

    public JewelryBuilder setYear(int year) {
        this.year = year;
        return this;
    }

    public Jewelry build() {
        return new Jewelry(id, name, minimumPrice, year, material, gem);
    }
}