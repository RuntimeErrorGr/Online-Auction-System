package Client;
import Auction.AuctionHouse;
import Employee.Broker;
import Product.Product;

import java.util.*;

import Observer.*;

/**
 * Instantele clasei client au rol de observer in relatia broker-clienti.
 */
public abstract class Client implements IObserver<Double> {
    private int id;
    private String name;
    private String address;
    private int noParticipation;
    private int noWins;
    private Broker broker;
    private Map<Integer, Double> maximumBids;

    Client() { }

    Client(int id, String name, String address) {
        this.name = name;
        this.address = address;
        this.noParticipation = 0;
        this.noWins = 0;
        this.id = id;
        this.maximumBids = new HashMap<>();
    }

    @Override
    public String toString() {
       StringBuilder sb = new StringBuilder()
               .append(id)
               .append(" ")
               .append(name)
               .append(" ADDRESS:")
               .append(address)
               .append(" P/W RATIO:")
               .append(noParticipation)
               .append("/")
               .append(noWins);
       if(broker != null) {
           sb.append(" BROKER:").append(broker.getName());
       }
       return sb.toString();
    }

    /**
     * Metoda implementata din IObserver.
     * Cand o noua runda a unei licitatii incepe, clientii ai carui broker e inscris la licitatie
     * sunt notificati pentru a transmite oferta lor pentru runda ce urmeaza sa inceapa.
     * @param id id-ul produsului pentru care se liciteaza.
     * @param currentPrice pretul curent maxim.
     * @return oferta.
     */
    public Double update(Integer id, Double currentPrice) {
        double maximumBid = maximumBids.get(id);
        double currentBid;
        if (currentPrice == maximumBid) {   // verifica daca pretul curent este egal cu oferta maxima
            currentBid = maximumBid;        // stabilita la inscrierea in licitatie
        }
        else if (currentPrice < maximumBid) {                               // inca se poate licita
            currentBid = currentPrice + new Random().nextInt(1000);
            if (currentBid > maximumBid) {                                  // plafoneaza
                currentBid = maximumBid;
            }
        }
        else {                                                              // nu se mai poate licita
            currentBid = -1;                                                // clientul se retrage
        }
        if (currentBid != -1) {                                             // clientul a licitat
            String productName = Product.getProductByID(id).getName();
            System.out.println("Customer " + name + " bid " + currentBid + " for " + productName);
        }
        return currentBid;
    }

    /**
     * In momentul in care clientul contacteaza casa de licitatii, aceasta ii pune la dispozitie
     * lista de produse in stock, care nu fac obiectul unei licitatii in curs de desfasurate.
     * Clientul alege un numar aleator de produse pentru care va licita si apoi alege produsele.
     * @param house casa de licitatii contactata.
     * @return produsele pentru care se doreste sa se liciteze.
     */
    private Set<Product> getDesiredProducts(AuctionHouse house) {
        Set<Product> products = new HashSet<>();
        if(!house.getProducts().isEmpty()) {
            int noAuctions = new Random().nextInt(house.getProducts().size());  //total participari
            for (int i = 0; i <= noAuctions; i++) {
                int randomIndex = new Random().nextInt(house.getProducts().size());
                Product p = house.getProducts().get(randomIndex);
                if (!p.isActiveAuction()) {
                    products.add(p);
                }
            }
        }
        return products;
    }

    /**
     * Pentru fiecare produs ales clientul stabileste o suma maxima pe care este dispus sa o plateasca.
     * Daca licitatia depaseste pretul maxim, clientul se va retrage.
     * Suma maxima este calculata aleator raportata la pretul minim al produslui si la puterea
     * financiara a clientului (am presupun ca pesoanele juridice au o putere de cumparare mai
     * mare decat persoanele fizice).
     * @param house casa de licitatii contactata.
     */
    public void makeOffer(AuctionHouse house) {
        Set<Product> products = getDesiredProducts(house);
        if(!products.isEmpty()) {
            Map<Integer, Double> bids = new HashMap<>();
            for (Product p : products) {
                double maximOfferPrice = p.getMinimumPrice();
                if (this instanceof Individual) {
                    maximOfferPrice += p.getMinimumPrice() * 0.025 + new Random().nextInt(1000);
                }
                if (this instanceof JuridicalPerson) {
                    maximOfferPrice +=  p.getMinimumPrice() * 0.075 + new Random().nextInt(1000);
                }
                bids.put(p.getId(), maximOfferPrice);
            }
            this.maximumBids = bids;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public int getNoParticipation() {
        return noParticipation;
    }

    public int getNoWins() {
        return noWins;
    }

    public Broker getBroker() {
        return broker;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNoParticipation(int noParticipation) {
        this.noParticipation = noParticipation;
    }

    public void setNoWins(int noWins) {
        this.noWins = noWins;
    }

    public Map<Integer, Double> getBid() {
        return maximumBids;
    }

    public void setBid(Map<Integer, Double> bid) {
        this.maximumBids = bid;
    }

}
