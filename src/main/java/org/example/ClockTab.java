package org.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class ClockTab extends BorderPane {

    public ClockTab() {
        setPadding(new Insets(10));

        ClockPane clock = new ClockPane();
        setCenter(clock);

        // Buttons and style choice
        Button start = new Button("Start");
        Button stop  = new Button("Stop");
        ComboBox<String> style = new ComboBox<>();
        style.getItems().addAll("Light", "Dark");
        style.setValue("Light");

        // Change theme for the whole scene and the clock
        style.valueProperty().addListener((o, a, b) -> {
            clock.styleModeProperty().set(b);
            if (getScene() == null) return;
            getScene().getStylesheets().clear();
            var css = getClass().getResource(b.equals("Dark") ? "/dark.css" : "/light.css");
            if (css != null) getScene().getStylesheets().add(css.toExternalForm());
        });

        // Try to match current scene theme when the tab first shows
        sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene == null) return;
            boolean dark = newScene.getStylesheets().stream().anyMatch(s -> s.endsWith("/dark.css"));
            style.setValue(dark ? "Dark" : "Light");
            clock.styleModeProperty().set(style.getValue());
        });

        start.setOnAction(e -> clock.start());
        stop.setOnAction(e -> clock.stop());

        // Bottom bar with controls
        HBox controls = new HBox(10, start, stop, new Label("Style:"), style);
        controls.setAlignment(Pos.CENTER_LEFT);
        controls.setPadding(new Insets(10));
        setBottom(controls);
    }
}
