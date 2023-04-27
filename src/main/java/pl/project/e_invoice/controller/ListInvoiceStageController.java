package pl.project.e_invoice.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Controller;
import pl.project.e_invoice.model.documents.Invoice;
import pl.project.e_invoice.service.InvoiceService;

import java.util.List;

@Controller
@FxmlView("ListInvoicesStage.fxml")
@RequiredArgsConstructor
public class ListInvoiceStageController {
    private final InvoiceService invoiceService;
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
    private Stage stage;
    private boolean isWindowOpen = false;
    private String title = "";
    @FXML
    private Button closeButton;
    @FXML
    private SplitPane listSplitPane;

    @FXML
    public void initialize() {
        if (!isWindowOpen) {
            this.stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(listSplitPane));
            isWindowOpen = true;
        }
        displayInvoices();
        setScrollBar(invoiceTableView);
        closeButton.setOnAction(
                actionEvent -> stage.hide()
        );
    }

    public void displayInvoices() {
        List<Invoice> invoices = invoiceService.findAll();
        ObservableList<Invoice> data = FXCollections.observableList(invoices);

        setColumnForTableView(invoiceTableView);
        invoiceTableView.setItems(data);

        scroll.setMax(data.size());
    }

    public void setScrollBar(TableView<?> tableView) {
        scroll.setMax(tableView.getItems().size());
        scroll.setMin(0);
        scroll.valueProperty().addListener((observableValue, number, t1) -> {
            tableView.scrollTo(t1.intValue());
        });
    }

    public void setColumnForTableView(TableView<?> tableView) {
        invoiceIdTableColumn.setCellValueFactory((new PropertyValueFactory<>("id")));
        invoiceStatusTableColumn.setCellValueFactory(data ->  {
            if (data.getValue().getInvoiceStatus() != null) {
                return new SimpleStringProperty(data.getValue().getInvoiceStatus().getLabel());
            } else {
                return new SimpleStringProperty("brak danych");
            }
        });
        invoiceTypeTableColumn.setCellValueFactory(data -> {
            if (data.getValue().getInvoiceType() != null) {
                return new SimpleStringProperty(data.getValue().getInvoiceType().getLabel());
            } else {
                return new SimpleStringProperty("brak danych");
            }
        });
        buyerNipTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBuyer().getNip()));
        sellerNipTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSeller().getNip()));
        amountTableColumn.setCellValueFactory(data -> {
            if (data.getValue().getValue() != null) {
                return new SimpleStringProperty(data.getValue().getValue().toString());
            } else {
                return new SimpleStringProperty("brak danych");
            }
        });
        tableView.setFixedCellSize(60);
    }

    public void openStage() {
        stage.show();
    }
}
