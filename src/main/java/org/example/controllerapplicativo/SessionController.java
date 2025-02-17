package org.example.controllerapplicativo;

import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Parent;
import java.util.logging.Logger;
import org.example.ApplicationContext;
import org.example.dao.GestoreDAOImpl;
import org.example.service.NavigationService;
import org.example.view.LoginOfflineView;
import org.example.view.LoginOnlineView;
import org.example.view.View;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.Scene;

public class SessionController {
    private static final Logger LOGGER = Logger.getLogger(SessionController.class.getName());

    private final Stage stage;
    private final ApplicationContext context;
    private final NavigationService navigationService;
    private boolean isOnlineMode;
    private boolean isInterfaccia1;

    protected static boolean isOnlineModeStatic = true;   // ✅ Salviamo lo stato globale

    public SessionController(Stage stage, boolean isOnlineMode, NavigationService navigationService) {
        this.stage = stage;
        this.isOnlineMode = isOnlineMode;
        this.navigationService = navigationService;
        isOnlineModeStatic = isOnlineMode;  // ✅ Salviamo lo stato globale
        View mainView = new View();
        this.context = new ApplicationContext(stage, mainView);
        initializeView();
    }


    private void initializeView() {
        View view = context.getMainView();
        if (LOGGER.isLoggable(java.util.logging.Level.INFO)) {
            LOGGER.info(String.format("✅ Modalità al riavvio: %s", isOnlineMode ? "ONLINE" : "OFFLINE"));
        }

        view.getLoginButton().setOnAction(event -> {
            if (view.getInterfaccia1Option().isSelected()) {
                isInterfaccia1 = true;
            } else if (view.getInterfaccia2Option().isSelected()) {
                isInterfaccia1 = false;
            } else {
                LOGGER.warning("❌ Nessuna interfaccia selezionata.");
                return;
            }

            LOGGER.info("🔄 Avvio login per " + (isOnlineMode ? "modalità ONLINE" : "modalità OFFLINE"));
            startLogin();
        });

        stage.setScene(new Scene(view.getRoot(), 400, 300));
        stage.setTitle("Selezione Interfaccia");
        stage.show();
    }

    private void navigateToGestione() {
        LOGGER.info("🔄 Navigazione a GestioneProdotti...");
        Parent gestioneView = navigationService.navigateToGestioneView(isOnlineMode, isInterfaccia1);
        if (gestioneView != null) {
            LOGGER.info("✅ Cambio scena a GestioneProdotti...");
            stage.setScene(new Scene(gestioneView, 600, 400));
            stage.setTitle("Gestione Prodotti - " + (isInterfaccia1 ? "Interfaccia 1" : "Interfaccia 2"));
        } else {
            LOGGER.warning("❌ Errore: gestioneView è NULL!");
        }
    }

    private void startLogin() {
        LOGGER.info("🔑 Avvio della schermata di login...");

        GestoreDAOImpl gestoreDAO = new GestoreDAOImpl();
        AuthController authController = new AuthController(gestoreDAO);

        if (!isOnlineMode) {
            LOGGER.info("🟢 Modalità offline, nessuna ricarica credenziali.");
        } else {
            LOGGER.info("🔄 Modalità online, ricarico le credenziali online...");
            gestoreDAO.refreshOnlineCredentials();
        }

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

        if (loginButton != null) {
            loginButton.setOnAction(null);
            loginButton.setOnAction(event -> {
                String username = usernameField.getText();
                String password = passwordField.getText();
                LOGGER.info(String.format("🔑 Tentativo di login con username: %s", username));
                boolean loginSuccess = authController.handleLogin(username, password, !isOnlineMode);

                if (loginSuccess) {
                    LOGGER.info("✅ Login riuscito!");
                    navigateToGestione();
                } else {
                    LOGGER.warning("❌ Credenziali errate.");
                }
            });
        } else {
            LOGGER.warning("⚠️ Bottone di login è NULL! Verifica la creazione della View.");
        }

        if (loginRoot != null) {
            stage.setScene(new Scene(new VBox(loginRoot), 400, 300));
            stage.setTitle("Login - " + (isInterfaccia1 ? "Interfaccia 1" : "Interfaccia 2"));
            LOGGER.info("✅ Scena aggiornata con la schermata di login.");
        } else {
            LOGGER.warning("❌ Errore: loginRoot è NULL!");
        }
    }

    public static boolean getIsOnlineModeStatic() {
        return isOnlineModeStatic;
    }

    public void resetSession() {
        LOGGER.info("🔄 Reset della sessione dopo il logout...");
        new SessionController(stage, isOnlineMode, navigationService);
    }
}
