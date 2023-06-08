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
    protected Button saveInvoiceButton;
    @FXML
    protected Button searchCompanyButton;
    @FXML
    protected TextField invoiceIdTextField;
    @FXML
    protected TextField sellerNipTextField;
    @FXML
    protected TextField sellerNameTextField;
    @FXML
    protected TextField sellerAddressTextField;
    @FXML
    protected TextField sellerEmailTextField;
    @FXML
    protected TextField buyerNipTextField;
    @FXML
    protected TextField buyerNameTextField;
    @FXML
    protected TextField buyerAddressTextField;
    @FXML
    protected TextField buyerEmailTextField;
    @FXML
    protected TextField invoiceNameTextField;
    //TODO dodac walidacje na liczby
    //TODO dobrze by bylo tez dodac walidacje na . i ,
    @FXML
    protected TextField amountTextField;
    @FXML
    protected ChoiceBox<String> invoiceStatusChoiceBox;
    @FXML
    protected ChoiceBox<String> invoiceTypeChoiceBox;
    @FXML
    protected DatePicker invoiceIssueDatePicker;
    @FXML
    protected ProgressIndicator progressIndicator = new ProgressIndicator(.314);
    protected Stage stage;
    protected Simulation sim;
    @Setter
    protected Invoice invoice;
    protected Company seller;
    protected Company buyer;
    protected boolean isWindowOpen = false;

    protected void addListenersForFields() {
        addListenersForBuyer();
        addListenersForSeller();
        addListenersForInvoice();
    }

    private void addListenersForBuyer() {
        addListenerToProperty(buyerNipTextField.textProperty(), buyer::setNip);
        addListenerToProperty(buyerNameTextField.textProperty(), buyer::setCompanyName);
        addListenerToProperty(buyerAddressTextField.textProperty(), buyer::setAddress);
        addListenerToProperty(buyerEmailTextField.textProperty(), buyer::setEmail);
    }

    private void addListenersForSeller() {
        addListenerToProperty(sellerNipTextField.textProperty(), seller::setNip);
        addListenerToProperty(sellerNameTextField.textProperty(), seller::setCompanyName);
        addListenerToProperty(sellerAddressTextField.textProperty(), seller::setAddress);
        addListenerToProperty(sellerEmailTextField.textProperty(), seller::setEmail);
    }

    private void addListenersForInvoice() {
        addListenerToProperty(invoiceIdTextField.textProperty(), invoice::setId);
        addListenerToProperty(invoiceNameTextField.textProperty(), invoice::setName);
        addListenerToProperty(amountTextField.textProperty(), BigDecimal::new, invoice::setValue);
        addListenerToChoiceBox(invoiceStatusChoiceBox, InvoiceStatus.convertLabelTOEnum(), invoice::setInvoiceStatus);
        addListenerToChoiceBox(invoiceTypeChoiceBox, InvoiceType.convertLabelTOEnum(), invoice::setInvoiceType);
        addListenerToProperty(invoiceIssueDatePicker.valueProperty(), invoice::setDateOfIssue);
    }

    protected void searchCompanyByApi() {
        searchCompany(buyerNipTextField, buyerNameTextField, buyerAddressTextField, buyerEmailTextField);
        searchCompany(sellerNipTextField, sellerNameTextField, sellerAddressTextField, sellerEmailTextField);
    }

    private void searchCompany(TextField nipField, TextField nameField, TextField addressField, TextField emailField) {
        Task<CompanyIntegration> task = new Task<>() {
            @Override
            protected CompanyIntegration call() {
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
        invoiceStatusChoiceBox.getItems().addAll(Arrays.stream(InvoiceStatus.values()).map(InvoiceStatus::getLabel).collect(Collectors.toList()));
        invoiceTypeChoiceBox.getItems().addAll(Arrays.stream(InvoiceType.values()).map(InvoiceType::getLabel).collect(Collectors.toList()));
    }
}
