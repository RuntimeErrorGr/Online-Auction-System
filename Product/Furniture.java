package Product;

public class Furniture extends Product {
    private String type;
    private String material;

    Furniture() {super();}

    Furniture(String type, String material) {
        this.type = type;
        this.material = material;
    }

    @Override
    public String toString() {
        return super.toString() + "TYPE:"+ type + " MATERIAL:" + material;
    }

    Furniture(int id, String name, double minimumPrice, int year, String type, String material) {
        super(id, name, minimumPrice, year);
        this.type = type;
        this.material = material;
    }

    public String getType() {
        return type;
    }

    public String getMaterial() {
        return material;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
