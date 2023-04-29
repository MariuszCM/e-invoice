package pl.project.e_invoice.service;

import pl.project.e_invoice.model.documents.Document;

import java.util.List;

public interface DocumentService {
    //TODO poprawa na generyki
    Document createDocument(Document document);
    Document updateDocument(Document document);
    void deleteDocument(Document document);
    <T extends Document> List<T> findAll();
}
