package Product;

import java.util.Scanner;

public class ProductFactory {
    private static ProductFactory unique;

    private ProductFactory() {}

    public static ProductFactory getInstance() {
        if (unique == null) {
            unique = new ProductFactory();
        }
        return unique;
    }

    public Product craft(ProductType type, Scanner scanner) {
        switch (type) {
            case PAINTING:
                return new PaintingBuilder()
                        .setId(Integer.parseInt(scanner.next()))
                        .setName(scanner.next())
                        .setMinimumPrice(Double.parseDouble(scanner.next()))
                        .setYear(Integer.parseInt(scanner.next()))
                        .setPainter(scanner.next())
                        .setColorsType(ColorsType.valueOf(scanner.next()))
                        .build();
            case FURNITURE:
                return new FurnitureBuilder()
                        .setId(Integer.parseInt(scanner.next()))
                        .setName(scanner.next())
                        .setMinimumPrice(Double.parseDouble(scanner.next()))
                        .setYear(Integer.parseInt(scanner.next()))
                        .setType(scanner.next())
                        .setMaterial(scanner.next())
                        .build();
            case JEWELRY:
                return new JewelryBuilder()
                        .setId(Integer.parseInt(scanner.next()))
                        .setName(scanner.next())
                        .setMinimumPrice(Double.parseDouble(scanner.next()))
                        .setYear(Integer.parseInt(scanner.next()))
                        .setMaterial(scanner.next())
                        .setGem(Boolean.parseBoolean(scanner.next()))
                        .build();
            default:
                throw new IllegalArgumentException("Product " + type + " cannot be auctioned.");
        }
    }
}
