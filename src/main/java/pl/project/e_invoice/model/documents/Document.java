package pl.project.e_invoice.model.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    public static <T> BigDecimal convertToBigDecimal(T value) {
        if (value instanceof String) {
            return new BigDecimal((String) value);
        } else {
            throw new IllegalArgumentException("Value is not a String");
        }
    }

}
