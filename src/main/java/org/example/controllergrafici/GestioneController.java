package org.example.controllergrafici;

import javafx.scene.Parent;
import javafx.stage.Stage;
import org.example.controllerapplicativo.AuthController;
import org.example.view.Gestione2View;
import org.example.view.Gestione1View;
import org.example.service.NavigationService;
import java.util.logging.Logger;

public class GestioneController {
    private final Stage stage;
    private final boolean isInterfaccia1;
    private final NavigationService navigationService;
    private final Gestione2View onlineView;
    private final Gestione1View offlineView;
    private static final Logger LOGGER = Logger.getLogger(GestioneController.class.getName());
    private final AuthController authController;

    public GestioneController(Stage stage, boolean isInterfaccia1, NavigationService navigationService, AuthController authController) {
        this.stage = stage;
        this.isInterfaccia1 = isInterfaccia1;
        this.navigationService = navigationService;
        this.authController = authController;

        this.onlineView = new Gestione2View();
        this.offlineView = new Gestione1View();

        setupHandlers();
    }


    private void setupHandlers() {
        if (isInterfaccia1) {
            setupOnlineHandlers();
        } else {
            setupOfflineHandlers();
        }
    }

    private void setupOnlineHandlers() {
        onlineView.getGestioneProdottiButton().setOnAction(event -> handleGestione("Prodotti"));
        onlineView.getGestioneSogliaButton().setOnAction(event -> handleGestione("Soglia"));
        onlineView.getGestioneSpedizioniButton().setOnAction(event -> handleGestione("Spedizioni"));
        onlineView.getLogoutButton().setOnAction(event -> handleLogout());
    }
    private void setupOfflineHandlers() {
        offlineView.getConfermaButton().setOnAction(event -> {
            String selectedOption = offlineView.getMenuTendina().getValue();
            if (selectedOption != null) {
                handleGestione(selectedOption);
            }
        });
        offlineView.getLogoutButton().setOnAction(event -> handleLogout());
    }
    private void handleLogout() {
        LOGGER.info("🔄 Logout in corso...");
        authController.logout(navigationService); // ✅ Passiamo NavigationService
    }

    private void handleGestione(String sezione) {
        LOGGER.log(java.util.logging.Level.INFO, "Navigazione a: {0}", sezione);
    }

    public Parent getRootView() {
        LOGGER.info("📦 Restituzione root view di GestioneProdotto...");

        Parent root;
        if (isInterfaccia1) {
            root = onlineView.getRoot();
        } else {
            root = offlineView.getRoot();
        }

        if (root == null) {
            LOGGER.warning("❌ Errore: La vista di GestioneProdotto è NULL!");
        }

        return root;
    }
}
