package pl.project.e_invoice.model.emailTemplate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailPaidTemplate extends AbstractEmailTemplate{
    private boolean paymentConfirmation;
}
