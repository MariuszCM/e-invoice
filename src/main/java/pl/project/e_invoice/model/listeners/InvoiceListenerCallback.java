package pl.project.e_invoice.model.listeners;

import pl.project.e_invoice.model.documents.Invoice;

public interface InvoiceListenerCallback {
    void onInvoiceEvent(Invoice invoice, DatabaseListenerType notifyType);
}
