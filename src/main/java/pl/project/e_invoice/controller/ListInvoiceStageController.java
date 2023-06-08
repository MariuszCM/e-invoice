package pl.project.e_invoice.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxmlView;
import net.rgielen.fxweaver.core.LazyFxControllerAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import pl.project.e_invoice.model.documents.Invoice;
import pl.project.e_invoice.model.listeners.DatabaseListenerType;
import pl.project.e_invoice.model.listeners.InvoiceListener;
import pl.project.e_invoice.service.InvoiceService;

import java.util.List;

@Controller
@FxmlView("ListInvoicesStage.fxml")
@RequiredArgsConstructor
public class ListInvoiceStageController extends AbstractStageController {
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
    //TODO Czy chcemy rzeczywiscie usuwac rekord czy tylko zmieniac widocznosc dla uzytkownika??
    protected Button deleteInvoiceButton;
    @FXML
    private Button closeButton;
    @FXML
    private Button editButton;
    @FXML
    private SplitPane listSplitPane;
    @Autowired
    private InvoiceListener invoiceListener;
    private Stage stage;
    private FxControllerAndView<UpdateInvoiceStageController, SplitPane> updateStageControllerSplitPane;
    private ObservableList<Invoice> data;
    private boolean isWindowOpen = false;
    @Value("${bundle-properties.noData}")
    private String noData;


    @FXML
    public void initialize() {
        if (!isWindowOpen) {
            this.stage = new Stage();
            stage.setTitle(stageTitle);
            this.stage.getIcons().add(stageImage);
            stage.setScene(new Scene(listSplitPane));
            isWindowOpen = true;
        }
        displayInvoices();
        setScrollBar(invoiceTableView);
        addEventHandlers();
    }

    private void addEventHandlers() {
        closeButton.setOnAction(actionEvent -> stage.hide());
        deleteInvoiceButton.setOnAction(actionEvent -> invoiceService.deleteDocument(invoiceTableView.getSelectionModel().getSelectedItem()));
        deleteInvoiceButton.disableProperty().bind(
                Bindings.createBooleanBinding(() -> invoiceTableView.getSelectionModel().getSelectedItem() == null, invoiceTableView.getSelectionModel().selectedItemProperty()));
        editButton.disableProperty().bind(
                Bindings.createBooleanBinding(() -> invoiceTableView.getSelectionModel().getSelectedItem() == null, invoiceTableView.getSelectionModel().selectedItemProperty()));
        editButton.setOnAction(event -> {
            updateStageControllerSplitPane = new LazyFxControllerAndView(() -> this.fxWeaver.load(updateInvoiceStageController.getClass()));
            Invoice currentInvoice = invoiceTableView.getSelectionModel().getSelectedItem();
            updateStageControllerSplitPane.getController().openStage(currentInvoice);
        });
        invoiceListener.addListener((invoice, notifyType) -> {
            if (notifyType == DatabaseListenerType.CREATE) {
                data.add(invoice);
            } else if (notifyType == DatabaseListenerType.DELETE) {
                data.removeIf(a -> a.getId().equals(invoice.getId()));
            } else if (notifyType != DatabaseListenerType.UPDATE) {
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
