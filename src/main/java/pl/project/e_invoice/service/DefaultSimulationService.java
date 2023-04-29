package pl.project.e_invoice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.project.e_invoice.model.EventType;
import pl.project.e_invoice.model.HistoryEvent;
import pl.project.e_invoice.model.Simulation;
import pl.project.e_invoice.repository.SimulationRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class DefaultSimulationService implements SimulationService{
    private SimulationRepository simulationRepository;

    @Override
    public Simulation createSimulation() {
        Simulation sim = new Simulation();
        sim.setId(UUID.randomUUID().toString());
        simulationRepository.save(sim);
        addStandardEventToHistory(sim, EventType.SIMUlACTION_CREATED);
        log.info("{} just created", sim);

        return sim;
    }

    @Override
    public Simulation saveAndRefreshSimulation(Simulation simulation) {
        var sim = simulationRepository.save(simulation);
        log.info("sim refreshed {}", simulation);
        return sim;
    }

    public HistoryEvent addStandardEventToHistory(Simulation sim, EventType eventType) {
        HistoryEvent event = HistoryEvent.builder()
                .eventTime(LocalDateTime.now())
                .eventType(eventType)
                .simulation(sim)
                .build();
        sim.getHistory().add(event);
        saveAndRefreshSimulation(sim);
        return event;
    }


}
