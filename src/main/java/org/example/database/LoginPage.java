package org.example.database;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controller.LoginPageController;
import org.example.view.LoginPageView;

public class LoginPage extends Application {

    private static boolean isOffline = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        LoginPageView view = new LoginPageView();
        LoginPageController controller = new LoginPageController(view);

        Scene loginScene = view.createLoginScene(primaryStage, controller);
        primaryStage.setTitle("Login");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    public static boolean isOffline() {
        return isOffline;
    }

    public static void setOffline(boolean offline) {
        isOffline = offline;
    }
}
