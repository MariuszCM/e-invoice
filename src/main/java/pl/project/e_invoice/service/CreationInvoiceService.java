package pl.project.e_invoice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.project.e_invoice.model.EventType;
import pl.project.e_invoice.model.documents.Document;
import pl.project.e_invoice.model.documents.Invoice;
import pl.project.e_invoice.repository.InvoiceRepository;

@Service
@Slf4j
@AllArgsConstructor
public class CreationInvoiceService implements CreationService {
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
}
