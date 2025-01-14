package org.example.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.controller.LoginPageController;

public class LoginPageView {

    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private RadioButton onlineLogin;
    private RadioButton offlineLogin;

    public Scene createLoginScene(Stage primaryStage, LoginPageController controller) {
        usernameField = new TextField();
        passwordField = new PasswordField();
        loginButton = new Button("Login");

        ToggleGroup loginTypeGroup = new ToggleGroup();
        onlineLogin = new RadioButton("Login con Database");
        onlineLogin.setToggleGroup(loginTypeGroup);
        onlineLogin.setSelected(true);
        offlineLogin = new RadioButton("Login Offline");
        offlineLogin.setToggleGroup(loginTypeGroup);

        if (!controller.isInternetAvailable()) {
            onlineLogin.setDisable(true);
            offlineLogin.setSelected(true);
        }

        loginButton.setOnAction(e -> controller.handleLogin(usernameField.getText(), passwordField.getText(), onlineLogin.isSelected(), primaryStage));

        VBox layout = new VBox(15,
                new Label("Username:"),
                usernameField,
                new Label("Password:"),
                passwordField,
                onlineLogin,
                offlineLogin,
                loginButton
        );
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f2f2f2; -fx-padding: 20;");

        return new Scene(layout, 400, 300);
    }
}
