package org.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginPage extends Application {


    private static boolean isOffline = false; // Flag per determinare se la modalità è offline
    private PageNavigator navigator; // Campo membro per il PageNavigator

    public static void main(String[] args) {
        // Avvia l'applicazione JavaFX
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Inizializza il PageNavigator come campo membro
        navigator = new PageNavigator(primaryStage);

        // Registra tutte le pagine
        navigator.registerPage("Gestione", () -> new GestionePage(navigator));
        navigator.registerPage("GestisciProdotti", () -> new GestisciProdottiPage(navigator));

        // Mostra la scena di login
        Scene loginScene = createLoginScene(primaryStage);
        primaryStage.setTitle("Login");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    public Scene createLoginScene(Stage primaryStage) {
        // Crea i componenti dell'interfaccia di login come variabili locali
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");

        // Crea i RadioButton per scegliere il tipo di login
        ToggleGroup loginTypeGroup = new ToggleGroup();
        RadioButton onlineLogin = new RadioButton("Login con Database");
        onlineLogin.setToggleGroup(loginTypeGroup);
        onlineLogin.setSelected(true); // Imposta il login con il database come predefinito
        RadioButton offlineLogin = new RadioButton("Login Offline");
        offlineLogin.setToggleGroup(loginTypeGroup);

        // Verifica la connessione e disabilita l'opzione "Login con Database" se offline
        if (!InternetCheck.isConnected()) {
            onlineLogin.setDisable(true); // Disabilita l'opzione se non c'è connessione
            offlineLogin.setSelected(true); // Imposta automaticamente la modalità offline
        }

        // Imposta l'azione di login
        loginButton.setOnAction(e -> handleLogin(usernameField.getText(), passwordField.getText(), onlineLogin.isSelected(), primaryStage));

        // Layout migliorato
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
            setOffline(false);  // Modalità online
            showMainPage(primaryStage);
        } else {
            showAlert("Login fallito", "Credenziali non corrette.");
        }
    }

    private void handleOfflineLogin(String username, String password, Stage primaryStage) {
        setOffline(true);  // Modalità offline
        if ("admin".equals(username) && "password123".equals(password)) {
            showMainPage(primaryStage);
        } else {
            showAlert("Login fallito", "Credenziali non corrette.");
        }
    }

    public void showMainPage(Stage primaryStage) {
        // Creazione dei pulsanti per la pagina principale
        Button gestisciProdottiButton = new Button("Gestisci Prodotti");
        gestisciProdottiButton.setOnAction(e -> openGestisciProdottiPage(primaryStage));

        Button soglieAvvisiButton = new Button("Soglie Avvisi");
        Button gestioneSchedeButton = new Button("Gestione Schede");
        Button gestisciOrdiniButton = new Button("Gestisci Ordini");
        Button logoutButton = new Button("Logout");

        // Impostazione dell'azione per il logout
        logoutButton.setOnAction(e -> primaryStage.setScene(createLoginScene(primaryStage)));

        // Layout per i pulsanti centrali
        VBox centerLayout = new VBox(15, gestisciProdottiButton, soglieAvvisiButton, gestioneSchedeButton, gestisciOrdiniButton);
        centerLayout.setAlignment(Pos.CENTER);
        centerLayout.setStyle("-fx-background-color: #f2f2f2; -fx-padding: 20;");

        // Layout per il pulsante di logout in alto a destra
        HBox topLayout = new HBox(logoutButton);
        topLayout.setAlignment(Pos.TOP_RIGHT);
        topLayout.setStyle("-fx-padding: 10;");

        // Layout principale che combina il tutto
        BorderPane layout = new BorderPane();
        layout.setTop(topLayout);
        layout.setCenter(centerLayout);

        // Crea la scena
        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openGestisciProdottiPage(Stage primaryStage) {
        // Usa la pagina registrata per navigare
        navigator.navigateToPage("GestisciProdotti");
    }



    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Getter per isOffline
    public static boolean isOffline() {
        return isOffline;
    }

    // Setter per isOffline
    private static void setOffline(boolean offline) {
        isOffline = offline;
    }
}