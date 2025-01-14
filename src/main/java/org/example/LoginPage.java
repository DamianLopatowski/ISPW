package org.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginPage extends Application implements MainPageNavigator {

    private NavigationManager navigationManager;
    private static boolean isOffline = false; // Flag per determinare se la modalità è offline

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        navigationManager = new NavigationManager(primaryStage, this); // Passa il riferimento a sé stesso
        Scene loginScene = createLoginScene(primaryStage);
        primaryStage.setTitle("Login");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    public Scene createLoginScene(Stage primaryStage) {
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        ToggleGroup loginTypeGroup = new ToggleGroup();
        RadioButton onlineLogin = new RadioButton("Login con Database");
        onlineLogin.setToggleGroup(loginTypeGroup);
        onlineLogin.setSelected(true);
        RadioButton offlineLogin = new RadioButton("Login Offline");
        offlineLogin.setToggleGroup(loginTypeGroup);

        if (!InternetCheck.isConnected()) {
            onlineLogin.setDisable(true);
            offlineLogin.setSelected(true);
        }

        loginButton.setOnAction(e -> handleLogin(usernameField.getText(), passwordField.getText(), onlineLogin.isSelected(), primaryStage));

        VBox layout = new VBox(15, new Label("Username:"), usernameField, new Label("Password:"), passwordField, onlineLogin, offlineLogin, loginButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #f2f2f2; -fx-padding: 20;");
        return new Scene(layout, 400, 300);
    }

    private void handleLogin(String username, String password, boolean isOnlineLogin, Stage primaryStage) {
        if (isOnlineLogin) {
            handleOnlineLogin(username, password, primaryStage);
        } else {
            handleOfflineLogin(username, password, primaryStage);
        }
    }

    private void handleOnlineLogin(String username, String password, Stage primaryStage) {
        if (!InternetCheck.isConnected()) {
            showAlert("Connessione non disponibile", "Internet non disponibile per il login con il database.");
            return;
        }

        if (DatabaseUtils.verifyCredentials(username, password)) {
            setOffline(false);
            showMainPage();
        } else {
            showAlert("Login fallito", "Credenziali non corrette.");
        }
    }

    private void handleOfflineLogin(String username, String password, Stage primaryStage) {
        setOffline(true);
        if ("admin".equals(username) && "password123".equals(password)) {
            showMainPage();
        } else {
            showAlert("Login fallito", "Credenziali non corrette.");
        }
    }

    @Override
    public void showMainPage() {
        Button gestisciProdottiButton = new Button("Gestisci Prodotti");
        gestisciProdottiButton.setOnAction(e -> openGestisciProdottiPage(navigationManager.getStage()));

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> navigationManager.getStage().setScene(createLoginScene(navigationManager.getStage())));

        VBox centerLayout = new VBox(15, gestisciProdottiButton);
        centerLayout.setAlignment(Pos.CENTER);
        centerLayout.setStyle("-fx-background-color: #f2f2f2; -fx-padding: 20;");

        Scene scene = new Scene(centerLayout, 800, 600);
        navigationManager.getStage().setScene(scene);
        navigationManager.getStage().show();
    }

    private void openGestisciProdottiPage(Stage primaryStage) {
        GestisciProdottiPage gestisciProdottiPage = new GestisciProdottiPage();
        gestisciProdottiPage.start(primaryStage, this); // Passa il riferimento di `this` come MainPageNavigator
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static boolean isOffline() {
        return isOffline;
    }

    private static void setOffline(boolean offline) {
        isOffline = offline;
    }
}
