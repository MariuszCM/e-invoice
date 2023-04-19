package pl.project.e_invoice.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Controller;
import pl.project.e_invoice.model.documents.InvoiceStatus;
import pl.project.e_invoice.model.documents.InvoiceType;

import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@FxmlView("CreationStage.fxml")
public class CreationInvoiceStageController {

    private Stage stage;
    private boolean isWindowOpen = false;
    @FXML
    protected Button backToMainStage;
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
    public void initialize() {
        stageCreationIfNotOpen();
        addItemsForChoiceBoxes();
    }

    private void addItemsForChoiceBoxes(){
        invoiceStatus.getItems().addAll(Arrays.stream(InvoiceStatus.values()).map(InvoiceStatus::getLabel).collect(Collectors.toList()));
        invoiceType.getItems().addAll(Arrays.stream(InvoiceType.values()).map(InvoiceType::getLabel).collect(Collectors.toList()));
    }

    private void stageCreationIfNotOpen() {
        if (!isWindowOpen) {
            this.stage = new Stage();
            stage.setTitle("Create New InVoice");
            stage.setScene(new Scene(creationSplitPane));
            isWindowOpen = true;
        }
    }

    protected void openStage(){
        stage.show();
    }
}
