package pl.project.e_invoice.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import net.rgielen.fxweaver.core.LazyFxControllerAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@FxmlView("MainStage.fxml")
public class MainStageController extends AbstractStageController{
    @FXML
    protected Button openCreationStage;
    @FXML
    protected Button openBrowseStage;
    private FxControllerAndView<CreationInvoiceStageController, SplitPane> creationStageControllerSplitPane;
    private final FxControllerAndView<ListInvoiceStageController, SplitPane> listStageControllerSplitPane;
    private final CreationInvoiceStageController creationInvoiceController;

    @FXML
    public void initialize() {
        openCreationStage.setOnAction(event -> {
            creationStageControllerSplitPane = new LazyFxControllerAndView(() -> this.fxWeaver.load(creationInvoiceController.getClass()));
            creationStageControllerSplitPane.getController().openStage();
        });
        openBrowseStage.setOnAction(event -> listStageControllerSplitPane.getController().openStage());
    }

}
