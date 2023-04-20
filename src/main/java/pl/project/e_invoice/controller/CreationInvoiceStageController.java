package pl.project.e_invoice.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Controller;
import pl.project.e_invoice.model.Company;
import pl.project.e_invoice.model.EventType;
import pl.project.e_invoice.model.Simulation;
import pl.project.e_invoice.model.documents.Invoice;
import pl.project.e_invoice.model.documents.InvoiceStatus;
import pl.project.e_invoice.model.documents.InvoiceType;
import pl.project.e_invoice.service.CreationInvoiceService;
import pl.project.e_invoice.service.DefaultSimulationService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

import static pl.project.e_invoice.controller.ControllerHelper.addListenerToChoiceBox;
import static pl.project.e_invoice.controller.ControllerHelper.addListenerToProperty;

@Controller
@RequiredArgsConstructor
@FxmlView("CreationStage.fxml")
public class CreationInvoiceStageController {

    private final DefaultSimulationService simulationService;
    private final CreationInvoiceService invoiceService;
    @FXML
    protected Button saveInvoice;
    @FXML
    protected Button searchCompany;
    @FXML
    protected TextField invoiceId;
    @FXML
    protected TextField sellerNip;
    @FXML
    protected TextField sellerName;
    @FXML
    protected TextField sellerAddress;
    @FXML
    protected TextField sellerEmail;
    @FXML
    protected TextField buyerNip;
    @FXML
    protected TextField buyerName;
    @FXML
    protected TextField buyerAddress;
    @FXML
    protected TextField buyerEmail;
    @FXML
    protected TextField invoiceName;
    //TODO dodac walidacje na liczby
    //TODO dobrze by bylo tez dodac walidacje na . i ,
    @FXML
    protected TextField amount;
    @FXML
    protected ChoiceBox<String> invoiceStatus;
    @FXML
    protected ChoiceBox<String> invoiceType;
    @FXML
    protected DatePicker invoiceIssueDate;
    private Stage stage;
    private Simulation sim;
    private Invoice invoice;
    private Company seller;
    private Company buyer;
    private boolean isWindowOpen = false;
    @FXML
    private SplitPane creationSplitPane;


    @FXML
    public void initialize() {
        stageCreationIfNotOpen();
        addItemsForChoiceBoxes();
        addHandlersForButtons();
        addListenersForFields();
    }

    private void addItemsForChoiceBoxes() {
        invoiceStatus.getItems().addAll(Arrays.stream(InvoiceStatus.values()).map(InvoiceStatus::getLabel).collect(Collectors.toList()));
        invoiceType.getItems().addAll(Arrays.stream(InvoiceType.values()).map(InvoiceType::getLabel).collect(Collectors.toList()));
    }

    //srednio mi sie podoba takie rozwiazanie ale nie widze innego
    //przystepnego sposobu na rozwiazanie tego
    private void addListenersForFields() {
        addListenersForBuyer();
        addListenersForSeller();
        addListenersForInvoice();
    }

    private void addListenersForBuyer(){
        addListenerToProperty(buyerNip.textProperty(), buyer::setNip);
        addListenerToProperty(buyerName.textProperty(), buyer::setCompanyName);
        addListenerToProperty(buyerAddress.textProperty(), buyer::setAddress);
        addListenerToProperty(buyerEmail.textProperty(), buyer::setEmail);
    }

    private void addListenersForSeller(){
        addListenerToProperty(sellerNip.textProperty(), seller::setNip);
        addListenerToProperty(sellerName.textProperty(), seller::setCompanyName);
        addListenerToProperty(sellerAddress.textProperty(), seller::setAddress);
        addListenerToProperty(sellerEmail.textProperty(), seller::setEmail);
    }

    private void addListenersForInvoice(){
        addListenerToProperty(invoiceId.textProperty(), invoice::setId);
        addListenerToProperty(invoiceName.textProperty(), invoice::setName);
        addListenerToProperty(amount.textProperty(), BigDecimal::new, invoice::setValue);
        addListenerToChoiceBox(invoiceStatus, InvoiceStatus.convertLabelTOEnum(), invoice::setInvoiceStatus);
        addListenerToChoiceBox(invoiceType, InvoiceType.convertLabelTOEnum(), invoice::setInvoiceType);
        addListenerToProperty(invoiceIssueDate.valueProperty(), invoice::setDateOfIssue);
    }


    private void stageCreationIfNotOpen() {
        if (!this.isWindowOpen) {
            this.stage = new Stage();
            this.stage.setTitle("Create New InVoice");
            this.stage.setScene(new Scene(creationSplitPane));
            simulationCreation();
            this.seller = new Company();
            this.buyer = new Company();
            this.invoice = new Invoice();
            this.isWindowOpen = true;
        }
    }

    private void simulationCreation() {
        this.sim = this.simulationService.createSimulation();
    }

    private void addHandlersForButtons() {
        this.saveInvoice.setOnAction(event -> saveInvoice());
    }

    private void saveInvoice() {
        invoice.setSimulation(sim);
        invoice.setSeller(seller);
        invoice.setBuyer(buyer);
        sim.setDocument(invoice);
        invoiceService.createDocument(invoice);
    }

    protected void openStage() {
        stage.show();
    }
}
