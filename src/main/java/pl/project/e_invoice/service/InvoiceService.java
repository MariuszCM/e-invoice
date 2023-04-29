package pl.project.e_invoice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.project.e_invoice.model.EventType;
import pl.project.e_invoice.model.documents.Document;
import pl.project.e_invoice.model.documents.Invoice;
import pl.project.e_invoice.repository.InvoiceRepository;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class InvoiceService implements DocumentService {
    private InvoiceRepository invoiceRepository;
    private DefaultSimulationService defaultSimulationService;

    @Override
    public Document createDocument(Document document) {
        if (document instanceof Invoice) {
            var docToReturn = invoiceRepository.save((Invoice) document);
            defaultSimulationService.addStandardEventToHistory(((Invoice) document).getSimulation(), EventType.INVOICE_CREATED);
            log.info("invoice just created to sim: {}", ((Invoice) document).getSimulation());
            return docToReturn;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Document updateDocument(Document document) {
        if (document instanceof Invoice) {
            var docToReturn = invoiceRepository.save((Invoice) document);
            log.info("invoice just updated to sim: {}", ((Invoice) document).getSimulation());
            return docToReturn;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void deleteDocument(Document document) {
        if (invoiceRepository.existsById(document.getId()) && document instanceof Invoice) {
            invoiceRepository.delete((Invoice) document);
        }
    }

    @Override
    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }
}
