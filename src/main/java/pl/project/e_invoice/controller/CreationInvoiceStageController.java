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
import pl.project.e_invoice.model.documents.InvoiceType;
import pl.project.e_invoice.service.DefaultSimulationService;

@Controller
@RequiredArgsConstructor
@FxmlView("CreationStage.fxml")
public class CreationInvoiceStageController extends AbstractInvoiceModyficationStageController {

    private final DefaultSimulationService simulationService;
    @FXML
    private SplitPane creationSplitPane;


    @FXML
    public void initialize() {
        stageCreationIfNotOpen();
        addItemsForChoiceBoxes();
        addHandlers();
        addListenersForFields();
        addDisableForButtons();
    }

    private void addDisableForButtons() {
        saveInvoiceButton.disableProperty().bind(
                Bindings.isEmpty(sellerNipTextField.textProperty())
                        .or(Bindings.isEmpty(buyerNipTextField.textProperty()))
                        .or(Bindings.isEmpty(invoiceIdTextField.textProperty()))
                        .or(Bindings.isEmpty(amountTextField.textProperty()))
        );
    }

    private void stageCreationIfNotOpen() {
        if (!this.isWindowOpen) {
            this.stage = new Stage();
            this.stage.setTitle(stageTitle);
            this.stage.getIcons().add(stageImage);
            this.stage.setScene(new Scene(creationSplitPane));
            this.progressIndicator.setVisible(false);

            simulationCreation();
            this.seller = new Company();
            this.buyer = new Company();
            this.invoice = new Invoice();
            this.isWindowOpen = true;
            invoice.setSimulation(sim);
            invoice.setSeller(seller);
            invoice.setBuyer(buyer);
            sim.setDocument(invoice);
        }
    }

    private void simulationCreation() {
        this.sim = this.simulationService.createSimulation();
    }

    private void addHandlers() {
        addHandlersForButtons();
        addHandlersForChoiceBoxes();
        stage.setOnCloseRequest(event -> isWindowOpen = false);
    }

    private void addHandlersForChoiceBoxes() {
        this.invoiceTypeChoiceBox.setOnAction(event -> setMyCompanyNipForField());
    }

    private void addHandlersForButtons() {
        this.saveInvoiceButton.setOnAction(event -> saveInvoice());
        this.searchCompanyButton.setOnAction(event -> searchCompanyByApi());
    }

    private void setMyCompanyNipForField() {
        if (InvoiceType.convertLabelTOEnum().apply(invoiceTypeChoiceBox.getValue()) == InvoiceType.INCOME) {
            buyerNipTextField.textProperty().setValue(myCompanyNip);
            sellerNipTextField.textProperty().setValue(null);
        } else if (InvoiceType.convertLabelTOEnum().apply(invoiceTypeChoiceBox.getValue()) == InvoiceType.OUTCOME) {
            buyerNipTextField.textProperty().setValue(null);
            sellerNipTextField.textProperty().setValue(myCompanyNip);
        }
    }


    private void saveInvoice() {
        getCompanyIfExistOrCreate(seller);
        getCompanyIfExistOrCreate(buyer);
        invoiceService.createDocument(invoice);
        isWindowOpen = false;
        stage.close();
    }

    private void getCompanyIfExistOrCreate(Company company) {
        companyService.findOrUpdateCompanyIfChanged(company);
    }

    protected void openStage() {
        stage.show();
    }
}
