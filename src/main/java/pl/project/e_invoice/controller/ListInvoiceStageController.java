package pl.project.e_invoice.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import net.rgielen.fxweaver.core.LazyFxControllerAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import pl.project.e_invoice.application.PrimaryStageInitializer;
import pl.project.e_invoice.model.documents.Invoice;
import pl.project.e_invoice.model.listeners.DatabaseListenerType;
import pl.project.e_invoice.model.listeners.InvoiceListener;
import pl.project.e_invoice.service.InvoiceService;

import java.util.List;
import java.util.Objects;

@Controller
@FxmlView("ListInvoicesStage.fxml")
@RequiredArgsConstructor
public class ListInvoiceStageController {
    private final InvoiceService invoiceService;
    private final UpdateInvoiceStageController updateInvoiceStageController;
    @FXML
    protected TableView<Invoice> invoiceTableView = new TableView<>();
    @FXML
    protected TableColumn<?, String> invoiceIdTableColumn;
    @FXML
    protected TableColumn<Invoice, String> invoiceTypeTableColumn;
    @FXML
    protected TableColumn<Invoice, String> invoiceStatusTableColumn;
    @FXML
    protected TableColumn<Invoice, String> buyerNipTableColumn;
    @FXML
    protected TableColumn<Invoice, String> sellerNipTableColumn;
    @FXML
    protected TableColumn<Invoice, String> amountTableColumn;
    @FXML
    protected ScrollBar scroll;
    @FXML
    private Button closeButton;
    @FXML
    private Button editButton;
    @FXML
    private SplitPane listSplitPane;
    @Autowired
    private InvoiceListener invoiceListener;
    private Stage stage;
    @Autowired
    private FxWeaver fxWeaver;
    private FxControllerAndView<UpdateInvoiceStageController, SplitPane> updateStageControllerSplitPane;
    private ObservableList<Invoice> data;
    private boolean isWindowOpen = false;
    private String title = "";
    @Value("${bundle-properties.noData}")
    private String noData;


    @FXML
    public void initialize() {
        if (!isWindowOpen) {
            this.stage = new Stage();
            stage.setTitle(title);
            this.stage.getIcons().add(new Image(Objects.requireNonNull(PrimaryStageInitializer.class.getResourceAsStream("/logo.png"))));
            stage.setScene(new Scene(listSplitPane));
            isWindowOpen = true;
        }
        displayInvoices();
        setScrollBar(invoiceTableView);
        addEventHandlers();
    }

    private void addEventHandlers() {
        closeButton.setOnAction(actionEvent -> stage.hide());
        editButton.setOnAction(event -> {
            updateStageControllerSplitPane = new LazyFxControllerAndView(() -> this.fxWeaver.load(updateInvoiceStageController.getClass()));
            Invoice currentInvoice = invoiceTableView.getSelectionModel().getSelectedItem();
            updateStageControllerSplitPane.getController().openStage(currentInvoice);
        });
        invoiceListener.addListener((invoice, notifyType) -> {
            if (notifyType == DatabaseListenerType.CREATE) {
                data.add(invoice);
            } else if (notifyType == DatabaseListenerType.DELETE) {
                data.remove(invoice);
            } else {
                throw new IllegalStateException("Not supported notifyType");
            }
            invoiceTableView.refresh();
        });
    }

    public void displayInvoices() {
        List<Invoice> invoices = invoiceService.findAll();
        this.data = FXCollections.observableList(invoices);

        setColumnForTableView();
        invoiceTableView.setFixedCellSize(60);
        invoiceTableView.setItems(data);

        scroll.setMax(data.size());
    }

    public void setScrollBar(TableView<?> tableView) {
        scroll.setMax(tableView.getItems().size());
        scroll.setMin(0);
        scroll.valueProperty().addListener((observableValue, number, t1) -> tableView.scrollTo(t1.intValue()));
    }

    public void setColumnForTableView() {
        invoiceIdTableColumn.setCellValueFactory((new PropertyValueFactory<>("id")));
        invoiceStatusTableColumn.setCellValueFactory(data -> data.getValue().getInvoiceStatus() != null
                ? new SimpleStringProperty(data.getValue().getInvoiceStatus().getLabel())
                : new SimpleStringProperty(noData));
        invoiceTypeTableColumn.setCellValueFactory(data -> data.getValue().getInvoiceType() != null
                ? new SimpleStringProperty(data.getValue().getInvoiceType().getLabel())
                : new SimpleStringProperty(noData));
        buyerNipTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBuyer().getNip()));
        sellerNipTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSeller().getNip()));
        amountTableColumn.setCellValueFactory(data -> data.getValue().getValue() != null
                ? new SimpleStringProperty(data.getValue().getValue().toString())
                : new SimpleStringProperty(noData));
    }

    public void openStage() {
        stage.show();
    }
}
