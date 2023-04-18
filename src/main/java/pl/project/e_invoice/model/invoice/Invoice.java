package pl.project.e_invoice.model.invoice;

import lombok.*;
import pl.project.e_invoice.model.Company;
import pl.project.e_invoice.model.Simulation;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode()
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Invoice {
    @Id
    private String id;
    private InvoiceType invoiceType;
    private LocalDateTime dateOfIssue;
    private BigDecimal value;
    private String name;
    private InvoiceStatus invoiceStatus;
    @OneToOne
    @JoinColumn(name = "seller_id")
    private Company seller;
    @OneToOne
    @JoinColumn(name = "buyer_id")
    private Company buyer;
    @OneToOne(mappedBy = "invoice")
    private Simulation simulation;

}
