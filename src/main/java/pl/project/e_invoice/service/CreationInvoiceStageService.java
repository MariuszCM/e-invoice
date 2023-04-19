package pl.project.e_invoice.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.project.e_invoice.model.Simulation;
import pl.project.e_invoice.model.documents.Document;
import pl.project.e_invoice.model.documents.Invoice;
import pl.project.e_invoice.repository.InvoiceRepository;

@Service
@Slf4j
@AllArgsConstructor
public class CreationInvoiceStageService implements CreationService{
    private InvoiceRepository invoiceRepository;
    @Override
    public Document createDocument(Simulation sim) {
        //TODO
        return null;
    }
}
