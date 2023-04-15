package pl.project.e_invoice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode()
@ToString(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Embeddable
public class HistoryEvent {
    private EventType eventType;
    private LocalDateTime eventTime;
}
