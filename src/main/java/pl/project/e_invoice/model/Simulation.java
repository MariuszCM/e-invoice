package pl.project.e_invoice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.project.e_invoice.model.documents.Document;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Simulation {
    @Id
    private String id;
    @OneToMany(mappedBy = "simulation", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoryEvent> history = new ArrayList<HistoryEvent>();
    @OneToOne
    @JoinColumn(name = "document_id")
    private Document document;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "document = " + document + ")";
    }
}
