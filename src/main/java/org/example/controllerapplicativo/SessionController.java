package org.example.controllerapplicativo;

import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Parent;


import java.util.logging.Logger;
import java.util.logging.Level;
import org.example.ApplicationContext;
import org.example.controllergrafici.LoginPersonalController;
import org.example.dao.GestoreDAOImpl;
import org.example.service.NavigationService;
import org.example.view.LoginOfflineView;
import org.example.view.LoginOnlineView;
import org.example.view.View;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;


public class SessionController {
    private static final Logger LOGGER = Logger.getLogger(SessionController.class.getName()); // Aggiunto LOGGER

    private final Stage stage;
    private final ApplicationContext context;
    private final NavigationService navigationService;
    private boolean isOnlineMode;
    private boolean isInterfaccia1;

    public SessionController(Stage stage, boolean isOnlineMode) {
        this.stage = stage;
        this.isOnlineMode = isOnlineMode;
        View mainView = new View();
        this.context = new ApplicationContext(stage, mainView);
        this.navigationService = new org.example.controllerapplicativo.NavigationController(stage, context);
        initializeView();
    }


    private void initializeView() {
        View view = context.getMainView();

        view.getLoginButton().setOnAction(event -> {
            if (view.getInterfaccia1Option().isSelected()) {
                isInterfaccia1 = true;
            } else if (view.getInterfaccia2Option().isSelected()) {
                isInterfaccia1 = false;
            } else {
                return;
            }

            startLogin();
        });

        stage.setScene(new javafx.scene.Scene(view.getRoot(), 400, 300));
        stage.setTitle("Selezione Interfaccia");
        stage.show();
    }

    private void navigateToGestione() {
        LOGGER.info("🔄 Navigazione a GestioneProdotti...");

        Parent gestioneView = navigationService.navigateToGestioneView(isOnlineMode, isInterfaccia1);

        if (gestioneView != null) {
            LOGGER.info("✅ Cambio scena a GestioneProdotti...");
            stage.setScene(new javafx.scene.Scene(gestioneView, 600, 400));
            stage.setTitle("Gestione Prodotti - " + (isInterfaccia1 ? "Interfaccia 1" : "Interfaccia 2"));
        } else {
            LOGGER.warning("❌ Errore: gestioneView è NULL!");
        }
    }

    private void startLogin() {
        GestoreDAOImpl gestoreDAO = new GestoreDAOImpl();

        // Creiamo la vista direttamente
        Parent loginRoot;
        Button loginButton;
        TextField usernameField;
        PasswordField passwordField;

        if (isInterfaccia1) {
            LoginOnlineView loginView = new LoginOnlineView(isInterfaccia1);
            loginRoot = loginView.getRoot();
            loginButton = loginView.getLoginButton();
            usernameField = loginView.getUsernameField();
            passwordField = loginView.getPasswordField();
        } else {
            LoginOfflineView loginView = new LoginOfflineView(isInterfaccia1);
            loginRoot = loginView.getRoot();
            loginButton = loginView.getLoginButton();
            usernameField = loginView.getUsernameField();
            passwordField = loginView.getPasswordField();
        }

        // FORZIAMO IL LOGIN QUI INVECE CHE IN `LoginPersonalController`
        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            AuthController authController = new AuthController(gestoreDAO);
            boolean loginSuccess = authController.handleLogin(username, password, !isOnlineMode);

            if (loginSuccess) {
                navigateToGestione();
            } else {
                System.out.println("❌ Credenziali errate.");
            }
        });

        // Cambio scena al login
        if (loginRoot != null) {
            stage.setScene(new Scene(new VBox(loginRoot), 400, 300));
            stage.setTitle("Login - " + (isInterfaccia1 ? "Interfaccia 1" : "Interfaccia 2"));
        }
    }
}
