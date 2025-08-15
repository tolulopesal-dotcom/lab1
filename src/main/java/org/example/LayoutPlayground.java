package org.example;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import java.io.InputStream;
import java.util.Objects;

public class LayoutPlayground extends BorderPane {

    public LayoutPlayground() {
        setPadding(new Insets(10));

        // Top bar with label, text field, and add button
        Label lbl = new Label("Enter text:");
        TextField tf = new TextField();
        Button add = new Button("Add");
        HBox top = new HBox(10, lbl, tf, add);
        top.setAlignment(Pos.CENTER_LEFT);
        top.setPadding(new Insets(5));
        setTop(top);

        // Center area: image + font demo, and list
        ListView<String> list = new ListView<>();

        Label imgHeader = new Label("Image & Font Demo");
        // Try loading a font from resources
        try (InputStream fontIs = getClass().getResourceAsStream("/fonts/Quicksand-SemiBold.ttf")) {
            if (fontIs != null) {
                Font f = Font.loadFont(fontIs, 22);
                if (f != null) imgHeader.setFont(f);
            } else {
                imgHeader.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
            }
        } catch (Exception ignore) {
            imgHeader.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        }

        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(260);

        Slider imgWidth = new Slider(120, 600, 260);
        imageView.fitWidthProperty().bind(imgWidth.valueProperty());

        Button loadImg = new Button("Load sample image");
        loadImg.setOnAction(e -> {
            try (InputStream is = getClass().getResourceAsStream("/images/javaimage.png")) {
                if (is == null) throw new IllegalArgumentException("images/javaimage.png not found in resources.");
                imageView.setImage(new Image(is));
            } catch (Exception ex) {
                new Alert(Alert.AlertType.ERROR, "Failed to load image: " + ex.getMessage()).showAndWait();
            }
        });

        VBox centerBox = new VBox(10, imgHeader, loadImg, imgWidth, imageView, new Label("Items:"), list);
        centerBox.setPadding(new Insets(10));
        setCenter(centerBox);

        add.setOnAction(e -> {
            if (!tf.getText().isBlank()) {
                list.getItems().add(tf.getText().trim());
                tf.clear();
            }
        });

        // Right side: theme choice and wrap toggle
        ComboBox<String> themes = new ComboBox<>();
        themes.getItems().addAll("Light", "Dark");
        themes.setValue("Light");

        CheckBox wrap = new CheckBox("Wrap demo");
        VBox right = new VBox(10, new Label("Theme:"), themes, wrap);
        right.setPadding(new Insets(10));
        right.setPrefWidth(200);
        setRight(right);

        // Left side: flow pane buttons
        FlowPaneDemo flow = new FlowPaneDemo();
        setLeft(flow);
        wrap.selectedProperty().addListener((obs, was, is) ->
                flow.setWrapLength(is ? 120 : Double.MAX_VALUE));

        // Change theme when combo box changes
        themes.valueProperty().addListener((o, oldV, newV) -> {
            if (getScene() == null) return;
            getScene().getStylesheets().clear();
            String cssName = Objects.equals(newV, "Dark") ? "/dark.css" : "/light.css";
            var css = getClass().getResource(cssName);
            if (css != null) {
                getScene().getStylesheets().add(css.toExternalForm());
            }
        });
    }

    // FlowPane with buttons for the wrap demo
    private static class FlowPaneDemo extends VBox {
        private final FlowPane flow = new FlowPane(8, 8);

        FlowPaneDemo() {
            setPadding(new Insets(10));
            flow.getChildren().addAll(
                    new Button("One"), new Button("Two"), new Button("Three"),
                    new Button("Four"), new Button("Five"), new Button("Six"));
            getChildren().addAll(new Label("FlowPane (auto-wrap)"), flow);
        }

        void setWrapLength(double len) {
            flow.setPrefWrapLength(len);
        }
    }
}
