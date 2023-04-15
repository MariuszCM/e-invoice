package pl.project.e_invoice.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode()
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateOfIssue;
    private BigDecimal value;
    private String name;
    private InvoiceStatus invoiceStatus;
    @OneToOne
    @JoinColumn(name = "seller_id")
    private Company seller;
    @OneToOne
    @JoinColumn(name = "buyer_id")
    private Company buyer;

}
