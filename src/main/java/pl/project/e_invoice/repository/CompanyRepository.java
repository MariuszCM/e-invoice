package pl.project.e_invoice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.project.e_invoice.model.Company;

public interface CompanyRepository extends JpaRepository<Company, String> {
}
