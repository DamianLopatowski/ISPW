package org.example.controllergrafici;

import javafx.stage.Stage;
import org.example.controllerapplicativo.AuthController;
import org.example.dao.GestoreDAOImpl;
import org.example.view.GestioneOnlineView;
import org.example.view.GestioneOfflineView;
import org.example.service.NavigationService;
import java.util.logging.Logger;

public class GestioneController {
    private final Stage stage;
    private final boolean isOfflineMode;
    private final NavigationService navigationService;
    private final GestioneOnlineView onlineView;
    private final GestioneOfflineView offlineView;
    private static final Logger LOGGER = Logger.getLogger(GestioneController.class.getName());
    private final AuthController authController; // 🔹 Dipendenza iniettata

    // 🔹 Modifica: Ora riceve un'istanza di AuthController
    public GestioneController(Stage stage, boolean isOfflineMode, NavigationService navigationService, AuthController authController) {
        this.stage = stage;
        this.isOfflineMode = isOfflineMode;
        this.navigationService = navigationService;
        this.authController = authController; // Salva l'istanza

        this.onlineView = isOfflineMode ? null : new GestioneOnlineView();
        this.offlineView = isOfflineMode ? new GestioneOfflineView() : null;

        setupHandlers();
    }

    private void setupHandlers() {
        if (!isOfflineMode) {
            onlineView.getGestioneProdottiButton().setOnAction(event -> handleGestione("Prodotti"));
            onlineView.getGestioneSogliaButton().setOnAction(event -> handleGestione("Soglia"));
            onlineView.getGestioneSpedizioniButton().setOnAction(event -> handleGestione("Spedizioni"));
            onlineView.getLogoutButton().setOnAction(event -> handleLogout());
        } else {
            offlineView.getConfermaButton().setOnAction(event -> {
                String selectedOption = offlineView.getMenuTendina().getValue();
                if (selectedOption != null) {
                    handleGestione(selectedOption);
                }
            });
            offlineView.getLogoutButton().setOnAction(event -> handleLogout());
        }
    }

    private void handleLogout() {
        authController.logout(); // Ora usa l'istanza corretta
        navigationService.navigateToMainView(); // Torna alla pagina principale
    }

    private void handleGestione(String sezione) {
        LOGGER.log(java.util.logging.Level.INFO, "Navigazione a: {0}", sezione);
    }

    public javafx.scene.Parent getRootView() {
        return isOfflineMode ? offlineView.getRoot() : onlineView.getRoot();
    }
}
