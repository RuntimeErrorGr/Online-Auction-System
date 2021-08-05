package Product;

public class FurnitureBuilder {
    private String type;
    private String material;
    private int id;
    private String name;
    private double sellingPrice;
    private double minimumPrice;
    private int year;

    public FurnitureBuilder setType(String type) {
        this.type = type;
        return this;
    }

    public FurnitureBuilder setMaterial(String material) {
        this.material = material;
        return this;
    }

    public FurnitureBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public FurnitureBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public FurnitureBuilder setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
        return this;
    }

    public FurnitureBuilder setMinimumPrice(double minimumPrice) {
        this.minimumPrice = minimumPrice;
        return this;
    }

    public FurnitureBuilder setYear(int year) {
        this.year = year;
        return this;
    }

    public Furniture build() {
        return new Furniture(id, name, minimumPrice, year, type, material);
    }
}