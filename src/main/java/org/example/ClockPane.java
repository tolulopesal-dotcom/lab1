package org.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import java.time.LocalTime;

public class ClockPane extends Pane {

    private final IntegerProperty hour = new SimpleIntegerProperty();
    private final IntegerProperty minute = new SimpleIntegerProperty();
    private final IntegerProperty second = new SimpleIntegerProperty();
    private final BooleanProperty running = new SimpleBooleanProperty(true);
    private final StringProperty styleMode = new SimpleStringProperty("Light");

    private final Timeline timeline;

    private final Ellipse dial = new Ellipse();
    private final Line hHand = new Line();
    private final Line mHand = new Line();
    private final Line sHand = new Line();

    public ClockPane() {
        setPadding(new Insets(10));
        getChildren().addAll(dial, hHand, mHand, sHand);

        // Bind ellipse center and radii to match pane size
        dial.centerXProperty().bind(widthProperty().divide(2));
        dial.centerYProperty().bind(heightProperty().divide(2));
        dial.radiusXProperty().bind(widthProperty().multiply(0.42));
        dial.radiusYProperty().bind(heightProperty().multiply(0.42));

        // Update time every second
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> setNow()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        running.addListener((obs, old, isRunning) -> {
            if (isRunning) timeline.play(); else timeline.pause();
        });

        styleMode.addListener((o, old, mode) -> applyStyle(mode));
        applyStyle(styleMode.get());

        // Redraw on size change
        widthProperty().addListener((o, a, b) -> draw());
        heightProperty().addListener((o, a, b) -> draw());

        setNow();
    }

    private void setNow() {
        var t = LocalTime.now();
        hour.set(t.getHour());
        minute.set(t.getMinute());
        second.set(t.getSecond());
        draw();
    }

    private void draw() {
        double cx = dial.getCenterX();
        double cy = dial.getCenterY();
        double rx = dial.getRadiusX();
        double ry = dial.getRadiusY();

        // Remove old ticks
        getChildren().removeIf(n -> "tick".equals(n.getUserData()));

        // Draw ticks along the ellipse
        for (int i = 0; i < 60; i++) {
            double angle = Math.toRadians(i * 6);
            double innerX = (i % 5 == 0) ? rx * 0.82 : rx * 0.88;
            double innerY = (i % 5 == 0) ? ry * 0.82 : ry * 0.88;
            Line tick = new Line(
                    cx + innerX * Math.sin(angle), cy - innerY * Math.cos(angle),
                    cx + rx     * Math.sin(angle), cy - ry     * Math.cos(angle)
            );
            tick.setStrokeWidth(i % 5 == 0 ? 2 : 1);
            tick.setUserData("tick");
            getChildren().add(tick);
        }

        // Draw hands — scaled separately in X/Y
        double sAng = Math.toRadians(second.get() * 6);
        double mAng = Math.toRadians((minute.get() + second.get()/60.0) * 6);
        double hAng = Math.toRadians((hour.get()%12 + minute.get()/60.0) * 30);

        placeHand(sHand, cx, cy, rx * 0.90, ry * 0.90, sAng, 1.5);
        placeHand(mHand, cx, cy, rx * 0.75, ry * 0.75, mAng, 3);
        placeHand(hHand, cx, cy, rx * 0.55, ry * 0.55, hAng, 4);
    }

    private static void placeHand(Line hand, double cx, double cy,
                                  double lenX, double lenY,
                                  double ang, double width) {
        hand.setStartX(cx); hand.setStartY(cy);
        hand.setEndX(cx + lenX * Math.sin(ang));
        hand.setEndY(cy - lenY * Math.cos(ang));
        hand.setStrokeWidth(width);
    }

    private void applyStyle(String mode) {
        boolean dark = "Dark".equalsIgnoreCase(mode);
        dial.setStroke(dark ? Color.WHITE : Color.BLACK);
        dial.setFill(dark ? Color.web("#222") : Color.WHITE);
        hHand.setStroke(dark ? Color.WHITE : Color.BLACK);
        mHand.setStroke(dark ? Color.WHITE : Color.BLACK);
        sHand.setStroke(Color.RED);
        setStyle(dark ? "-fx-background-color:#111;" : "-fx-background-color:white;");
    }

    // properties
    public IntegerProperty hourProperty() { return hour; }
    public IntegerProperty minuteProperty() { return minute; }
    public IntegerProperty secondProperty() { return second; }
    public BooleanProperty runningProperty() { return running; }
    public StringProperty styleModeProperty() { return styleMode; }

    public void start() { running.set(true); }
    public void stop()  { running.set(false); }
}
