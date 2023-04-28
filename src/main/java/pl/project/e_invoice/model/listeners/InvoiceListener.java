package pl.project.e_invoice.model.listeners;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import pl.project.e_invoice.model.documents.Invoice;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = "singleton")
public class InvoiceListener {
    private final List<InvoiceListenerCallback> listeners = new ArrayList<>();

    public void addListener(InvoiceListenerCallback listener) {
        listeners.add(listener);
    }

    public void removeListener(InvoiceListenerCallback listener) {
        listeners.remove(listener);
    }

    public void notifyListeners(Invoice invoice, DatabaseListenerType notifyType) {
        for (InvoiceListenerCallback listener : listeners) {
            listener.onInvoiceEvent(invoice, notifyType);
        }
    }
}
