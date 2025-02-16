package org.example.controllergrafici;

import javafx.stage.Stage;
import org.example.ApplicationContext;
import org.example.view.GestioneOnlineView;
import org.example.view.GestioneOfflineView;
import org.example.service.NavigationService;

import java.util.logging.Logger;

public class GestioneController {
    private static final Logger LOGGER = Logger.getLogger(GestioneController.class.getName());

    private final Stage stage;
    private final boolean isOfflineMode;
    private final NavigationService navigationService;
    private final GestioneOnlineView onlineView;
    private final GestioneOfflineView offlineView;

    public GestioneController(Stage stage, boolean isOfflineMode, ApplicationContext context, NavigationService navigationService) {
        this.stage = stage;
        this.isOfflineMode = isOfflineMode;
        this.navigationService = navigationService;

        if (isOfflineMode) {
            this.offlineView = new GestioneOfflineView();
            this.onlineView = null;
        } else {
            this.onlineView = new GestioneOnlineView();
            this.offlineView = null;
        }

        setupHandlers();
    }

    private void setupHandlers() {
        if (!isOfflineMode) {
            onlineView.getGestioneProdottiButton().setOnAction(event -> LOGGER.info("Gestione prodotti online..."));
            onlineView.getGestioneSogliaButton().setOnAction(event -> LOGGER.info("Gestione soglia online..."));
            onlineView.getGestioneSpedizioniButton().setOnAction(event -> LOGGER.info("Gestione spedizioni online..."));
            onlineView.getLogoutButton().setOnAction(event -> navigationService.navigateToMainView());
        } else {
            offlineView.getConfermaButton().setOnAction(event -> LOGGER.info("Selezioni confermate."));
            offlineView.getLogoutButton().setOnAction(event -> navigationService.navigateToMainView());
        }
    }

    public javafx.scene.Parent getRootView() {
        return isOfflineMode ? offlineView.getRoot() : onlineView.getRoot();
    }
}
