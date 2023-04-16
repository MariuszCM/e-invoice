package pl.project.e_invoice.model.invoice;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public enum InvoiceStatus {
    NOT_PAID("NASD001"),
    PAID("MCMA001");

    private final String emailTemplateCode;
}
