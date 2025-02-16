package org.example.controllergrafici;

import javafx.stage.Stage;
import javafx.scene.Parent;
import org.example.view.LoginOnlineView;
import org.example.view.LoginOfflineView;
import org.example.controllerapplicativo.AuthController;
import org.example.service.NavigationService;

import java.util.logging.Logger;

public class LoginPersonalController {
    private static final Logger LOGGER = Logger.getLogger(LoginPersonalController.class.getName());

    private static final String ERROR_CREDENTIALS = "Credenziali errate.";

    private final Stage stage;
    private final boolean isOfflineMode;
    private final AuthController authController;
    private final NavigationService navigationService;
    private final LoginOnlineView onlineView;
    private final LoginOfflineView offlineView;

    public LoginPersonalController(Stage stage, boolean isOfflineMode, NavigationService navigationService) {
        this.stage = stage;
        this.isOfflineMode = isOfflineMode;
        this.authController = new AuthController();
        this.navigationService = navigationService;  // Ora il costruttore accetta NavigationService

        if (isOfflineMode) {
            this.offlineView = new LoginOfflineView();
            this.onlineView = null;
            stage.getScene().setRoot(offlineView.getRoot());
        } else {
            this.onlineView = new LoginOnlineView();
            this.offlineView = null;
            stage.getScene().setRoot(onlineView.getRoot());
        }

        setupHandlers();
    }

    private void setupHandlers() {
        if (!isOfflineMode) {
            onlineView.getLoginButton().setOnAction(event -> handleLogin(
                    onlineView.getUsernameField().getText(),
                    onlineView.getPasswordField().getText()
            ));
        } else {
            offlineView.getLoginButton().setOnAction(event -> handleLogin(
                    offlineView.getUsernameField().getText(),
                    offlineView.getPasswordField().getText()
            ));
        }
    }

    private void handleLogin(String username, String password) {
        boolean loginSuccess = isOfflineMode
                ? authController.handleOfflineLogin(username, password)
                : authController.handleOnlineLogin(username, password);

        if (loginSuccess) {
            LOGGER.info("Accesso riuscito!");

            Parent gestioneView = navigationService.navigateToGestioneView(isOfflineMode);
            if (gestioneView != null) {
                stage.getScene().setRoot(gestioneView);
            }
        } else {
            LOGGER.warning(ERROR_CREDENTIALS);
            if (isOfflineMode) {
                offlineView.getStatusLabel().setText(ERROR_CREDENTIALS);
            } else {
                onlineView.getStatusLabel().setText(ERROR_CREDENTIALS);
            }
        }
    }
}
