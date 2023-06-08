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
        saveInvoiceButton.disableProperty().bind(
                Bindings.isEmpty(sellerNipTextField.textProperty())
                        .or(Bindings.isEmpty(buyerNipTextField.textProperty()))
                        .or(Bindings.isEmpty(invoiceIdTextField.textProperty()))
                        .or(Bindings.isEmpty(amountTextField.textProperty()))
        );
        invoiceIdTextField.setDisable(true);
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
        this.saveInvoiceButton.setOnAction(event -> saveInvoice());
        this.searchCompanyButton.setOnAction(event -> searchCompanyByApi());
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
        invoiceIdTextField.setText(invoice.getId());
        invoiceTypeChoiceBox.setValue(invoice.getInvoiceType() != null ? invoice.getInvoiceType().getLabel() : null);
        invoiceStatusChoiceBox.setValue(invoice.getInvoiceStatus() != null ? invoice.getInvoiceStatus().getLabel() : null);
        invoiceIssueDatePicker.setValue(invoice.getDateOfIssue());
        invoiceNameTextField.setText(invoice.getName());
        amountTextField.setText(invoice.getValue() != null ? invoice.getValue().toString() : null);

        sellerNipTextField.setText(seller.getNip());
        sellerAddressTextField.setText(seller.getAddress());
        sellerNameTextField.setText(seller.getCompanyName());
        sellerEmailTextField.setText(seller.getEmail());

        buyerNipTextField.setText(buyer.getNip());
        buyerAddressTextField.setText(buyer.getAddress());
        buyerNameTextField.setText(buyer.getCompanyName());
        buyerEmailTextField.setText(buyer.getEmail());
    }
}
