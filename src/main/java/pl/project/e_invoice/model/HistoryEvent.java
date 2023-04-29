package pl.project.e_invoice.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode()
@NoArgsConstructor
@SuperBuilder
@Entity
public class HistoryEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private EventType eventType;
    private LocalDateTime eventTime;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "simulation_id")
    private Simulation simulation;
}
