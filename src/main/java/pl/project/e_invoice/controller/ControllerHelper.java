package pl.project.e_invoice.controller;


import javafx.beans.property.Property;
import javafx.scene.control.ChoiceBox;

import java.util.function.Consumer;
import java.util.function.Function;

public class ControllerHelper {
    public static <T> void addListenerToProperty(Property<T> property, Consumer<T> listener) {
        property.addListener((observable, oldValue, newValue) -> {
            listener.accept(newValue);
        });
    }

    public static <T, E> void addListenerToProperty(Property<T> property, Function<T, E> converter, Consumer<E> listener) {
        property.addListener((observable, oldValue, newValue) -> {
            var convertedVal = converter.apply(newValue);
            listener.accept(convertedVal);
        });
    }

    public static <T, E extends  Enum<E>> void addListenerToChoiceBox(ChoiceBox<T> choiceBox, Function<T, E> converter, Consumer<E> listener) {
        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            E enumValue = converter.apply(newValue);
            listener.accept(enumValue);
        });
    }
}
