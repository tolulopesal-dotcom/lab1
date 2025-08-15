package org.example;

import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class GraphicsBinding extends BorderPane {

    public GraphicsBinding() {
        setPadding(new Insets(10));

        Pane canvas = new Pane();
        canvas.setMinSize(300, 300);

        // Circle in the middle
        Circle circle = new Circle(80);
        circle.setStroke(Color.BLACK);
        circle.setStyle("-fx-fill: cornflowerblue;");
        circle.centerXProperty().bind(canvas.widthProperty().divide(2));
        circle.centerYProperty().bind(canvas.heightProperty().divide(2));

        // Small dot on top to make the rotation easier to see
        Circle marker = new Circle(6, Color.DARKBLUE);
        marker.centerXProperty().bind(circle.centerXProperty());
        marker.centerYProperty().bind(Bindings.subtract(circle.centerYProperty(), circle.radiusProperty()));

        // Group both shapes so they rotate together
        Group spinner = new Group(circle, marker);
        canvas.getChildren().add(spinner);
        setCenter(canvas);

        // Slider for radius (one-way binding)
        Slider radius = new Slider(20, 200, 80);
        circle.radiusProperty().bind(radius.valueProperty());

        // Slider for rotation (one-way binding)
        Slider rotate = new Slider(0, 360, 0);
        spinner.rotateProperty().bind(rotate.valueProperty());

        // Field and label linked both ways
        TextField captionField = new TextField("Hello FX");
        Label captionLabel = new Label();
        captionLabel.textProperty().bindBidirectional(captionField.textProperty());

        // Color picker to change the circle color
        ColorPicker picker = new ColorPicker(Color.CORNFLOWERBLUE);
        picker.setOnAction(e -> {
            var c = picker.getValue();
            circle.setStyle(String.format("-fx-fill: rgb(%d,%d,%d);",
                    (int)(c.getRed()*255),(int)(c.getGreen()*255),(int)(c.getBlue()*255)));
            marker.setFill(c.darker());
        });

        // Button to make the circle pulse
        Button pulse = new Button("Pulse");
        pulse.setOnAction(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(600), spinner);
            st.setFromX(1); st.setFromY(1);
            st.setToX(1.25); st.setToY(1.25);
            st.setAutoReverse(true);
            st.setCycleCount(2);
            st.setInterpolator(Interpolator.EASE_BOTH);
            st.play();
        });

        // Layout for the controls
        GridPane controls = new GridPane();
        controls.setHgap(10);
        controls.setVgap(10);
        controls.setPadding(new Insets(10));
        controls.addRow(0, new Label("Radius:"), radius);
        controls.addRow(1, new Label("Rotate:"), rotate);
        controls.addRow(2, new Label("Caption:"), captionField, captionLabel);
        controls.addRow(3, new Label("Fill:"), picker, pulse);
        setBottom(controls);
    }
}
