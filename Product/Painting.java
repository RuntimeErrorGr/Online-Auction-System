package Product;

public class Painting extends Product {
    private String painter;
    private ColorsType colorsType;

    Painting() { super();}

    Painting(String painter, ColorsType colorsType) {
        this.painter = painter;
        this.colorsType = colorsType;
    }

    Painting(int id, String name, double minimumPrice, int an, String painter, ColorsType colorsType) {
        super(id, name, minimumPrice, an);
        this.painter = painter;
        this.colorsType = colorsType;
    }

    @Override
    public String toString() {
        return super.toString() + "PAINTER:" + painter + " COLORS:" + colorsType;
    }

    public String getPainter() {
        return painter;
    }

    public ColorsType getColorsType() {
        return colorsType;
    }

    public void setPainter(String painter) {
        this.painter = painter;
    }

    public void setColorsType(ColorsType colorsType) {
        this.colorsType = colorsType;
    }
}
