package Parser;

import Auction.*;
import Employee.Manager;
import java.io.*;
import java.util.*;

/**
 * Pentru executia comenzilor primite se foloseste un obiect de tip parser.
 */
public class Parser {
    public void parse(Scanner scanner, AuctionHouse house) {
        Manager manager = (Manager)house.getEmployees().get(0);
        label:
        while (true) {
            String[] command = scanner.nextLine().split(" ");
            try {
                switch (command[0]) {
                    case "list":
                        new ListCommand(house, command[1]).execute();
                        break;
                    case "hire":
                        new HireCommand(manager, house, command[1]).execute();
                        break;
                    case "startactivity":
                        house.setClientsFileName(command[2]);
                        house.setProductsFileName(command[1]);
                        new StartCommand(house).execute();
                        break;
                    case "quit":
                    case "exit":
                        break label;
                    default:
                        System.out.println("Unknown commnad.");
                }
            } catch (ArrayIndexOutOfBoundsException | IOException e) {
                if (e instanceof ArrayIndexOutOfBoundsException)
                    System.out.println("Inccorect number of arguments.");
                else {
                    System.out.print(e.getMessage());
                }
            }
        }
    }
}
