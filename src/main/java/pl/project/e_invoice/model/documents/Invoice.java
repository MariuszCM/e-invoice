package pl.project.e_invoice.model.documents;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.project.e_invoice.model.Simulation;

import javax.persistence.*;

@Data
@EqualsAndHashCode()
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Invoice extends Document{

    private InvoiceType invoiceType;
    private InvoiceStatus invoiceStatus;

    @OneToOne(mappedBy = "document")
    private Simulation simulation;

}
