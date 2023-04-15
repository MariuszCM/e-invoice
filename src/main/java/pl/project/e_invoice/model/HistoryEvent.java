package pl.project.e_invoice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@SuperBuilder
public class HistoryEvent {
    private final EventType eventType;
    private final LocalDateTime eventTime;

}
