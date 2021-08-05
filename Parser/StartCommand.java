package Parser;

import Auction.AuctionHouse;
import Employee.*;
import java.util.concurrent.*;

/**
 * O instanta a clasei este creata in parser cu scopul de a deschide threadurile de adaugare
 * clienti si adaugare produse.
 */
public class StartCommand implements Command {
    private AuctionHouse house;

    StartCommand(AuctionHouse house) {
        this.house = house;
    }

    public void execute() {
        if(house.getEmployees().size() == 1) {
            System.out.println("Please hire some brokers first!");
            return;
        }
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Manager manager = (Manager)house.getEmployees().get(0);
        if(!house.getProductsFileName().equals("-"))    // a fost introdus un fisier de produse
            executor.execute(manager);
        if(!house.getClientsFileName().equals("-"))     // a fost introdus un fisier de clienti
            executor.execute(house);
        executor.shutdown();
    }
}
