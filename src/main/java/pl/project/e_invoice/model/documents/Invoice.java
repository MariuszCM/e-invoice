package pl.project.e_invoice.model.documents;

import lombok.*;
import lombok.experimental.SuperBuilder;
import pl.project.e_invoice.model.Simulation;
import pl.project.e_invoice.model.listeners.InvoiceDatabaseListener;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(InvoiceDatabaseListener.class)
public class Invoice extends Document{

    private InvoiceType invoiceType;
    private InvoiceStatus invoiceStatus;

    @OneToOne(mappedBy = "document", cascade = CascadeType.ALL, orphanRemoval = true)
    private Simulation simulation;

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceType=" + invoiceType +
                ", invoiceStatus=" + invoiceStatus +
                '}';
    }
}
