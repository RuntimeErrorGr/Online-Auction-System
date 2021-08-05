package Employee;
import Auction.AuctionHouse;
import Client.*;

import java.util.*;

import Observer.*;
import Product.Product;

public class Broker extends Employee implements IObservable<Client, Map<Client, Double>>,
        IObserver<Map<Client, Double>> {
    private List<Client> clients;
    private double totalEarnings;

    Broker(String name) {
        super(name);
        this.clients = new LinkedList<>();
    }

    public Broker(List<Client> clients, double total) {
        this.clients = clients;
        this.totalEarnings = total;
    }

    public Broker(String nume, List<Client> clients) {
        super(nume);
        this.clients = clients;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString())
                .append(" Total earnings: ")
                .append(totalEarnings)
                .append(" Clients: ");
        if (!clients.isEmpty()) {
            for(Client c: clients) {
                sb.append(c.getId())
                        .append("-")
                        .append(c.getName())
                        .append(", ");
            }
        }
        return sb.toString();
    }

    /**
     * Metoda implementata din interfata IObservable.
     * Pune un client (observer) in legatura cu brokerul.
     * @param c noul client.
     */
    public void add(Client c) {
        clients.add(c);
    }

    /**
     * Metoda implementata din interfata IObservable.
     * Sterge un client (observer) din lista brokerului.
     * @param c clientul sters.
     */
    public void remove(Client c) {
        clients.remove(c);
    }

    /**
     * Metoda implementata din interfata IObservable.
     * Brokerul isi informeaza clientii ca o noua runda dintr-o licitatie a inceput si le cere
     * sa ii trimita ofertele lor pe baza pretului maxim din runda care tocmai s-a incheiat anterior
     * (sau pe paza pretului de start in caz ca este prima runda din licitatie).
     * @param id id-ului produsului care se liciteaza.
     * @param price ultimul pret maxim al produsului.
     * @return ofertele tuturor clientilor
     */
    public Map<Client, Double> inform(Integer id, Double price) {
        Map<Client, Double> clientsBids = new HashMap<>();
        for (Client c : clients) {                                      // pentru fiecare client
            Map<Integer, Double> offer = c.getBid();                    // verific daca vrea sa
            for(Map.Entry<Integer, Double> entry : offer.entrySet()) {  // liciteze pentru produs
                if(entry.getKey() == id.intValue()) {                   // da
                    clientsBids.put(c, c.update(id, price));            // liciteaza
                    break;
                }
            }
        }
        for(Map.Entry<Client, Double> entry : clientsBids.entrySet()) {
            if(entry.getValue() == -1) {                                // oferte invalide
                System.out.println("Customer " + entry.getKey().getName() + " withdrew from " +
                        Product.getProductByID(id).getName() + " auction");
            }
        }
        return clientsBids;
    }

    /**
     * Metoda implementata din interfata IObserver.
     * Brokerul a fost notificat ca o noua runda a licitatiei a inceput, asa ca isi anunta
     * mai departe clientii.
     * @param id id-ul produsului care se liciteaza.
     * @param price ultimul pret maxim al produsului.
     * @return ofertele tuturor clientilor.
     */
    public Map<Client, Double> update(Integer id, Double price) {
        return inform(id, price);
    }

    /**
     * Atunci cand licitatia se incheie si produsul a fost vandut, brokerul clientului castigator
     * sterge produsul din lista de produse disponibile a casei de licitatii si il adauga in
     * lista de produse vandute. Acest lucru se intampla pe threadul licitatiei.
     * @param p produsul vandut.
     * @throws InterruptedException threadul a fost intrerupt.
     */
    public synchronized void removeSoldProduct(Product p) throws InterruptedException {
        while (AuctionHouse.getInstance().getProducts().isEmpty())
            wait(new Random().nextInt(15000));
        if (p.isSold()) {
            AuctionHouse.getInstance().addHistory(p);
            AuctionHouse.getInstance().getProducts().remove(p);
            System.out.println("==========================================================");
            System.out.println("Product " + p.getName() + " has been removed by " + this.getName());
        }
    }

    /**
     * Colaborarea dintre un client si un broker se incheie.
     * @param client clientul cu care se incheie colaborarea.
     */
    public void endCollaboration(Client client) {
        clients.remove(client);
        client.setBroker(null);
    }

    /**
     * Pe baza istoricului si tipului unui client, brokerul calculeaza comisionul pe care il va
     * aplica pentru o tranzactie incheiata cu succes.
     * @param c clientul taxat.
     * @param value valoarea tranzactionata.
     */
    public void calculateCommission(Client c, double value) {
        double comission = 0;
        String algType = "";
        if (c instanceof Individual) {
            if (c.getNoParticipation() <= 5) {
                comission += 0.2 * value;
                algType = "C1-type";
            }
            else {
                comission += 0.15 * value;
                algType = "C2-type";
            }
        }
        else if (c instanceof JuridicalPerson) {
            if (c.getNoParticipation() <= 25) {
                comission += 0.25 * value;
                algType = "C3-type";
            }
            else {
                comission += 0.1 * value;
                algType = "C4-type";
            }
        }
        System.out.println(this.getName() + " charged a " + algType + " comisson: " + comission);
        totalEarnings += comission;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public double getTotalEarnings() {
        return totalEarnings;
    }

    public void setTotalEarnings(double totalEarnings) {
        this.totalEarnings = totalEarnings;
    }
}
