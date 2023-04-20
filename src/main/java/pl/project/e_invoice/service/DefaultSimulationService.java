package pl.project.e_invoice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.project.e_invoice.model.EventType;
import pl.project.e_invoice.model.Simulation;
import pl.project.e_invoice.repository.SimulationRepository;

@Service
@AllArgsConstructor
@Slf4j
public class DefaultSimulationService implements SimulationService{
    private SimulationRepository simulationRepository;

    @Override
    public Simulation createSimulation() {
        Simulation sim = new Simulation();
        Simulation.addStandardEventToHistory(sim, EventType.SIMUlACTION_CREATION);
        simulationRepository.save(sim);
        log.info("{} just created", sim);

        return sim;
    }


}
