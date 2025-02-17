package org.example.controllerapplicativo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controllergrafici.GestioneController;
import org.example.dao.GestoreDAOImpl;
import org.example.service.NavigationService;
import org.example.view.LoginOfflineView;
import org.example.view.LoginOnlineView;
import org.example.view.View;

import java.io.IOException;
import java.util.logging.Logger;

public class NavigationController implements NavigationService {
    private static final Logger LOGGER = Logger.getLogger(NavigationController.class.getName());
    private final Stage stage;


    // Costruttore corretto con ApplicationContext
    public NavigationController(Stage stage) {
        this.stage = stage;
    }


    @Override
    public void navigateToLogin(boolean isInterfaccia1) {
        LOGGER.info("🔄 Navigazione alla schermata di Login...");
        Parent loginView;

        if (isInterfaccia1) {
            loginView = new LoginOnlineView(isInterfaccia1).getRoot();
        } else {
            loginView = new LoginOfflineView(isInterfaccia1).getRoot();
        }

        if (loginView != null) {
            stage.setScene(new Scene(loginView, 400, 300));
            stage.setTitle("Login - " + (isInterfaccia1 ? "Interfaccia 1" : "Interfaccia 2"));
        } else {
            LOGGER.warning("❌ Errore: Login View è NULL!");
        }
    }

    @Override
    public void navigateToMainView() {
        LOGGER.info("🔄 Creazione di una NUOVA istanza di View per il logout...");

        View newMainView = new View();
        newMainView.setNavigationService(this);

        Parent root = newMainView.getRoot();
        if (root != null) {
            stage.setScene(new Scene(root, 400, 300));
            stage.setTitle("Schermata Principale");
            LOGGER.info("✅ Navigazione alla nuova View completata.");

            // ✅ Usa lo stato salvato per mantenere la modalità online
            new SessionController(stage, SessionController.getIsOnlineModeStatic(), this);
        } else {
            LOGGER.warning("❌ Errore: View principale è NULL!");
        }
    }




    @Override
    public Parent navigateToGestioneView(boolean isOfflineMode, boolean isInterfaccia1) {
        if (LOGGER.isLoggable(java.util.logging.Level.INFO)) {
            LOGGER.info(String.format("🔄 Creazione di GestioneProdottoController per %s, Interfaccia: %s",
                    isOfflineMode ? "Offline" : "Online",
                    isInterfaccia1 ? "1" : "2"));
        }

        GestoreDAOImpl gestoreDAO = new GestoreDAOImpl();
        AuthController authController = new AuthController(gestoreDAO);

        GestioneController gestioneController = new GestioneController(stage, isInterfaccia1, this, authController);


        Parent rootView = gestioneController.getRootView();
        if (rootView == null) {
            LOGGER.warning("❌ Errore: `getRootView()` ha restituito NULL!");
        } else {
            LOGGER.info("✅ GestioneProdottoController creato con successo!");
        }

        return rootView;
    }

    @Override
    public Parent createMainView() {
        LOGGER.info("🔄 Creazione di una nuova Main View per il logout...");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/MainView.fxml"));
            return loader.load();
        } catch (IOException e) {
            LOGGER.severe("❌ Errore nel caricamento della Main View: " + e.getMessage());
            return null;
        }
    }
}
