package org.example.controllergrafici;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Parent;
import org.example.view.LoginOnlineView;
import org.example.view.LoginOfflineView;
import org.example.controllerapplicativo.AuthController;
import org.example.dao.GestoreDAOImpl;
import org.example.service.NavigationService;
import org.example.view.View;
import org.example.controllergrafici.GestioneController;

import java.util.logging.Logger;

public class LoginPersonalController {
    private static final Logger LOGGER = Logger.getLogger(LoginPersonalController.class.getName());
    private static final String ERROR_CREDENTIALS = "Credenziali errate.";

    private final Stage stage;
    private final View mainView;
    private final AuthController authController;
    private final NavigationService navigationService;
    private LoginOnlineView onlineView;
    private LoginOfflineView offlineView;
    private boolean isOnlineMode;
    private boolean isInterfaccia1;

    private Runnable onLoginSuccess;

    public LoginPersonalController(Stage stage, View mainView, NavigationService navigationService, GestoreDAOImpl gestoreDAO, boolean isOnlineMode, boolean isInterfaccia1) {
        this.stage = stage;
        this.mainView = mainView;
        this.navigationService = navigationService;
        this.authController = new AuthController(gestoreDAO);
        this.isOnlineMode = isOnlineMode;
        this.isInterfaccia1 = isInterfaccia1;

        if (isOnlineMode) {
            this.onlineView = new LoginOnlineView(isInterfaccia1);
        } else {
            this.offlineView = new LoginOfflineView(isInterfaccia1);
        }

        setupHandlers();
    }

    public void setOnLoginSuccess(Runnable onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }

    public Runnable getOnLoginSuccess() {
        return this.onLoginSuccess;
    }

    private void updateLoginView() {
        if (isOnlineMode) {
            this.onlineView = new LoginOnlineView(isInterfaccia1);
            this.offlineView = null;
            stage.getScene().setRoot(onlineView.getRoot());
        } else {
            this.offlineView = new LoginOfflineView(isInterfaccia1);
            this.onlineView = null;
            stage.getScene().setRoot(offlineView.getRoot());
        }

        setupHandlers();
    }

    private void setupHandlers() {
        if (isOnlineMode) {
            if (onlineView.getLoginButton() == null) {
                LOGGER.warning("⚠️ Il bottone di login ONLINE è NULL! Controlla l'inizializzazione.");
            } else {
                LOGGER.info("✅ Registrazione evento per il bottone di login ONLINE.");
                onlineView.getLoginButton().setOnAction(event -> {
                    LOGGER.info("🔵 Bottone di login ONLINE premuto.");
                    handleLogin(
                            onlineView.getUsernameField().getText(),
                            onlineView.getPasswordField().getText()
                    );
                });
            }
        } else {
            if (offlineView.getLoginButton() == null) {
                LOGGER.warning("⚠️ Il bottone di login OFFLINE è NULL! Controlla l'inizializzazione.");
            } else {
                LOGGER.info("✅ Registrazione evento per il bottone di login OFFLINE.");
                offlineView.getLoginButton().setOnAction(event -> {
                    LOGGER.info("🟢 Bottone di login OFFLINE premuto.");
                    handleLogin(
                            offlineView.getUsernameField().getText(),
                            offlineView.getPasswordField().getText()
                    );
                });
            }
        }
    }
    private void handleLogin(String username, String password) {
        LOGGER.info("🔑 Tentativo di login con username: " + username);

        boolean loginSuccess = authController.handleLogin(username, password, !isOnlineMode);

        if (loginSuccess) {
            LOGGER.info("✅ Login riuscito! Chiamata alla callback di navigazione...");
            if (onLoginSuccess != null) {
                onLoginSuccess.run(); // CHIAMIAMO LA NAVIGAZIONE
            } else {
                LOGGER.warning("⚠️ Callback di navigazione NULL! Controlla `setOnLoginSuccess()`.");
            }
        } else {
            LOGGER.warning("❌ Login fallito: credenziali errate.");
            if (isOnlineMode) {
                onlineView.getStatusLabel().setText("❌ Credenziali errate!");
            } else {
                offlineView.getStatusLabel().setText("❌ Credenziali errate!");
            }
        }
    }



    private void navigateToGestione() {
        LOGGER.info("🔄 Entrato in navigateToGestione()...");

        Parent gestioneView = navigationService.navigateToGestioneView(isOnlineMode, isInterfaccia1);

        if (gestioneView != null) {
            LOGGER.info("✅ Cambio scena a GestioneProdotti...");
            stage.setScene(new javafx.scene.Scene(gestioneView, 600, 400));
            stage.setTitle("Gestione Prodotti - " + (isInterfaccia1 ? "Interfaccia 1" : "Interfaccia 2"));
        } else {
            LOGGER.warning("❌ Errore: gestioneView è NULL!");
        }
    }

    public Parent getLoginViewRoot() {
        if (onlineView != null) {
            return new VBox(onlineView.getRoot());  // Wrappiamo sempre in un nuovo VBox
        } else if (offlineView != null) {
            return new VBox(offlineView.getRoot());
        }
        return null;
    }
}
