package pl.project.e_invoice.application;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.project.e_invoice.controller.CreationStageController;
import pl.project.e_invoice.controller.MainStageController;

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
        Scene scene = new Scene(fxWeaver.loadView(MainStageController.class));
        stage.getIcons().add(new Image(PrimaryStageInitializer.class.getResourceAsStream("/logo.png")));

        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}
