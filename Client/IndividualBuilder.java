package Client;

public class IndividualBuilder {
    private String birthDay;
    private int id;
    private String name;
    private String address;

    public IndividualBuilder setBirthDay(String birthDay) {
        this.birthDay = birthDay;
        return this;
    }

    public IndividualBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public IndividualBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public IndividualBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public Individual build() {
        return new Individual(id, name, address, birthDay);
    }
}