package pl.project.e_invoice.service;

import pl.project.e_invoice.model.Simulation;
import pl.project.e_invoice.model.documents.Document;

public interface CreationService {
    void createDocument(Document document);
}
