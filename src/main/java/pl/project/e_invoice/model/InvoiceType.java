package pl.project.e_invoice.model;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public enum InvoiceType {
    INCOME("WCAX001"),
    OUTCOME("LOAS001");

    private final String emailTemplateCode;
}
