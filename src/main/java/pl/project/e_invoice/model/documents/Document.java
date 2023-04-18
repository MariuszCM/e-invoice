package pl.project.e_invoice.model.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.project.e_invoice.model.Company;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Document {
    @Id
    private String id;
    private LocalDateTime dateOfIssue;
    private BigDecimal value;
    private String name;
    @OneToOne
    @JoinColumn(name = "seller_id")
    private Company seller;
    @OneToOne
    @JoinColumn(name = "buyer_id")
    private Company buyer;

}
