package pl.project.e_invoice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email {
    private String templateCode;
    private byte[] template;
    private String destinationAddress;
}
