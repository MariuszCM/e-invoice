package pl.project.e_invoice.model.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

@AllArgsConstructor
@Getter
public enum InvoiceType {
    INCOME("WCAX001", "Przychodząca"),
    OUTCOME("LOAS001", "Wychodząca");

    private final String emailTemplateCode;
    private final String label;

    public static Function<String, InvoiceType> convertLabelTOEnum() {
        return statusString -> {
            for (InvoiceType status : InvoiceType.values()) {
                if (status.getLabel().equalsIgnoreCase(statusString)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Illegal invoice type: " + statusString);
        };
    }

    @Override
    public String toString() {
        return "InvoiceType{" +
                "emailTemplateCode='" + emailTemplateCode + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
