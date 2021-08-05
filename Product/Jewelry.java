package Product;

public class Jewelry extends Product {
    private String material;
    private boolean gem;

    Jewelry() {super();}

    Jewelry(String material, boolean gem) {
        this.material = material;
        this.gem = gem;
    }

    Jewelry(int id, String name, double minimumPrice, int year, String material, boolean gem) {
        super(id, name, minimumPrice, year);
        this.material = material;
        this.gem = gem;
    }

    @Override
    public String toString() {
        return super.toString() + "MATERAL:" + material + " IS_GEM:" + gem;
    }

    public String getMaterial() {
        return material;
    }

    public boolean isGem() {
        return gem;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public void setGem(boolean gem) {
        this.gem = gem;
    }
}
