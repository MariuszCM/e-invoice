package pl.project.e_invoice.application;

import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.project.e_invoice.controller.CreationStageController;

@Component
public class PrimaryStageInitializer implements ApplicationListener<StageReadyEvent> {
    @Value("${mainStageTitle}")
    private String title;

    private final FxWeaver fxWeaver;

    @Autowired
    public PrimaryStageInitializer(FxWeaver fxWeaver) {
        this.fxWeaver = fxWeaver;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        Stage stage = event.stage;
        //TODO
        Scene scene = new Scene(fxWeaver.loadView(CreationStageController.class));

        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}
