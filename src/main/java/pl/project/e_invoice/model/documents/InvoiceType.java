package pl.project.e_invoice.model.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public enum InvoiceType {
    INCOME("WCAX001", "Przychodząca"),
    OUTCOME("LOAS001", "Wychodząca");

    private final String emailTemplateCode;
    private final String label;
}
