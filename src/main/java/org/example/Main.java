package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("FX Mini Studio");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(650);

        // Menu at the top
        MenuBar menuBar = new MenuBar();
        Menu help = new Menu("Help");
        MenuItem about = new MenuItem("About");
        about.setOnAction(e ->
                new Alert(Alert.AlertType.INFORMATION, "FX Mini Studio\nJavaFX basics demo.").showAndWait());
        help.getItems().add(about);
        menuBar.getMenus().add(help);

        // Tabs for each section
        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.getTabs().add(new Tab("Layout Playground", new LayoutPlayground()));
        tabs.getTabs().add(new Tab("Graphics & Binding", new GraphicsBinding()));
        tabs.getTabs().add(new Tab("Clock Widget", new ClockTab()));

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        BorderPane.setMargin(menuBar, new Insets(0, 0, 5, 0));
        root.setCenter(tabs);

        // Main scene
        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) { launch(args); }
}
