package pl.project.e_invoice.application;

import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.project.e_invoice.controller.AbstractStageController;
import pl.project.e_invoice.controller.MainStageController;

@Component
public class PrimaryStageInitializer extends AbstractStageController implements ApplicationListener<StageReadyEvent> {

    @Autowired
    public PrimaryStageInitializer(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        Stage stage = event.stage;
        Scene scene = new Scene(fxWeaver.loadView(MainStageController.class));
        stage.getIcons().add(stageImage);

        stage.setTitle(stageTitle);
        stage.setScene(scene);
        stage.show();
    }
}
