package Auction;
import Client.Client;
import Employee.Broker;
import Observer.IObservable;
import java.util.*;

/**
 * Licitatia - clasa se instantiaza in momentul in care un client isi exprima dorinta
 * de a lua parte la licitatia unui anumit produs.
 */
public class Auction implements IObservable<Broker, Map<Client, Double>> {
    private int id;
    private int noParticipants;
    private int noStandbyClients;
    private int idProduct;
    private int noMaximSteps;
    private Set<Broker> brokers;
    private boolean finished;

    public Auction() { }

    public Auction(int id, int noParticipants, int idProduct, int noMaximSteps) {
        this.id = id;
        this.noParticipants = noParticipants;
        this.idProduct = idProduct;
        this.noMaximSteps = noMaximSteps;
        this.brokers = new HashSet<>();
    }

    @Override
    public String toString() {
        return id +
                " Registered customers:" +
                noParticipants +
                " Standby Customers:" +
                noStandbyClients +
                " Product ID:" +
                idProduct +
                " Maxim steps:" +
                noMaximSteps +
                " Finished:" +
                finished;
    }

    /**
     * Metoda implementata din interfata IObservable.
     * Inscrie un broker (observator) la licitatie.
     * @param broker brokerul inscris.
     */
    public void add(Broker broker) {
        brokers.add(broker);
    }

    /**
     * Metoda implementata din interfata IObservable.
     * Retrage un broker din licitatie.
     * @param broker retras.
     */
    public void remove(Broker broker) {
        brokers.remove(broker);
    }

    /**
     * Metoda implementata din interfata IObservable.
     * Informeaza toti brokerii inscrisi ca o noua runda a licitatiei a inceput.
     * Colecteaza ofertele clientilor fiecarui broker si creeaza o mapa mare cu toate ofertele
     * din runda curenta.
     * @param id id-ul produsulului licitat.
     * @param price pretul maxim din runda anterioara.
     * @return ofertele clientilor reprezentati de brokeri pentru runda curenta.
     */
    public Map<Client, Double> inform(Integer id, Double price) {
        finished = true;
        Map<Client, Double> clientsBids = new HashMap<>();
        for(Broker b : brokers) {
            for (Map.Entry<Client, Double> entry : b.update(id, price).entrySet()) {
                clientsBids.put(entry.getKey(), entry.getValue());
            }
        }
        return clientsBids;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNoParticipants(int noParticipants) {
        this.noParticipants = noParticipants;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public void setNoMaximSteps(int noMaximSteps) {
        this.noMaximSteps = noMaximSteps;
    }

    public int getId() {
        return id;
    }

    public int getNoParticipants() {
        return noParticipants;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public int getNoMaximSteps() {
        return noMaximSteps;
    }

    public int getNoStandbyClients() {
        return noStandbyClients;
    }

    public void setNoStandbyClients(int noStandbyClients) {
        this.noStandbyClients = noStandbyClients;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Set<Broker> getBrokers() {
        return brokers;
    }

    public void setBrokers(Set<Broker> brokers) {
        this.brokers = brokers;
    }

    public void addBroker(Broker b) {
        brokers.add(b);
    }
}
