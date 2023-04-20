package pl.project.e_invoice.service;

import pl.project.e_invoice.model.Company;

import java.util.Optional;

public interface CompanyService {
    Company createOrUpdateCompany(Company company);
    Optional<Company> findCompany(String nip);
}
