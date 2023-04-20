package pl.project.e_invoice.model.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import pl.project.e_invoice.model.Company;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Document {
    @Id
    private String id;
    private LocalDate dateOfIssue;
    private BigDecimal value;
    private String name;
    @OneToOne
    @JoinColumn(name = "seller_id")
    private Company seller;
    @OneToOne
    @JoinColumn(name = "buyer_id")
    private Company buyer;

    @Override
    public String toString() {
        return "Document{" +
                "id='" + id + '\'' +
                ", dateOfIssue=" + dateOfIssue +
                ", value=" + value +
                ", name='" + name + '\'' +
                ", seller=" + seller +
                ", buyer=" + buyer +
                '}';
    }
}
