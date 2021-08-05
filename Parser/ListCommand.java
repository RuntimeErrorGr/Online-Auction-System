package Parser;
import Auction.AuctionHouse;

/**
 * O instanta a clasei este creata la intalnirea comenzii "list".
 */
public class ListCommand implements Command {
    private AuctionHouse house;
    private String command;

    ListCommand(AuctionHouse house, String command) {
        this.house = house;
        this.command = command;
    }

    public void execute() {
        switch (command) {
            case "employees":
                AuctionHouse.list(house.getEmployees());
                break;
            case "products":
                System.out.println("IN STOCK: ");
                AuctionHouse.list(house.getProducts());
                System.out.println("SOLD: ");
                AuctionHouse.list(house.getHistoryProducts());
                break;
            case "auctions":
                System.out.println("STANDBY/ONGOING AUCTIONS: ");
                AuctionHouse.list(house.getStandbyAuctions());
                System.out.println("FINISHED AUCTIONS: ");
                AuctionHouse.list(house.getFinishedAuctions());
                break;
            case "clients":
                AuctionHouse.list(house.getClients());
                break;
            default:
                System.out.println("Cannot list " + command);
        }
    }
}
