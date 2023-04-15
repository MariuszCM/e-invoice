package pl.project.e_invoice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Simulation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ElementCollection
    private List<HistoryEvent> history = new ArrayList<HistoryEvent>();
    @OneToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;
}
