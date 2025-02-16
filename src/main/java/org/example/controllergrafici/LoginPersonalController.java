package org.example.controllergrafici;

import javafx.stage.Stage;
import javafx.scene.Parent;
import org.example.view.LoginOnlineView;
import org.example.view.LoginOfflineView;
import org.example.controllerapplicativo.AuthController;
import org.example.service.NavigationService;
import org.example.view.View;

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

    public LoginPersonalController(Stage stage, View mainView, NavigationService navigationService) {
        this.stage = stage;  // Salva il riferimento allo Stage
        this.mainView = mainView;
        this.authController = new AuthController();
        this.navigationService = navigationService;

        updateLoginView();
    }

    private void updateLoginView() {
        boolean isOfflineMode = mainView.getOfflineOption().isSelected();

        if (isOfflineMode) {
            this.offlineView = new LoginOfflineView();
            this.onlineView = null;
            stage.getScene().setRoot(offlineView.getRoot()); // Usare stage qui ora funziona
        } else {
            this.onlineView = new LoginOnlineView();
            this.offlineView = null;
            stage.getScene().setRoot(onlineView.getRoot()); // Usare stage qui ora funziona
        }

        setupHandlers();
    }

    private void setupHandlers() {
        boolean isOfflineMode = mainView.getOfflineOption().isSelected();

        if (!isOfflineMode) {
            onlineView.getLoginButton().setOnAction(event -> handleLogin(
                    onlineView.getUsernameField().getText(),
                    onlineView.getPasswordField().getText(),
                    false // Modalità online
            ));
        } else {
            offlineView.getLoginButton().setOnAction(event -> handleLogin(
                    offlineView.getUsernameField().getText(),
                    offlineView.getPasswordField().getText(),
                    true // Modalità offline
            ));
        }
    }

    private void handleLogin(String username, String password, boolean isOfflineMode) {
        boolean loginSuccess = authController.handleLogin(username, password, isOfflineMode);

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
