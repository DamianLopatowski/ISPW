package org.example.controllerapplicativo;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controllergrafici.GestioneController;
import org.example.controllergrafici.RegistratiClienteController;
import org.example.controllergrafici.LoginController;
import org.example.controllergrafici.ViewController;
import org.example.dao.ClienteDAOImpl;
import org.example.dao.GestoreDAOImpl;
import org.example.service.NavigationService;
import org.example.view.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NavigationController implements NavigationService {
    private static final Logger LOGGER = Logger.getLogger(NavigationController.class.getName());
    private final Stage stage;

    public NavigationController(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void navigateToLogin(boolean isInterfaccia1, boolean isCliente) {
        boolean isOnlineMode = SessionController.getIsOnlineModeStatic();

        Parent loginRoot;
        if (isInterfaccia1) {
            Login1View loginView = new Login1View();
            new LoginController(loginView, this, isOnlineMode);
            loginRoot = loginView.getRoot();
        } else {
            Login2View loginView = new Login2View();
            new LoginController(loginView, this, isOnlineMode);
            loginRoot = loginView.getRoot();
        }

        if (loginRoot != null) {
            stage.setScene(new Scene(loginRoot, 400, 300));
            stage.setTitle("Login Cliente");
            stage.show();
        } else {
            LOGGER.warning("❌ Errore: LoginView è NULL!");
        }
    }

    @Override
    public Parent navigateToRegistrazioneCliente(boolean isInterfaccia1) {
        boolean isOnlineMode = SessionController.getIsOnlineModeStatic();
        ClienteDAOImpl clienteDAO = new ClienteDAOImpl(isOnlineMode);

        RegistratiClienteController registratiClienteController = new RegistratiClienteController(stage, clienteDAO, this, isInterfaccia1);
        Parent registrazioneView = registratiClienteController.getViewRoot();

        if (registrazioneView != null) {
            stage.setScene(new Scene(registrazioneView, 400, 300));
            stage.setTitle("Registrazione Cliente");
            LOGGER.log(Level.INFO, "🔄 Navigazione alla registrazione cliente {0}", isInterfaccia1 ? "Offline" : "Online");
        } else {
            LOGGER.warning("❌ Errore: registrazioneView è NULL!");
        }
        return registrazioneView;
    }

    @Override
    public void navigateToMainView() {
        LOGGER.info("🔄 Creazione di una NUOVA istanza di View per il logout...");

        View newMainView = new View();
        new ViewController(newMainView, this);

        Parent root = newMainView.getRoot();
        if (root != null) {
            stage.setScene(new Scene(root, 400, 300));
            stage.setTitle("Schermata Principale");
            LOGGER.info("✅ Navigazione alla nuova View completata.");

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
    public void navigateToNegozio() {
        LOGGER.info("🔄 Navigazione all'interfaccia del negozio...");
        Parent negozioView = new NegozioView().getRoot();
        if (negozioView != null) {
            stage.setScene(new Scene(negozioView, 600, 400));
            stage.setTitle("Negozio");
            stage.show();
            LOGGER.info("✅ Navigazione al negozio completata.");
        } else {
            LOGGER.warning("❌ Errore: NegozioView è NULL!");
        }
    }
}
