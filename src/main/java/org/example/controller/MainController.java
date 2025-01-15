package org.example.controller;

import org.example.view.LoginPersonalView;
import org.example.view.View;

import java.util.logging.Logger;

public class MainController {
    private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());

    private final View mainView;
    private final LoginPersonalView loginPersonalView;

    public MainController(View mainView) {
        this.mainView = mainView;
        this.loginPersonalView = new LoginPersonalView();

        configureMainView();
    }

    private void configureMainView() {
        mainView.getLoginPersonaleButton().setOnAction(event -> {
            boolean isOfflineMode = mainView.getOfflineOption().isSelected();
            if (!isOfflineMode && !mainView.getOnlineOption().isSelected()) {
                LOGGER.warning("Seleziona una modalità prima di procedere.");
                return;
            }
            // Passa alla schermata di Login Personale
            loginPersonalView.setOfflineMode(isOfflineMode);
            mainView.getRoot().getChildren().setAll(loginPersonalView.getRoot());
        });
    }
}
