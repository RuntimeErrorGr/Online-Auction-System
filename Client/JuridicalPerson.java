package Client;

public class JuridicalPerson extends Client {
    private CompanyType companyType;
    private double socialCapital;

    public JuridicalPerson() {
        super();
    }

    public JuridicalPerson(CompanyType companyType, double socialCapital) {
        this.companyType = companyType;
        this.socialCapital = socialCapital;
    }

    JuridicalPerson(int id, String name, String address, CompanyType companyType, double socialCapital) {
        super(id, name, address);
        this.companyType = companyType;
        this.socialCapital = socialCapital;
    }

    @Override
    public String toString() {
        return super.toString() + " TYPE:" + companyType + " SOCIAL_CAP:" + socialCapital;
    }

    public CompanyType getCompanyType() {
        return companyType;
    }

    public double getSocialCapital() {
        return socialCapital;
    }

    public void setCompanyType(CompanyType companyType) {
        this.companyType = companyType;
    }

    public void setSocialCapital(double socialCapital) {
        this.socialCapital = socialCapital;
    }
}
