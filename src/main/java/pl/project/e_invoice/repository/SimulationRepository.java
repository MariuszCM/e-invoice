package pl.project.e_invoice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.project.e_invoice.model.Simulation;

public interface SimulationRepository extends JpaRepository<Simulation, String> {
}
