package pl.project.e_invoice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.project.e_invoice.model.HistoryEvent;

public interface HistoryEventRepository extends JpaRepository<HistoryEvent, Long> {
}
