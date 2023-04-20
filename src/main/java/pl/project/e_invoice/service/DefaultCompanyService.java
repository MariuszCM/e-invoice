package pl.project.e_invoice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.project.e_invoice.model.Company;
import pl.project.e_invoice.repository.CompanyRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class DefaultCompanyService implements CompanyService {
    private CompanyRepository companyRepository;

    @Override
    public Company createOrUpdateCompany(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Optional<Company> findCompany(String nip) {
        return companyRepository.findById(nip);
    }

    //sprawdzamy czy odczytana z bazy firma jest taka sama jak ta, ktora wprowadzilismy
    //bo np. mogl sie zmienic numer telefonu
    public Company findOrUpdateCompanyIfChanged(Company company){
        var resultOfSearch = findCompany(company.getNip());
        if (resultOfSearch.isPresent() && resultOfSearch.get().equals(company)) {
            return resultOfSearch.get();
        } else {
            return createOrUpdateCompany(company);
        }
    }
}
