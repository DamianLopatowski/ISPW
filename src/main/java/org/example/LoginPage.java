package org.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginPage extends Application implements MainPageNavigator {

    private Navigator navigator;
    private static boolean isOffline = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        navigator = new NavigationManager(primaryStage, this);
        Scene loginScene = createLoginScene(primaryStage); // Passa il parametro richiesto
        primaryStage.setTitle("Login");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }


    public Scene createLoginScene(Stage primaryStage) {
        // Creazione dei campi di input
        TextField usernameField = new TextField();
        usernameField.setPromptText("Inserisci il tuo username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Inserisci la tua password");

        // Pulsante di login
        Button loginButton = new Button("Login");

        // Gruppo di selezione per il tipo di login
        ToggleGroup loginTypeGroup = new ToggleGroup();
        RadioButton onlineLogin = new RadioButton("Login con Database");
        onlineLogin.setToggleGroup(loginTypeGroup);
        onlineLogin.setSelected(true);

        RadioButton offlineLogin = new RadioButton("Login Offline");
        offlineLogin.setToggleGroup(loginTypeGroup);

        // Disabilita l'opzione online se non c'è connessione
        if (!InternetCheck.isConnected()) {
            onlineLogin.setDisable(true);
            offlineLogin.setSelected(true);
        }

        // Azione del pulsante di login
        loginButton.setOnAction(e -> {
            if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                showAlert("Errore di validazione", "Username e password sono obbligatori.");
                return;
            }
            boolean isOnlineLogin = onlineLogin.isSelected();
            handleLogin(usernameField.getText(), passwordField.getText(), isOnlineLogin, primaryStage);
        });

        // Layout principale
        VBox layout = new VBox(15,
                new Label("Username:"), usernameField,
                new Label("Password:"), passwordField,
                onlineLogin, offlineLogin,
                loginButton
        );
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
        gestisciProdottiButton.setOnAction(e -> navigator.navigateToGestisciProdotti());

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> navigator.navigateToMainPage());

        VBox centerLayout = new VBox(15, gestisciProdottiButton, logoutButton);
        centerLayout.setAlignment(Pos.CENTER);
        centerLayout.setStyle("-fx-background-color: #f2f2f2; -fx-padding: 20;");

        Scene scene = new Scene(centerLayout, 800, 600);
        ((NavigationManager) navigator).getStage().setScene(scene);
        ((NavigationManager) navigator).getStage().show();
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
