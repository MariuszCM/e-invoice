package pl.project.e_invoice.model.invoice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
public enum InvoiceStatus {
    NOT_PAID("NASD001", "Nieopłacona"),
    PAID("MCMA001", "Opłacona");

    private final String emailTemplateCode;
    private final String label;
}
