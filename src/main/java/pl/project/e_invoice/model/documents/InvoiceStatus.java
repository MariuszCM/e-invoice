package pl.project.e_invoice.model.documents;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@ToString
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
}
