package Auction;

public class AuctionBuilder {
    private int id;
    private int noParticipants;
    private int idProduct;
    private int noMaximSteps;
    private int noStandbyClients;

    public AuctionBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public AuctionBuilder setNoParticipants(int noParticipants) {
        this.noParticipants = noParticipants;
        return this;
    }

    public AuctionBuilder setIdProduct(int idProduct) {
        this.idProduct = idProduct;
        return this;
    }

    public AuctionBuilder setNoMaximSteps(int noMaximSteps) {
        this.noMaximSteps = noMaximSteps;
        return this;
    }

    public AuctionBuilder setNoStandbyClients(int noStandbyClients) {
        this.noStandbyClients = noStandbyClients;
        return this;
    }

    public Auction build() {
        return new Auction(id, noParticipants, idProduct, noMaximSteps);
    }
}