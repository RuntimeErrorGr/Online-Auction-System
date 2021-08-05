package Client;

public class JuridicalPersonBuilder {
    private CompanyType companyType;
    private double socialCapital;
    private int id;
    private String name;
    private String address;

    public JuridicalPersonBuilder setCompanyType(CompanyType companyType) {
        this.companyType = companyType;
        return this;
    }

    public JuridicalPersonBuilder setSocialCapital(double socialCapital) {
        this.socialCapital = socialCapital;
        return this;
    }

    public JuridicalPersonBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public JuridicalPersonBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public JuridicalPersonBuilder setAddress(String address) {
        this.address = address;
        return this;
    }


    public JuridicalPerson build() {
        return new JuridicalPerson(id, name, address, companyType, socialCapital);
    }
}