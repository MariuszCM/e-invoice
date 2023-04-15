package pl.project.e_invoice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Simulation {
    List<HistoryEvent> history;
}
