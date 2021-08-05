package Parser;

import Auction.AuctionHouse;
import Employee.Manager;
import java.io.FileNotFoundException;

/**
 * O instanta a clasei este creata la intalnirea comenzii "hire".
 * Pentru inceperea activitatii de licitatii, casa de licitatii are nevoie sa angajeze prima data
 * brokeri.
 */
public class HireCommand implements Command {
    private Manager manager;
    private AuctionHouse house;
    private String filename;

    HireCommand(Manager manager, AuctionHouse house, String filename) {
        this.manager = manager;
        this.house = house;
        this.filename = filename;
    }

    /**
     * Angajeaza brokeri.
     * @throws FileNotFoundException fisierul nu a fost gasit.
     */
    public void execute() throws FileNotFoundException {
        manager.hireBrokers(house, filename);
    }
}
