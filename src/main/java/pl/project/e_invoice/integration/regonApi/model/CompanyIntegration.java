package pl.project.e_invoice.integration.regonApi.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;

@Data
public class CompanyIntegration {
    private String nip;
    private String regon;
    private String companyName;
    private String street;
    private String city;
    private String email;

    @XmlElement(name = "Regon")
    public void setRegon(String regon) {
        this.regon = regon;
    }

    @XmlElement(name = "Nazwa")
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @XmlElement(name = "Miejscowosc")
    public void setCity(String city) {
        this.city = city;
    }

    @XmlElement(name = "Ulica")
    public void setStreet(String street) {
        this.street = street;
    }

    @XmlElement(name = "praw_adresEmail")
    public void setEmail(String email) {
        this.email = email;
    }
}
