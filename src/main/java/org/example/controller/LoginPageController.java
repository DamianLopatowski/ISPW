package org.example.controller;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.database.DatabaseUtils;
import org.example.database.InternetCheck;
import org.example.database.LoginPage;
import org.example.view.LoginPageView;
import org.example.ui.GestisciProdottiPage;

public class LoginPageController {

    private final LoginPageView view;

    public LoginPageController(LoginPageView view) {
        this.view = view;
    }

    public boolean isInternetAvailable() {
        return InternetCheck.isConnected();
    }

    public void handleLogin(String username, String password, boolean isOnlineLogin, Stage primaryStage) {
        if (isOnlineLogin) {
            handleOnlineLogin(username, password, primaryStage);
        } else {
            handleOfflineLogin(username, password, primaryStage);
        }
    }

    private void handleOnlineLogin(String username, String password, Stage primaryStage) {
        if (!isInternetAvailable()) {
            showAlert("Connessione non disponibile", "Internet non disponibile per il login con il database.");
            return;
        }

        if (DatabaseUtils.verifyCredentials(username, password)) {
            setOffline(false);
            showMainPage(primaryStage);
        } else {
            showAlert("Login fallito", "Credenziali non corrette.");
        }
    }

    private void handleOfflineLogin(String username, String password, Stage primaryStage) {
        setOffline(true);
        if ("admin".equals(username) && "password123".equals(password)) {
            showMainPage(primaryStage);
        } else {
            showAlert("Login fallito", "Credenziali non corrette.");
        }
    }

    public void showMainPage(Stage primaryStage) {
        Button gestisciProdottiButton = new Button("Gestisci Prodotti");
        gestisciProdottiButton.setOnAction(e -> openGestisciProdottiPage(primaryStage));
        Button soglieAvvisiButton = new Button("Soglie Avvisi");
        Button gestioneSchedeButton = new Button("Gestione Schede");
        Button gestisciOrdiniButton = new Button("Gestisci Ordini");
        Button logoutButton = new Button("Logout");

        logoutButton.setOnAction(e -> primaryStage.setScene(view.createLoginScene(primaryStage, this)));

        VBox centerLayout = new VBox(15, gestisciProdottiButton, soglieAvvisiButton, gestioneSchedeButton, gestisciOrdiniButton);
        centerLayout.setAlignment(Pos.CENTER);
        centerLayout.setStyle("-fx-background-color: #f2f2f2; -fx-padding: 20;");

        HBox topLayout = new HBox(logoutButton);
        topLayout.setAlignment(Pos.TOP_RIGHT);
        topLayout.setStyle("-fx-padding: 10;");

        BorderPane layout = new BorderPane();
        layout.setTop(topLayout);
        layout.setCenter(centerLayout);

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openGestisciProdottiPage(Stage primaryStage) {
        GestisciProdottiPage gestisciProdottiPage = new GestisciProdottiPage();
        gestisciProdottiPage.start(primaryStage);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void setOffline(boolean offline) {
        LoginPage.setOffline(offline);
    }
}
