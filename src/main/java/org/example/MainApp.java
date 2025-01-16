package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.controller.MainController;
import org.example.view.View;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        View view = new View();
        ApplicationContext.getInstance().setStage(primaryStage);
        ApplicationContext.getInstance().setMainView(view);

        // Controlla la connessione Internet
        boolean isInternetAvailable = NetworkUtils.isInternetAvailable();
        view.setOnlineOptionEnabled(isInternetAvailable);

        new MainController(view);
        primaryStage.setScene(new javafx.scene.Scene(view.getRoot(), 400, 300));
        primaryStage.setTitle("Applicazione");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}