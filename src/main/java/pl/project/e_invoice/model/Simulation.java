package pl.project.e_invoice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.project.e_invoice.model.documents.Document;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class Simulation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "simulation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoryEvent> history = new ArrayList<HistoryEvent>();
    @OneToOne
    @JoinColumn(name = "document_id")
    private Document document;

    public static HistoryEvent addStandardEventToHistory(Simulation sim, EventType eventType) {
        HistoryEvent event = HistoryEvent.builder()
                .eventTime(LocalDateTime.now())
                .eventType(eventType)
                .simulation(sim)
                .build();
        sim.getHistory().add(event);
        return event;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "document = " + document + ")";
    }
}
