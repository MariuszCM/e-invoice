package pl.project.e_invoice.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@EqualsAndHashCode()
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Company {
    @Id
    private String nip;
    private String companyName;
    private String address;
}
