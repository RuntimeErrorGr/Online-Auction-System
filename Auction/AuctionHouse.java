package Auction;
import Client.Client;
import Employee.*;
import Parser.*;
import Product.Product;
import java.io.*;
import java.util.*;

/**
 * Casa de licitatii este unica in program.
 * Implementeaza Runnable si metoda run prin intermediul careia preia clienti noi si le
 * prezinta oferta curenta de produse pentru care acestia pot licita.
 */
public class AuctionHouse implements Runnable {
    private List<Product> products;             // produse in stoc
    private List<Product> historyProducts;      // produse vandute
    private List<Client> clients;               // clientii
    private List<Auction> finishedAuctions;     // licitatii finalizate
    private List<Auction> standbyAuctions;      // licitatii care nu au atins nr. necesar participanti
    private List<Employee> employees;           // angajati (brokeri + manager)
    private String productsFileName;            // fisierul din care sunt preluate produse
    private String clientsFileName;             // fisierul din care sunt preluati clienti
    private static AuctionHouse unique;

    private AuctionHouse() {
        this.products = Collections.synchronizedList(new LinkedList<>());
        this.clients = new LinkedList<>();
        this.finishedAuctions = new LinkedList<>();
        this.standbyAuctions = new LinkedList<>();
        this.employees = new LinkedList<>();
        this.historyProducts = new LinkedList<>();
    }

    /**
     * Casa de licitatii primeste clienti.
     */
    public void run() {
        try {
            welcomeClients(clientsFileName);
        } catch (FileNotFoundException | InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Singleton constructor.
     * @return instanta unica a casei de licitatii.
     */
    public static AuctionHouse getInstance() {
        if (unique == null) {
            unique = new AuctionHouse();
        }
        return unique;
    }

    /**
     * Metoda generica pentru afisarea informatiilor din listele casei de licitatii.
     * @param list lista care se vrea a fi afisata (produse, produse istoric, clienti,
     *             licitatii, licitatii istoric, angajati).
     * @param <T> tipul de obiecte continute de lista.
     */
    public static<T> void list(List<T> list) {
        if(list.isEmpty()) {
            System.out.println("Empty list");
            return;
        }
        for(T x: list) {
            System.out.println(x.toString());
        }
    }

    /**
     * Metoda sincronizata prin intermediul careia managerul casei mentine stocul de produse
     * prin adaugarea de produse noi.
     * @param filename fisierul din care sunt incarcate informatiile produselor.
     * @throws FileNotFoundException fisierul nu a fost gasit.
     * @throws InterruptedException threadul a fost intrerupt.
     */
    public synchronized void mantainSupply(String filename) throws FileNotFoundException, InterruptedException {
        File myFile = new File(filename);
        Scanner scanner = new Scanner(myFile).useDelimiter(",");
        while(scanner.hasNextLine()) {                  // cat timp exista produse noi
            new AddProductCommand(scanner).execute();   // adauga produs
            wait(new Random().nextInt(5000));    // permite altor threaduri sa se execute
        }
        scanner.close();
    }

    /**
     * Metoda sincronizata prin intermediul careia casa de licitatii intampina clientii noi
     * si le prezinta oferta de produse.
     * @param filename fisierul din care sunt incarcate informatiile clientilor.
     * @throws FileNotFoundException fisierul nu a fost gasit.
     * @throws InterruptedException threadul a fost intrerupt.
     */
    private synchronized void welcomeClients(String filename) throws FileNotFoundException, InterruptedException {
        File myFile = new File(filename);
        Scanner scanner = new Scanner(myFile).useDelimiter(",");
        while(scanner.hasNextLine()) {                  // cat timp avem clienti noi
            new AddClientCommand(scanner).execute();    // adauga client
            wait(new Random().nextInt(5000));    // permite altor threaduri sa se execute
        }
        scanner.close();
    }

    /**
     * Metoda apelata in cadrul actualizarii licitatiilor.
     * Numarul de clienti in asteptare este incrementat.
     * Numarul de participari ale unui client este incrementat.
     * Brokerul clientului este adaugat la licitatie. In acest mod clientul va comunica cu licitatia
     * prin intermediul brokerului.
     * @param c clientul.
     * @param a licitatia.
     */
    private void updateClientAndAuction(Client c, Auction a) {
        a.setNoStandbyClients(a.getNoStandbyClients() + 1);
        a.addBroker(c.getBroker());
        c.setNoParticipation(c.getNoParticipation() + 1);
    }

    /**
     * Selecteaza un broker random din lista de angajati si il atribuie unui client nou.
     * Deoarece exista sansa sa fie selectat intamplator administratorul care nu poate reprezenta
     * un client, se incearca obtinerea unui angajat pana cand acesta este de tip broker.
     * @param client noul client.
     */
    public void associateBroker(Client client) {
        Broker randomBroker;
        while (true) {
            Employee e = employees.get(new Random().nextInt(employees.size() - 1));
            if (e instanceof Broker) {
                randomBroker = (Broker)e;
                break;
            }
        }
        client.setBroker(randomBroker);
        randomBroker.add(client);
    }

    /**
     * Pe baza optiunilor de licitatie exprimate de un client dupa consultarea produselor disponibile
     * casa de licitatii creeaza o licitatie noua pentru un anumit produs (in cazul in care nu exista
     * deja una) sau adauga clientul in asteptare intr-o licitatie (in cazul in care aceasta deja
     * exista).
     * Daca numarul tinta de participanti pentru licitatia respectiva a fost atins, atunci licitatia
     * porneste.
     * @param c clientul care vine cu oferta.
     */
    public void updateAuctions(Client c) {
        for (Map.Entry<Integer, Double> entry : c.getBid().entrySet()) {    // pentru fiecare oferta
            boolean auctionFound = false;
            for (Auction a : standbyAuctions) {                             // verifica licitatiile
                if (entry.getKey() == a.getIdProduct()) {                   // licitatie exista
                    auctionFound = true;
                    Product p = Product.getProductByID(entry.getKey());
                    assert p != null;
                    if (p.getMinimumPrice() <= entry.getValue()) {          // oferta valida
                        updateClientAndAuction(c, a);
                    }
                    if (a.getNoStandbyClients() == a.getNoParticipants()) { // licitatia incepe
                        standbyAuctions.remove(a);
                        System.out.println("Auction for " + p.getName() + " started! Good luck everyone!");
                        createAuctionThread(a);
                    }
                    break;
                }
            }
            if (!auctionFound) {                                      // nu exista licitatie activa
                for(Auction a : finishedAuctions) {
                    if (a.getIdProduct() == entry.getKey()) {         // exista licitatie incheiata
                        auctionFound = true;
                        break;
                    }
                }
                if (!auctionFound) {                                  // nu exista licitatie
                    createNewAuction(c, entry.getKey());              // creeaza licitatie
                    c.setNoParticipation(c.getNoParticipation() + 1);
                }
            }
        }
    }

    /**
     * Calculeaza oferta maxima dintr-o runda a licitatiei.
     * @param bids ofertele facute in cadrul rundei
     * @return perechea client - oferta maxima din cadrul licitatiei.
     */
    private Map.Entry<Client, Double> calculateMaximBid(Map<Client, Double> bids) {
        return Collections.max(bids.entrySet(), Comparator.comparing(Map.Entry::getValue));
    }

    /**
     * Numara ofertele valide din cadrul unei runde.
     * Daca un client s-a retras deja din licitatie, acesta va transmite la fiecare runda
     * ulterioara oferta -1 (invalida).
     * @param bids ofertele din cadrul rundei.
     * @return numarul de oferte valide.
     */
    private int numberValidBids(Map<Client, Double> bids) {
        int counter = 0;
        for(Map.Entry<Client, Double> entry: bids.entrySet()) {
            if(entry.getValue() > 0) {
                counter ++;
            }
        }
        return counter;
    }

    /**
     * In cazul in care se retrag toti participantii la licitatie, mai putin unul (singurul
     * care face o oferta valida in cadrul unei runde) atunci acesta este declarat castigator.
     * Pentru a obtine numele castigatorului in acest caz se cauta in mapa de oferte oferta cu valoare pozitiva.
     * @param bids ofertele din cadrul unei runde.
     * @return numele clientului castigator al licitatiei.
     */
    private String getWinnerName(Map<Client, Double> bids) {
        String name = "Hacked";
        for(Map.Entry<Client, Double> entry: bids.entrySet()) {
            if(entry.getValue() > 0) {
                name = entry.getKey().getName();
            }
        }
        return name;
    }

    private void printLogLastStand(Map<Client, Double> bids, int i, Product p) {
        String winnerName = getWinnerName(bids);
        System.out.println(winnerName + " is the last standing in " + p.getName() + " auction!");
        System.out.println("==== ROUND " + i + "-END ====");
        p.setSold(true);
    }

    private void printLogAllOut(int i) {
        System.out.println("Everyone withdrew!");
        System.out.println("==== ROUND " + i + "-END ====");
    }

    /**
     * In urma unei licitatii si declararea unui castigator se afiseaza un mesaj de out.
     * Este actualizat pretul de vanzare al produsului.
     * Este actualizat numarul de licitatii castigate ale castigatorului.
     * Este platit brokerul castigatorului cu comisionul corespunzator.
     * Brokerul sterge produsul vandut din lista de produse a casei si il adauga in istoric.
     * @param winner perechea castigor - oferta castigatoare.
     * @param p produsul vandut.
     */
    private void updateAfterAuction(Map.Entry<Client, Double> winner, Product p) {
        Client winnerClient = winner.getKey();
        p.setSold(true);
        System.out.println("Auction for product " + p.getName() +
                " has been won by " + winnerClient.getName() +
                ". Final selling price: " + winner.getValue() + ". Congratulations!");
        p.setSellingPrice(winner.getValue());
        winnerClient.setNoWins(winner.getKey().getNoWins() + 1);
        try {
            winnerClient.getBroker().calculateCommission(winnerClient, winner.getValue());
            winnerClient.getBroker().removeSoldProduct(p);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
        System.out.println("======================== AUCTION-END ========================");
    }

    /**
     * Cand numarul tinta de participanti este atins pentru o licitatie, aceasta este lansata
     * de casa de licitatii prin crearea unui thread anonim.
     * @param a licitatia lansata.
     */
    private void createAuctionThread(Auction a) {
        final String START = "======================== AUCTION ========================";
        final String ROUNDS = "==== ROUND ";
        final String ROUNDF = "-END ====";
        finishedAuctions.add(a);
        new Thread("Auction")  {                                 // thread anonim
            @Override
            public synchronized void run() {
                boolean sold = true;
                Product p = Product.getProductByID(a.getIdProduct());
                double currentPrice = p.getMinimumPrice();
                System.out.println(START);
                Map.Entry<Client, Double> winner = null;
                for(int i = 0; i < a.getNoMaximSteps(); i++) {         // licitatie cu n runde
                    System.out.println(ROUNDS + i + " ====");          // informeaza brokerii
                    Map<Client, Double> bids = a.inform(a.getIdProduct(), currentPrice);
                    if (bids.size() == 0) {                            // toati clientii
                        printLogAllOut(i);                             // s-au retras
                        sold = false;
                        break;
                    }
                    if (numberValidBids(bids) == 1) {                  // a ramas
                        printLogLastStand(bids, i, p);                 // un singur client
                        break;
                    }
                    winner = calculateMaximBid(bids);                  // castigator runda
                    if (winner == null) {
                        winner = bids.entrySet().iterator().next();
                    }
                    currentPrice = winner.getValue();                 // oferta castigatoare
                    String winnerName =  winner.getKey().getName();   // nume castigator
                    System.out.println("Biggest round bid: " + currentPrice + " by: " + winnerName);
                    System.out.println(ROUNDS + i + ROUNDF);
                }
                if(sold && winner != null) {
                    updateAfterAuction(winner, p);                    // actualizeaza castigatorul
                }
            }
        }.start();
    }

    /**
     * Este instantiata un nou obiect de tip licitatie si adaugat implicit in lista de licitatii
     * in asteptare a casei.
     * Brokerul ce reprezinta clientul ce a initiat cererea pentru pornirea licitatiei este
     * adaugat la licitatie. Clientul va comunica cu licitatia de acum inainte prin intermediul
     * brokerului.
     * @param c clientul care a solicitat licitatia.
     * @param id id-ul produsului solicitat.
     */
    private void createNewAuction(Client c, int id) {
        Random rand = new Random();
        Auction auction = new AuctionBuilder()
                .setId(standbyAuctions.size() + finishedAuctions.size() + 1)
                .setIdProduct(id)
                .setNoMaximSteps(rand.nextInt(10 - 2) + 2)
                .setNoParticipants(rand.nextInt(10) % clients.size() + 2)
                .setNoStandbyClients(1)
                .build();
        auction.addBroker(c.getBroker());
        standbyAuctions.add(auction);
    }

    public void add(Employee employee) {
        employees.add(employee);
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void addClient(Client client) {
        clients.add(client);
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Client> getClients() {
        return clients;
    }

    public List<Auction> getFinishedAuctions() {
        return finishedAuctions;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public void setFinishedAuctions(List<Auction> finishedAuctions) {
        this.finishedAuctions = finishedAuctions;
    }

    public List<Auction> getStandbyAuctions() {
        return standbyAuctions;
    }

    public void setStandbyAuctions(List<Auction> standbyAuctions) {
        this.standbyAuctions = standbyAuctions;
    }

    public String getProductsFileName() {
        return productsFileName;
    }

    public String getClientsFileName() {
        return clientsFileName;
    }

    public void setProductsFileName(String productsFileName) {
        this.productsFileName = productsFileName;
    }

    public void setClientsFileName(String clientsFileName) {
        this.clientsFileName = clientsFileName;
    }

    public List<Product> getHistoryProducts() {
        return historyProducts;
    }

    public void addHistory(Product p) {
        historyProducts.add(p);
    }
}
