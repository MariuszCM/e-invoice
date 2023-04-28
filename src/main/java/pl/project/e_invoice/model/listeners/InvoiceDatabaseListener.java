package pl.project.e_invoice.model.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.project.e_invoice.model.documents.Invoice;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;

@Component
@Scope(value = "singleton")
public class InvoiceDatabaseListener {
    @Autowired
    private InvoiceListener invoiceListener;

    @PostPersist
    public void onCustomerCreated(Invoice invoice) {
        invoiceListener.notifyListeners(invoice, DatabaseListenerType.CREATE);
    }

    @PostRemove
    public void onCustomerRemoved(Invoice invoice) {
        invoiceListener.notifyListeners(invoice, DatabaseListenerType.DELETE);
    }

    @PostUpdate
    public void onCustomerUpdated(Invoice invoice) {
        invoiceListener.notifyListeners(invoice, DatabaseListenerType.UPDATE);
    }
}
