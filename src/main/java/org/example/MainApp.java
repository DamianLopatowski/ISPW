package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.controllergrafici.MainController;
import org.example.view.View;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        View view = new View();
        ApplicationContext context = new ApplicationContext(primaryStage, view);

        // Rimosso NavigationService, ora MainController non lo richiede più
        new MainController(view, context);

        primaryStage.setScene(new javafx.scene.Scene(view.getRoot(), 400, 300));
        primaryStage.setTitle("Applicazione");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
