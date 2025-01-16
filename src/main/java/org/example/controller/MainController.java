package org.example.controller;

import javafx.stage.Stage;
import org.example.ApplicationContext;
import org.example.NetworkUtils;
import org.example.view.LoginPersonalView;
import org.example.view.View;

import java.util.logging.Logger;

public class MainController {
    private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());

    private final View mainView;

    public MainController(View mainView) {
        this.mainView = mainView;

        configureMainView();
    }

    private void configureMainView() {
        mainView.getLoginPersonaleButton().setOnAction(event -> {
            boolean isOfflineMode = mainView.getOfflineOption().isSelected();
            boolean isOnlineMode = mainView.getOnlineOption().isSelected();

            if (!isOfflineMode && !isOnlineMode) {
                LOGGER.warning("Seleziona una modalità prima di procedere.");
                return;
            }

            if (isOnlineMode && !NetworkUtils.isInternetAvailable()) {
                LOGGER.warning("Connessione Internet assente. Non è possibile entrare online.");
                return;
            }

            LOGGER.info("Modalità selezionata: " + (isOfflineMode ? "Offline" : "Online"));

            Stage stage = ApplicationContext.getInstance().getStage();
            LoginPersonalView newLoginView = new LoginPersonalView();
            LoginPersonalController loginController = new LoginPersonalController(newLoginView, isOfflineMode);
            stage.getScene().setRoot(newLoginView.getRoot());
        });
    }
}