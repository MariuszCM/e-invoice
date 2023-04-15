package pl.project.e_invoice.model.emailTemplate;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class EmailIssuedTemplate extends AbstractEmailTemplate {
    private LocalDate paymentDeadline;
}
