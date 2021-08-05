package Client;
import java.util.*;

public class ClientFactory {
    private static ClientFactory unique;

    private ClientFactory() {}

    public static ClientFactory getInstance() {
        if (unique == null) {
            unique = new ClientFactory();
        }
        return unique;
    }

    public Client craft(ClientType type, Scanner scanner) {
        switch (type) {
            case INDIVIDUAL:
                return new IndividualBuilder()
                        .setId(Integer.parseInt(scanner.next()))
                        .setName(scanner.next())
                        .setAddress(scanner.next())
                        .setBirthDay(scanner.next())
                        .build();
            case JURIDICAL:
                return new JuridicalPersonBuilder()
                        .setId(Integer.parseInt(scanner.next()))
                        .setName(scanner.next())
                        .setAddress(scanner.next())
                        .setCompanyType(CompanyType.valueOf(scanner.next()))
                        .setSocialCapital(Double.parseDouble(scanner.next()))
                        .build();
            default:
                throw new IllegalArgumentException("Client " + type + " cannot be created.");
        }
    }

}
