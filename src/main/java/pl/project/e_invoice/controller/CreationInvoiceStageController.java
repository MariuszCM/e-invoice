package pl.project.e_invoice.controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import pl.project.e_invoice.application.PrimaryStageInitializer;
import pl.project.e_invoice.integration.regonApi.model.CompanyIntegration;
import pl.project.e_invoice.model.Company;
import pl.project.e_invoice.model.Simulation;
import pl.project.e_invoice.model.documents.Invoice;
import pl.project.e_invoice.model.documents.InvoiceStatus;
import pl.project.e_invoice.model.documents.InvoiceType;
import pl.project.e_invoice.service.InvoiceService;
import pl.project.e_invoice.service.DefaultCompanyService;
import pl.project.e_invoice.service.DefaultSimulationService;
import pl.project.e_invoice.service.integration.NipApiService;

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
    private final InvoiceService invoiceService;
    private final DefaultCompanyService companyService;
    private final NipApiService regonApiPromptService;
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
    @FXML
    private SplitPane creationSplitPane;
    private Stage stage;
    private Simulation sim;
    private Invoice invoice;
    private Company seller;
    private Company buyer;
    @Value("${company.nip}")
    private String myCompanyNip;
    private boolean isWindowOpen = false;


    @FXML
    public void initialize() {
        stageCreationIfNotOpen();
        addItemsForChoiceBoxes();
        addHandlers();
        addListenersForFields();
        addDisableForButtons();
    }

    private void addDisableForButtons() {
        saveInvoice.disableProperty().bind(
                Bindings.isEmpty(sellerNip.textProperty())
                        .or(Bindings.isEmpty(buyerNip.textProperty()))
                        .or(Bindings.isEmpty(invoiceId.textProperty()))
                        .or(Bindings.isEmpty(amount.textProperty()))
        );
        stage.setOnCloseRequest(event -> isWindowOpen = false);
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

    private void addListenersForBuyer() {
        addListenerToProperty(buyerNip.textProperty(), buyer::setNip);
        addListenerToProperty(buyerName.textProperty(), buyer::setCompanyName);
        addListenerToProperty(buyerAddress.textProperty(), buyer::setAddress);
        addListenerToProperty(buyerEmail.textProperty(), buyer::setEmail);
    }

    private void addListenersForSeller() {
        addListenerToProperty(sellerNip.textProperty(), seller::setNip);
        addListenerToProperty(sellerName.textProperty(), seller::setCompanyName);
        addListenerToProperty(sellerAddress.textProperty(), seller::setAddress);
        addListenerToProperty(sellerEmail.textProperty(), seller::setEmail);
    }

    private void addListenersForInvoice() {
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
            this.stage.setTitle("Utwórz nową fakturę");
            this.stage.getIcons().add(new Image(PrimaryStageInitializer.class.getResourceAsStream("/logo.png")));
            this.stage.setScene(new Scene(creationSplitPane));

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
    }

    private void addHandlersForChoiceBoxes() {
        this.invoiceType.setOnAction(event -> setMyCompanyNipForField());
    }

    private void addHandlersForButtons() {
        this.saveInvoice.setOnAction(event -> saveInvoice());
        this.searchCompany.setOnAction(event -> searchCompanyByApi());
    }

    private void setMyCompanyNipForField() {
        if (InvoiceType.convertLabelTOEnum().apply(invoiceType.getValue()) == InvoiceType.INCOME) {
            buyerNip.textProperty().setValue(myCompanyNip);
            sellerNip.textProperty().setValue(null);
        } else if (InvoiceType.convertLabelTOEnum().apply(invoiceType.getValue()) == InvoiceType.OUTCOME) {
            buyerNip.textProperty().setValue(null);
            sellerNip.textProperty().setValue(myCompanyNip);
        }
    }

    private void searchCompanyByApi() {
        searchAndSetCompanyFields(buyerNip, buyerName, buyerAddress, buyerEmail);
        searchAndSetCompanyFields(sellerNip, sellerName, sellerAddress, sellerEmail);
    }

    private void searchAndSetCompanyFields(TextField nipField, TextField nameField, TextField addressField, TextField emailField) {
        CompanyIntegration companyFromApi = regonApiPromptService.getFullCompanyReport(nipField.textProperty().get());
        nameField.textProperty().setValue(companyFromApi.getCompanyName());
        if (companyFromApi.getStreet() != null && companyFromApi.getCity() != null) {
            addressField.textProperty().setValue(companyFromApi.getStreet() + " " + companyFromApi.getCity());
        } else {
            addressField.textProperty().setValue(null);
        }
        emailField.textProperty().setValue(companyFromApi.getEmail());
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
