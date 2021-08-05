
import Auction.AuctionHouse;
import Employee.Manager;
import Parser.Parser;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AuctionHouse house = AuctionHouse.getInstance();
        Manager manager = Manager.getInstance(house);
        house.add(manager);
        Scanner scanner = new Scanner(System.in);
        new Parser().parse(scanner, house);
    }
}
