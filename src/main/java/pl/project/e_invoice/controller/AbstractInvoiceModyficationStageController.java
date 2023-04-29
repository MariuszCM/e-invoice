package pl.project.e_invoice.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import pl.project.e_invoice.integration.regonApi.model.CompanyIntegration;
import pl.project.e_invoice.model.Company;
import pl.project.e_invoice.model.Simulation;
import pl.project.e_invoice.model.documents.Invoice;
import pl.project.e_invoice.model.documents.InvoiceStatus;
import pl.project.e_invoice.model.documents.InvoiceType;
import pl.project.e_invoice.service.DefaultCompanyService;
import pl.project.e_invoice.service.InvoiceService;
import pl.project.e_invoice.service.integration.NipApiService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

import static pl.project.e_invoice.controller.ControllerHelper.addListenerToChoiceBox;
import static pl.project.e_invoice.controller.ControllerHelper.addListenerToProperty;


public abstract class AbstractInvoiceModyficationStageController extends AbstractStageController {
    @Autowired
    protected InvoiceService invoiceService;
    @Autowired
    protected DefaultCompanyService companyService;
    @Autowired
    protected NipApiService regonApiPromptService;
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
    protected ProgressIndicator progressIndicator = new ProgressIndicator(.314);
    protected Stage stage;
    protected Simulation sim;
    @Setter
    protected Invoice invoice;
    protected Company seller;
    protected Company buyer;
    protected boolean isWindowOpen = false;

    //srednio mi sie podoba takie rozwiazanie ale nie widze innego
    //przystepnego sposobu na rozwiazanie tego
    protected void addListenersForFields() {
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

    protected void searchCompanyByApi() {
        searchCompany(buyerNip, buyerName, buyerAddress, buyerEmail);
        searchCompany(sellerNip, sellerName, sellerAddress, sellerEmail);
    }

    private void searchCompany(TextField nipField, TextField nameField, TextField addressField, TextField emailField) {
        Task<CompanyIntegration> task = new Task<>() {
            @Override
            protected CompanyIntegration call() throws Exception {
                progressIndicator.setVisible(true);
                CompanyIntegration fullCompanyReport = regonApiPromptService.getFullCompanyReport(nipField.textProperty().get());
                progressIndicator.setVisible(false);
                return fullCompanyReport;
            }
        };

        task.setOnSucceeded(event -> {
            CompanyIntegration companyFromApi = task.getValue();
            Platform.runLater(() -> setCompanyFieldsFoundByApi(nameField, addressField, emailField, companyFromApi));
        });

        new Thread(task).start();
    }

    private void setCompanyFieldsFoundByApi(TextField nameField, TextField addressField, TextField emailField, CompanyIntegration companyFromApi) {
        nameField.textProperty().setValue(companyFromApi.getCompanyName());
        if (companyFromApi.getStreet() != null && companyFromApi.getCity() != null) {
            addressField.textProperty().setValue(companyFromApi.getStreet() + " " + companyFromApi.getCity());
        } else {
            addressField.textProperty().setValue(null);
        }
        emailField.textProperty().setValue(companyFromApi.getEmail());
    }

    protected void addItemsForChoiceBoxes() {
        invoiceStatus.getItems().addAll(Arrays.stream(InvoiceStatus.values()).map(InvoiceStatus::getLabel).collect(Collectors.toList()));
        invoiceType.getItems().addAll(Arrays.stream(InvoiceType.values()).map(InvoiceType::getLabel).collect(Collectors.toList()));
    }
}
