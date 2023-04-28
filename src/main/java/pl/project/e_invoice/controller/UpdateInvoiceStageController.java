package pl.project.e_invoice.controller;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Controller;
import pl.project.e_invoice.application.PrimaryStageInitializer;
import pl.project.e_invoice.integration.regonApi.model.CompanyIntegration;
import pl.project.e_invoice.model.Company;
import pl.project.e_invoice.model.Simulation;
import pl.project.e_invoice.model.documents.Invoice;
import pl.project.e_invoice.model.documents.InvoiceStatus;
import pl.project.e_invoice.model.documents.InvoiceType;
import pl.project.e_invoice.service.DefaultCompanyService;
import pl.project.e_invoice.service.DefaultSimulationService;
import pl.project.e_invoice.service.InvoiceService;
import pl.project.e_invoice.service.integration.NipApiService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static pl.project.e_invoice.controller.ControllerHelper.addListenerToChoiceBox;
import static pl.project.e_invoice.controller.ControllerHelper.addListenerToProperty;

@Controller
@RequiredArgsConstructor
@FxmlView("UpdateStage.fxml")
//TODO fecator i wyniesienie do abstrakcji !!!
public class UpdateInvoiceStageController {
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
    @FXML
    private Text header;
    private Stage stage;
    private Simulation sim;
    @Setter
    private Invoice invoice;
    private Company seller;
    private Company buyer;
    private boolean isWindowOpen = false;

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
            this.stage.setTitle("Zmiany w fakturze");
            this.header.setText("Wprowadzanie zmian w fakturze");
            this.stage.getIcons().add(new Image(Objects.requireNonNull(PrimaryStageInitializer.class.getResourceAsStream("/logo.png"))));
            this.stage.setScene(new Scene(creationSplitPane));
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
        invoiceService.updateDocument(invoice);
        isWindowOpen = false;
        //TODO odlozyc zdarzeniu o aktualizacji
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
