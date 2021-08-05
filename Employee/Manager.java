package Employee;

import Auction.*;
import java.io.*;
import java.util.*;

public class Manager extends Employee implements Runnable {
    private static Manager unique;
    private AuctionHouse house;

    private Manager(String name, AuctionHouse house) {
        super(name);
        this.house = house;
    }

    /**
     * Singleton constructor.
     * @param house casa de licitatii pe care o administreaza.
     * @return instanta unica a managerului.
     */
    public static Manager getInstance(AuctionHouse house) {
        if (unique == null) {
            unique = new Manager("$$$ Andrei $$$", house);
        }
        return unique;
    }

    /**
     * Inainte de a incepe activitatea, casa de licitatii angajeaza brokeri.
     * @param house casa pentru care vor lucra brokerii.
     * @param filename fisierul din care sunt incarcate datele brokerilor.
     * @throws FileNotFoundException fisierul nu a fost gasit.
     */
    public void hireBrokers(AuctionHouse house, String filename) throws FileNotFoundException {
        File myFile = new File(filename);
        Scanner scanner = new Scanner(myFile).useDelimiter(",");
        while(scanner.hasNextLine()) {
            house.add(new Broker(scanner.nextLine()));
        }
        scanner.close();
    }

    /**
     * Managerul adauga produse noi in casa de licitatii pe un thread.
     */
    public void run() {
        try {
            house.mantainSupply(house.getProductsFileName());
        } catch (FileNotFoundException | InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
