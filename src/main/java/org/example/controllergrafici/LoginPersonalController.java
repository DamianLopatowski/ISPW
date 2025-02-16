package org.example.controllergrafici;

import org.example.ApplicationContext;
import org.example.view.LoginPersonalView;
import org.example.controllerapplicativo.AuthController;
import org.example.controllerapplicativo.NavigationController;
import org.example.service.NavigationService;

import java.util.logging.Logger;

public class LoginPersonalController {
    private static final Logger LOGGER = Logger.getLogger(LoginPersonalController.class.getName());

    private final LoginPersonalView view;
    private final boolean isOfflineMode;
    private final AuthController authController;
    private final NavigationService navigationService;

    public LoginPersonalController(LoginPersonalView view, boolean isOfflineMode, ApplicationContext context) {
        this.view = view;
        this.isOfflineMode = isOfflineMode;
        this.authController = new AuthController(); // Rimosso il parametro context
        this.navigationService = new NavigationController(context.getStage(), context);
        setupHandlers();
    }

    private void setupHandlers() {
        view.getLoginButton().setOnAction(event -> {
            String username = view.getUsernameField().getText();
            String password = view.getPasswordField().getText();

            boolean loginSuccess = isOfflineMode
                    ? authController.handleOfflineLogin(username, password)
                    : authController.handleOnlineLogin(username, password);

            if (loginSuccess) {
                view.getStatusLabel().setText("Accesso riuscito!");
                LOGGER.info("Accesso effettuato con successo.");
                navigationService.navigateToGestioneView();
            } else {
                view.getStatusLabel().setText("Credenziali errate.");
                LOGGER.warning("Credenziali non valide.");
            }
        });
    }
}
