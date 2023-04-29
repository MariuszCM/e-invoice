package pl.project.e_invoice.controller;

import javafx.scene.image.Image;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import pl.project.e_invoice.application.PrimaryStageInitializer;

import java.util.Objects;

public abstract class AbstractStageController {
    @Autowired
    protected FxWeaver fxWeaver;
    @Value("${bundle-properties.stageTitle}")
    protected String stageTitle;

    protected Image stageImage = new Image(Objects.requireNonNull(PrimaryStageInitializer.class.getResourceAsStream("/logo.png")));

    @Value("${company.nip}")
    protected String myCompanyNip;
}
