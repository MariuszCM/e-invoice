package pl.project.e_invoice.service;

import pl.project.e_invoice.model.documents.Document;

import java.util.List;

public class AgreementStageService implements DocumentService {

    /** Klasa stworzona w celu zobrazowania elastycznosci kodu -
     * dzieki zbudowanej architekturze bardzo latwo dodawac kolejne tworzenie dokumentow np. umowy
     */

    @Override
    public Document createDocument(Document sim) {
        illegalOperation();
        return null;
    }

    @Override
    public <T extends Document> List<T> findAll() {
        illegalOperation();
        return null;
    }

    @Override
    public Document updateDocument(Document document) {
        illegalOperation();
        return null;
    }

    private void illegalOperation() {
        throw new IllegalStateException("Agreement operations ale unsupported");
    }
}
