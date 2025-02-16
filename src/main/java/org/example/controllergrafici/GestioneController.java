package org.example.controllergrafici;

import javafx.stage.Stage;
import org.example.ApplicationContext;
import org.example.view.GestioneOnlineView;
import org.example.view.GestioneOfflineView;
import org.example.service.NavigationService;

public class GestioneController {
    private final Stage stage;
    private final boolean isOfflineMode;
    private final NavigationService navigationService;
    private final GestioneOnlineView onlineView;
    private final GestioneOfflineView offlineView;

    public GestioneController(Stage stage, boolean isOfflineMode, NavigationService navigationService) {
        this.stage = stage;
        this.isOfflineMode = isOfflineMode;
        this.navigationService = navigationService;

        this.onlineView = isOfflineMode ? null : new GestioneOnlineView();
        this.offlineView = isOfflineMode ? new GestioneOfflineView() : null;

        setupHandlers();
    }

    private void setupHandlers() {
        if (!isOfflineMode) {
            onlineView.getGestioneProdottiButton().setOnAction(event -> handleGestione("Prodotti"));
            onlineView.getGestioneSogliaButton().setOnAction(event -> handleGestione("Soglia"));
            onlineView.getGestioneSpedizioniButton().setOnAction(event -> handleGestione("Spedizioni"));
            onlineView.getLogoutButton().setOnAction(event -> navigationService.navigateToMainView());
        } else {
            offlineView.getConfermaButton().setOnAction(event -> {
                String selectedOption = offlineView.getMenuTendina().getValue();
                if (selectedOption != null) {
                    handleGestione(selectedOption);
                }
            });
            offlineView.getLogoutButton().setOnAction(event -> navigationService.navigateToMainView());
        }
    }

    private void handleGestione(String sezione) {
        System.out.println("Navigazione a: " + sezione); // Può essere sostituito con un metodo di navigazione
    }

    public javafx.scene.Parent getRootView() {
        return isOfflineMode ? offlineView.getRoot() : onlineView.getRoot();
    }
}
