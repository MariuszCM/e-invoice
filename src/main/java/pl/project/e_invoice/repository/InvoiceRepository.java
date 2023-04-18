package pl.project.e_invoice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.project.e_invoice.model.invoice.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, String> {
}
