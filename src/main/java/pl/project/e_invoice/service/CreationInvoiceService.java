package pl.project.e_invoice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.project.e_invoice.model.EventType;
import pl.project.e_invoice.model.Simulation;
import pl.project.e_invoice.model.documents.Document;
import pl.project.e_invoice.model.documents.Invoice;
import pl.project.e_invoice.repository.InvoiceRepository;


@Service
@Slf4j
@AllArgsConstructor
public class CreationInvoiceService implements CreationService {
    private InvoiceRepository invoiceRepository;

    @Override
    public void createDocument(Document document) {
        if (document instanceof Invoice) {
            invoiceRepository.save((Invoice) document);
            Simulation.addStandardEventToHistory(((Invoice) document).getSimulation(), EventType.INVOICE_CREATED);
        } else {
            throw new IllegalArgumentException();
        }
    }
}
