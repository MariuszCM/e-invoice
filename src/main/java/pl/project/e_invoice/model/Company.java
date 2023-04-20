package pl.project.e_invoice.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@EqualsAndHashCode()
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Company {
    @Id
    private String nip;
    private String companyName;
    private String address;
    private String email;

    @Override
    public String toString() {
        return "Company{" +
                "nip='" + nip + '\'' +
                ", companyName='" + companyName + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
