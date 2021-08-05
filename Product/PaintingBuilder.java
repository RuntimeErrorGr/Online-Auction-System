package Product;

public class PaintingBuilder {
    private String painter;
    private ColorsType colorsType;
    private int id;
    private String name;
    private double sellingPrice;
    private double minimumPrice;
    private int year;

    public PaintingBuilder setPainter(String painter) {
        this.painter = painter;
        return this;
    }

    public PaintingBuilder setColorsType(ColorsType colorsType) {
        this.colorsType = colorsType;
        return this;
    }

    public PaintingBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public PaintingBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PaintingBuilder setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
        return this;
    }

    public PaintingBuilder setMinimumPrice(double minimumPrice) {
        this.minimumPrice = minimumPrice;
        return this;
    }

    public PaintingBuilder setYear(int year) {
        this.year = year;
        return this;
    }

    public Painting build() {
        return new Painting(id, name, minimumPrice, year, painter, colorsType);
    }
}