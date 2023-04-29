package pl.project.e_invoice.model.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

@AllArgsConstructor
@Getter
public enum InvoiceStatus {
    NOT_PAID("NASD001", "Nieopłacona"),
    PAID("MCMA001", "Opłacona");

    private final String emailTemplateCode;
    private final String label;

    public static Function<String, InvoiceStatus> convertLabelTOEnum() {
        return statusString -> {
            for (InvoiceStatus status : InvoiceStatus.values()) {
                if (status.getLabel().equalsIgnoreCase(statusString)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Illegal invoice status: " + statusString);
        };
    }

    @Override
    public String toString() {
        return "InvoiceStatus{" +
                "emailTemplateCode='" + emailTemplateCode + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
