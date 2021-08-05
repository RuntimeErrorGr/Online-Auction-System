package Parser;
import Auction.AuctionHouse;
import Product.Product;
import Product.ProductFactory;
import Product.ProductType;
import java.util.Scanner;

public class AddProductCommand implements Command {
    private Scanner scanner;

    public AddProductCommand(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Incarca datele unui produs.
     * @return produs.
     */
    private Product readProduct(Scanner scanner) {
        ProductFactory factory = ProductFactory.getInstance();
        ProductType type = ProductType.valueOf(scanner.next());
        return factory.craft(type, scanner);
    }

    /**
     * Verifica daca produsul exista in lista de produse in stock sau produsele deja vandute.
     * Daca nu, produsul este adaugat.
     */
    public void execute() {
        Product p = readProduct(scanner);                               // citeste produsul
        boolean found = false;
        for(Product i : AuctionHouse.getInstance().getProducts()) {     // cauta in produse in stoc
            if(i.getId() == p.getId()) {
                found = true;                                           // a fost gasit
                System.out.println("Duplicate product ID!");
                break;
            }
        }
        for(Product i : AuctionHouse.getInstance().getHistoryProducts()) {  // cauta in istoric
            if(i.getId() == p.getId()) {
                found = true;                                               // a fost gasit
                System.out.println("Duplicate product ID!");
                break;
            }
        }
        if(!found) {                                                        // nu a fost gasit
            AuctionHouse.getInstance().addProduct(p);
            System.out.println("Product " + p.getName() + " has been added.");
            System.out.println("==========================================================");
        }
    }
}
