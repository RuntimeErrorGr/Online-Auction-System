package Parser;
import Auction.*;
import Client.*;
import Product.Product;

import java.util.Map;
import java.util.Scanner;

public class AddClientCommand implements Command {
    private Scanner scanner;

    public AddClientCommand(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Incarca datele unui client.
     * @return client.
     */
    private Client readClient(Scanner scanner) {
        ClientFactory factory = ClientFactory.getInstance();
        ClientType type = ClientType.valueOf(scanner.next());
        return factory.craft(type, scanner);
    }

    private void printMessage(Client client, Map<Integer, Double> desiredProducts) {
        System.out.println("Welcome, " + client.getName() + "!");
        System.out.println("Products you are interested in: ");
        for(Map.Entry<Integer, Double> entry : desiredProducts.entrySet()) {
            Product p = Product.getProductByID(entry.getKey());
            assert p != null;
            System.out.println(p.getName() + " Maximum_bid: " + entry.getValue() + " ");
        }
        System.out.println("==========================================================");
    }

    /**
     * Adauga un client in casa de licitatii daca id-ul acestuia nu este duplicat.
     * @param client clientul care trebuie adaugat.
     * @param auctionHouse casa de licitatii la care se adauga clientul.
     */
    private void addClient(Client client, AuctionHouse auctionHouse) {
        boolean found = false;
        for(Client c: auctionHouse.getClients()) {
            if(c.getName().equals(client.getName())) {
                found = true;
                break;
            }
        }
        if(!found) {
            auctionHouse.addClient(client);             // adauga clientul casei de licitatii
            client.makeOffer(auctionHouse);             // consulta produsele si fa oferte
            auctionHouse.associateBroker(client);       // casa asociaza un broker clientului
            printMessage(client, client.getBid());
            auctionHouse.updateAuctions(client);        // licitatiile sunt actualizate
        }
        else {
            System.out.println("Duplicate client ID!"); // un client cu acelasi ID deja exista
        }
    }

    public void execute() {
        AuctionHouse auctionHouse = AuctionHouse.getInstance();
        Client client = readClient(scanner);            // citeste client
        addClient(client, auctionHouse);                // adauga client
    }
}
