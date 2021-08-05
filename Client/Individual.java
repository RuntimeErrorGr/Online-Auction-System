package Client;

public class Individual extends Client {
    private String birthDay;

    public Individual() {
        super();
    }

    public Individual(String birthDay) {
        this.birthDay = birthDay;
    }

    public Individual(int id, String name, String address, String birthDay) {
        super(id, name, address);
        this.birthDay = birthDay;
    }

    @Override
    public String toString() {
        return super.toString() + " BIRTHDAY:" + birthDay;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

}
