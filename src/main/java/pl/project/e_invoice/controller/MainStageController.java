package pl.project.e_invoice.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@FxmlView("MainStage.fxml")
public class MainStageController {
    @FXML
    protected Button openCreationStage;
    @FXML
    protected Button openBrowseStage;

    private final FxControllerAndView<CreationInvoiceStageController, SplitPane> creationStageControllerSplitPane;

    @FXML
    public void initialize(){
        openCreationStage.setOnAction(event -> creationStageControllerSplitPane.getController().openStage());
        openCreationStage.setOnAction(event -> creationStageControllerSplitPane.getController().openStage());
    }

}
