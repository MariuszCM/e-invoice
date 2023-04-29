package pl.project.e_invoice.controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Controller;
import pl.project.e_invoice.model.Company;
import pl.project.e_invoice.model.documents.Invoice;

@Controller
@RequiredArgsConstructor
@FxmlView("UpdateStage.fxml")
public class UpdateInvoiceStageController extends AbstractInvoiceModyficationStageController {
    @FXML
    private SplitPane updateSplitPane;

    @FXML
    public void initialize() {
        stageCreationIfNotOpen();
        addItemsForChoiceBoxes();
        addHandlers();
    }

    private void addDisableForButtons() {
        saveInvoice.disableProperty().bind(
                Bindings.isEmpty(sellerNip.textProperty())
                        .or(Bindings.isEmpty(buyerNip.textProperty()))
                        .or(Bindings.isEmpty(invoiceId.textProperty()))
                        .or(Bindings.isEmpty(amount.textProperty()))
        );
        //nie chcemy aby ktos zmianial numer faktury
        invoiceId.setDisable(true);
    }

    private void stageCreationIfNotOpen() {
        if (!this.isWindowOpen) {
            this.stage = new Stage();
            this.stage.setTitle(stageTitle);
            this.stage.getIcons().add(stageImage);
            this.stage.setScene(new Scene(updateSplitPane));
            this.progressIndicator.setVisible(false);
            this.isWindowOpen = true;
        }
    }

    private void addHandlers() {
        addHandlersForButtons();
        stage.setOnCloseRequest(event -> isWindowOpen = false);
    }

    private void addHandlersForButtons() {
        this.saveInvoice.setOnAction(event -> saveInvoice());
        this.searchCompany.setOnAction(event -> searchCompanyByApi());
    }

    private void saveInvoice() {
        getCompanyIfExistOrCreate(seller);
        getCompanyIfExistOrCreate(buyer);
        invoiceService.updateDocument(invoice);
        isWindowOpen = false;
        stage.close();
    }

    private void getCompanyIfExistOrCreate(Company company) {
        companyService.findOrUpdateCompanyIfChanged(company);
    }

    protected void openStage(Invoice invoice) {
        this.invoice = invoice;
        this.sim = invoice.getSimulation();
        this.seller = invoice.getSeller();
        this.buyer = invoice.getBuyer();
        addListenersForFields();
        addDisableForButtons();
        fillDataToFields();
        stage.show();
    }

    private void fillDataToFields() {
        invoiceId.setText(invoice.getId());
        invoiceType.setValue(invoice.getInvoiceType() != null ? invoice.getInvoiceType().getLabel() : null);
        invoiceStatus.setValue(invoice.getInvoiceStatus() != null ? invoice.getInvoiceStatus().getLabel() : null);
        invoiceIssueDate.setValue(invoice.getDateOfIssue());
        invoiceName.setText(invoice.getName());
        amount.setText(invoice.getValue() != null ? invoice.getValue().toString() : null);

        sellerNip.setText(seller.getNip());
        sellerAddress.setText(seller.getAddress());
        sellerName.setText(seller.getCompanyName());
        sellerEmail.setText(seller.getEmail());

        buyerNip.setText(buyer.getNip());
        buyerAddress.setText(buyer.getAddress());
        buyerName.setText(buyer.getCompanyName());
        buyerEmail.setText(buyer.getEmail());
    }
}
