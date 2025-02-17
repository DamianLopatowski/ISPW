package org.example.controllergrafici;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Parent;
import org.example.view.LoginOnlineView;
import org.example.view.LoginOfflineView;
import org.example.controllerapplicativo.AuthController;
import org.example.dao.GestoreDAOImpl;

import java.util.logging.Logger;

public class LoginPersonalController {
    private static final Logger LOGGER = Logger.getLogger(LoginPersonalController.class.getName());

    private final Stage stage;
    private final AuthController authController;
    private LoginOnlineView onlineView;
    private LoginOfflineView offlineView;
    private boolean isOnlineMode;

    private Runnable onLoginSuccess;

    public LoginPersonalController(Stage stage, GestoreDAOImpl gestoreDAO, boolean isOnlineMode, boolean isInterfaccia1) {
        this.stage = stage;
        this.authController = new AuthController(gestoreDAO);
        this.isOnlineMode = isOnlineMode;

        if (isOnlineMode) {
            this.onlineView = new LoginOnlineView();
        } else {
            this.offlineView = new LoginOfflineView();
        }

        setupHandlers();
    }

    public void setOnLoginSuccess(Runnable onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }

    public Runnable getOnLoginSuccess() {
        return this.onLoginSuccess;
    }

    public void updateLoginView() {
        if (isOnlineMode) {
            this.onlineView = new LoginOnlineView();
            this.offlineView = null;
            LOGGER.info("🔄 Ricreata nuova LoginOnlineView.");
        } else {
            this.offlineView = new LoginOfflineView();
            this.onlineView = null;
            LOGGER.info("🔄 Ricreata nuova LoginOfflineView.");
        }
        stage.getScene().setRoot(getLoginViewRoot());
        setupHandlers(); // Riassegna il listener del bottone
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
        if (LOGGER.isLoggable(java.util.logging.Level.INFO)) {
            LOGGER.info(String.format("🔑 Tentativo di login con username: %s", username));
        }
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

    public Parent getLoginViewRoot() {
        if (onlineView != null) {
            return new VBox(onlineView.getRoot());  // Wrappiamo sempre in un nuovo VBox
        } else if (offlineView != null) {
            return new VBox(offlineView.getRoot());
        }
        return null;
    }
}
