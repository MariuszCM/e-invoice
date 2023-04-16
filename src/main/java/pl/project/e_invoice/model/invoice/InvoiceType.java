package pl.project.e_invoice.model.invoice;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public enum InvoiceType {
    INCOME("WCAX001", "Przychodząca"),
    OUTCOME("LOAS001", "Wychodząca");

    private final String emailTemplateCode;
    private final String label;
}
