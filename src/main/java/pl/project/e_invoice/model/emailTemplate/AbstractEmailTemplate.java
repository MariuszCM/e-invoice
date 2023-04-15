package pl.project.e_invoice.model.emailTemplate;

import lombok.Data;

@Data
public abstract class AbstractEmailTemplate {
    private Long inVoiceId;
}
