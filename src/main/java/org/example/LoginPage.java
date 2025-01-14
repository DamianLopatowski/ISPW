package org.example;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginPage extends Application {

    private static boolean isOffline = false; // Flag per determinare se la modalità è offline

    public static void main(String[] args) {
        // Avvia l'applicazione JavaFX
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Crea e configura il NavigationHandler
        NavigationHandler navigationHandler = NavigationHandler.getInstance();

        // Crea il PageController
        PageController pageController = new PageController(primaryStage);

        // Crea e registra le pagine
        GestionePage gestionePage = new GestionePage(pageController);
        GestisciProdottiPage gestisciProdottiPage = new GestisciProdottiPage(pageController);

        navigationHandler.registerPage("GestionePage", gestionePage);
        navigationHandler.registerPage("GestisciProdottiPage", gestisciProdottiPage);

        // Configura le pagine nel PageController
        pageController.setPages(gestionePage, gestisciProdottiPage);

        // Mostra la pagina principale
        Scene loginScene = createLoginScene(primaryStage, pageController);
        primaryStage.setTitle("Login");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }



    public Scene createLoginScene(Stage primaryStage, PageController pageController) {
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

        loginButton.setOnAction(e -> {
            boolean isOnlineLogin = onlineLogin.isSelected();
            handleLogin(usernameField.getText(), passwordField.getText(), isOnlineLogin, primaryStage, pageController);
        });

        VBox layout = new VBox(15, new Label("Username:"), usernameField, new Label("Password:"), passwordField, onlineLogin, offlineLogin, loginButton);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 20;");

        return new Scene(layout, 400, 300);
    }


    private void handleLogin(String username, String password, boolean isOnlineLogin, Stage primaryStage, PageController pageController) {
        if (isOnlineLogin) {
            handleOnlineLogin(username, password, primaryStage, pageController);
        } else {
            handleOfflineLogin(username, password, primaryStage, pageController);
        }
    }


    private void handleOnlineLogin(String username, String password, Stage primaryStage, PageController pageController) {
        if (!InternetCheck.isConnected()) {
            showAlert("Connessione non disponibile", "Internet non disponibile per il login con il database.");
            return;
        }

        if (DatabaseUtils.verifyCredentials(username, password)) {
            setOffline(false); // Modalità online
            showMainPage(primaryStage, pageController);
        } else {
            showAlert("Login fallito", "Credenziali non corrette.");
        }
    }


    private void handleOfflineLogin(String username, String password, Stage primaryStage, PageController pageController) {
        setOffline(true); // Modalità offline
        if ("admin".equals(username) && "password123".equals(password)) {
            showMainPage(primaryStage, pageController);
        } else {
            showAlert("Login fallito", "Credenziali non corrette.");
        }
    }


    public void showMainPage(Stage primaryStage, PageController pageController) {
        Button gestisciProdottiButton = new Button("Gestisci Prodotti");
        gestisciProdottiButton.setOnAction(e -> {
            System.out.println("Tentativo di navigare verso GestisciProdottiPage...");
            pageController.showGestisciProdottiPage();
        });
        Button soglieAvvisiButton = new Button("Soglie Avvisi");
        Button gestioneSchedeButton = new Button("Gestione Schede");
        Button gestisciOrdiniButton = new Button("Gestisci Ordini");
        Button logoutButton = new Button("Logout");

        // Impostazione dell'azione per il logout
        logoutButton.setOnAction(e -> {
            // Passa il PageController configurato alla scena di login
            Scene loginScene = createLoginScene(primaryStage, pageController);
            primaryStage.setScene(loginScene);
        });

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