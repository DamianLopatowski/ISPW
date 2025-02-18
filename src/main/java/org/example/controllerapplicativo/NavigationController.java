package org.example.controllerapplicativo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.controllergrafici.GestioneController;
import org.example.dao.ClienteDAO;
import org.example.dao.ClienteDAOImpl;
import org.example.dao.GestoreDAOImpl;
import org.example.model.Cliente;
import org.example.service.NavigationService;
import org.example.view.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NavigationController implements NavigationService {
    private static final Logger LOGGER = Logger.getLogger(NavigationController.class.getName());
    private final Stage stage;



    // Costruttore corretto con ApplicationContext
    public NavigationController(Stage stage) {
        this.stage = stage;
    }


    @Override
    public void navigateToLogin(boolean isInterfaccia1, boolean isCliente) {
        boolean isOnlineMode = SessionController.getIsOnlineModeStatic(); // ✅ Recuperiamo lo stato attuale

        Parent loginView = isInterfaccia1
                ? new Login1View(this, isOnlineMode).getRoot() // ✅ Ora passiamo NavigationService e isOnlineMode!
                : new Login2View(this, isOnlineMode).getRoot();

        if (loginView != null) {
            stage.setScene(new Scene(loginView, 400, 300));
            stage.setTitle("Login Cliente");
            stage.show();
        } else {
            LOGGER.warning("❌ Errore: LoginView è NULL!");
        }
    }
    @Override
    public void navigateToRegistrazioneCliente(boolean isInterfaccia1) {
        RegistratiCliente1View offlineView = null;
        RegistratiCliente2View onlineView = null;
        Parent registrazioneView;

        if (isInterfaccia1) {
            offlineView = new RegistratiCliente1View(stage);
            registrazioneView = offlineView.getRoot();
        } else {
            onlineView = new RegistratiCliente2View(stage);
            registrazioneView = onlineView.getRoot();
        }

        if (registrazioneView != null) {
            stage.setScene(new Scene(registrazioneView, 400, 300));
            stage.setTitle("Registrazione Cliente");
            LOGGER.log(Level.INFO, "🔄 Navigazione alla registrazione cliente {0}", isInterfaccia1 ? "Offline" : "Online");

            setupRegistrazioneHandler(isInterfaccia1, offlineView, onlineView);
        } else {
            LOGGER.warning("❌ Errore: registrazioneView è NULL!");
        }
    }

    private void setupRegistrazioneHandler(boolean isInterfaccia1,
                                           RegistratiCliente1View offlineView,
                                           RegistratiCliente2View onlineView) {
        Button registratiButton;
        TextField usernameField;
        TextField nomeField;
        TextField cognomeField;
        PasswordField passwordField;

        if (isInterfaccia1) {
            if (offlineView == null) {
                LOGGER.warning("❌ Errore: offlineView è NULL!");
                return;
            }
            registratiButton = offlineView.getRegistratiButton();
            usernameField = offlineView.getUsernameField();
            nomeField = offlineView.getNomeField();
            cognomeField = offlineView.getCognomeField();
            passwordField = offlineView.getPasswordField();
        } else {
            if (onlineView == null) {
                LOGGER.warning("❌ Errore: onlineView è NULL!");
                return;
            }
            registratiButton = onlineView.getRegistratiButton();
            usernameField = onlineView.getUsernameField();
            nomeField = onlineView.getNomeField();
            cognomeField = onlineView.getCognomeField();
            passwordField = onlineView.getPasswordField();
        }

        if (registratiButton == null || usernameField == null || nomeField == null || cognomeField == null || passwordField == null) {
            LOGGER.warning("❌ Errore: uno dei campi di registrazione è NULL! Controlla la UI.");
            return;
        }

        registratiButton.setOnAction(event -> {
            LOGGER.info("🔘 Bottone REGISTRATI premuto! Chiamo processaRegistrazione...");
            processaRegistrazione(usernameField, nomeField, cognomeField, passwordField, isInterfaccia1);
        });
    }

    private void processaRegistrazione(TextField usernameField, TextField nomeField, TextField cognomeField, PasswordField passwordField, boolean isInterfaccia1) {
        LOGGER.info("🔥 Entrato in processaRegistrazione!");

        String username = usernameField.getText().trim();
        String nome = nomeField.getText().trim();
        String cognome = cognomeField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || nome.isEmpty() || cognome.isEmpty() || password.isEmpty()) {
            LOGGER.warning("❌ Campi vuoti! Riempi tutti i campi.");
            return;
        }

        LOGGER.log(Level.INFO, "📌 Tentativo di registrazione per: {0} - {1} {2}", new Object[]{username, nome, cognome});

        Cliente cliente = new Cliente(username, nome, cognome, password);
        boolean isOnlineMode = SessionController.getIsOnlineModeStatic(); // ✅ Otteniamo la modalità scelta all'inizio
        ClienteDAO clienteDAO = new ClienteDAOImpl(isOnlineMode);
        clienteDAO.saveCliente(cliente);

        // Verifica se il cliente è stato salvato
        Cliente clienteVerifica = clienteDAO.findByUsername(username);
        if (clienteVerifica == null) {
            LOGGER.severe("❌ Errore: Il cliente non è stato salvato correttamente!");
            return;
        }

        LOGGER.info("✅ Cliente registrato con successo! Navigazione al login cliente...");
        navigateToLogin(isInterfaccia1, true);
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
