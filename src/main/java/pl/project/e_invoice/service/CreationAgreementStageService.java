package pl.project.e_invoice.service;

import pl.project.e_invoice.model.documents.Document;

public class CreationAgreementStageService implements CreationService {

    /** Klasa stworzona w celu zobrazowania elastycznosci kodu -
     * dzieki zbudowanej architekturze bardzo latwo dodawac kolejne tworzenie dokumentow np. umowy
     */

    @Override
    public Document createDocument(Document sim) {
        illegalOperation();
        return null;
    }

    private void illegalOperation() {
        throw new IllegalStateException("Agreement operations ale unsupported");
    }
}
