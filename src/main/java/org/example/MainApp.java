package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.controllergrafici.MainController;
import org.example.controllerapplicativo.NavigationController;
import org.example.view.View;
import org.example.service.NavigationService;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        View view = new View();
        ApplicationContext context = new ApplicationContext(primaryStage, view);
        NavigationService navigationService = new NavigationController(primaryStage, context);

        // Ora MainController riceve il parametro NavigationService
        new MainController(view, context, navigationService);

        primaryStage.setScene(new javafx.scene.Scene(view.getRoot(), 400, 300));
        primaryStage.setTitle("Applicazione");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
