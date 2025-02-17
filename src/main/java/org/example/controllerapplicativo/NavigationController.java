package org.example.controllerapplicativo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.controllergrafici.GestioneController;
import org.example.dao.ClienteDAO;
import org.example.dao.ClienteDAOImpl;
import org.example.dao.GestoreDAOImpl;
import org.example.model.Cliente;
import org.example.service.NavigationService;
import org.example.view.*;

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
    public void navigateToLogin(boolean isInterfaccia1, boolean isCliente) {
        LOGGER.info("🔄 Navigazione al login cliente...");

        Parent loginView;
        if (isCliente) {
            loginView = isInterfaccia1 ? new LoginOfflineView().getRoot() : new LoginOnlineView(stage, this).getRoot();
        } else {
            loginView = isInterfaccia1 ? new LoginOfflineView().getRoot() : new LoginOnlineView(stage, this).getRoot();
        }

        if (loginView != null) {
            stage.setScene(new Scene(loginView, 400, 300));
            stage.setTitle("Login Cliente");

            // ✅ Controlliamo se il login funziona
            setupLoginHandler(isCliente);
        } else {
            LOGGER.warning("❌ Errore: LoginView è NULL!");
        }
    }

    private void setupLoginHandler(boolean isCliente) {
        LOGGER.info("✅ Cliente autenticato. Verifica credenziali in corso...");

        Parent loginRoot = stage.getScene().getRoot();
        if (loginRoot instanceof VBox) {
            VBox vbox = (VBox) loginRoot;

            // Stampiamo tutti gli elementi per capire la struttura
            for (int i = 0; i < vbox.getChildren().size(); i++) {
                LOGGER.info("🔍 Elemento in VBox [" + i + "]: " + vbox.getChildren().get(i).getClass().getName());
            }

            final TextField[] usernameField = {null};
            final PasswordField[] passwordField = {null};
            final Button[] avantiButton = {null};
            final Button[] loginButton = {null};

            for (javafx.scene.Node node : vbox.getChildren()) {
                if (node instanceof TextField && usernameField[0] == null) {
                    usernameField[0] = (TextField) node;
                } else if (node instanceof PasswordField && passwordField[0] == null) {
                    passwordField[0] = (PasswordField) node;
                } else if (node instanceof Button) {
                    if (avantiButton[0] == null) {
                        avantiButton[0] = (Button) node;
                    } else if (loginButton[0] == null) {
                        loginButton[0] = (Button) node;
                    }
                }
            }

            if (usernameField[0] == null || passwordField[0] == null || loginButton[0] == null || avantiButton[0] == null) {
                LOGGER.warning("❌ Errore: uno dei campi di login è NULL! Controlla la UI.");
                return;
            }

            // ✅ Imposta il bottone "Avanti" per attivare la password field
            avantiButton[0].setOnAction(event -> {
                LOGGER.info("✅ Bottone Avanti premuto! Ora puoi inserire la password.");
                avantiButton[0].setDisable(true);
                usernameField[0].setDisable(true);
                passwordField[0].setVisible(true);
                passwordField[0].setDisable(false);
                loginButton[0].setVisible(true);
                loginButton[0].setDisable(false);
            });

            // ✅ Il login viene eseguito SOLO quando si preme "Login"
            loginButton[0].setOnAction(event -> {
                String username = usernameField[0].getText().trim();
                String password = passwordField[0].getText().trim();  // ✅ Ora la password è stata inserita!

                LOGGER.info("🔑 Tentativo di login con username: " + username);
                LOGGER.info("🔑 Password inserita dall'utente: " + password);

                ClienteDAO clienteDAO = new ClienteDAOImpl(SessionController.getIsOnlineModeStatic());
                Cliente cliente = clienteDAO.findByUsername(username);

                if (cliente != null) {
                    LOGGER.info("✅ Cliente trovato: " + cliente.getUsername());
                    LOGGER.info("🔒 Password salvata nel database: " + cliente.getPassword());

                    if (cliente.getPassword().equals(password)) {
                        LOGGER.info("✅ Credenziali corrette! Navigazione al negozio...");
                        navigateToNegozio();
                    } else {
                        LOGGER.warning("❌ Password errata! La password inserita: " + password + " | Password nel database: " + cliente.getPassword());
                    }
                } else {
                    LOGGER.warning("❌ Nessun cliente trovato con username: " + username);
                }
            });
        }
    }


    @Override
    public void navigateToRegistrazioneCliente(boolean isInterfaccia1) {
        RegistratiClienteOfflineView offlineView = isInterfaccia1 ? new RegistratiClienteOfflineView(stage) : null;
        RegistratiClienteOnlineView onlineView = isInterfaccia1 ? null : new RegistratiClienteOnlineView(stage);

        Parent registrazioneView = isInterfaccia1 ? offlineView.getRoot() : onlineView.getRoot();

        if (registrazioneView != null) {
            stage.setScene(new Scene(registrazioneView, 400, 300));
            stage.setTitle("Registrazione Cliente");
            LOGGER.info("🔄 Navigazione alla registrazione cliente " + (isInterfaccia1 ? "Offline" : "Online"));

            // Imposta manualmente il comportamento del bottone
            Button registratiButton = isInterfaccia1 ? offlineView.getRegistratiButton() : onlineView.getRegistratiButton();
            TextField usernameField = isInterfaccia1 ? offlineView.getUsernameField() : onlineView.getUsernameField();
            TextField nomeField = isInterfaccia1 ? offlineView.getNomeField() : onlineView.getNomeField();
            TextField cognomeField = isInterfaccia1 ? offlineView.getCognomeField() : onlineView.getCognomeField();
            PasswordField passwordField = isInterfaccia1 ? offlineView.getPasswordField() : onlineView.getPasswordField();

            registratiButton.setOnAction(event -> {
                LOGGER.info("🔘 Bottone REGISTRATI premuto! Chiamo processaRegistrazione...");
                processaRegistrazione(usernameField, nomeField, cognomeField, passwordField, isInterfaccia1);
            });
        } else {
            LOGGER.warning("❌ Errore: registrazioneView è NULL!");
        }
    }


    public void processaRegistrazione(TextField usernameField, TextField nomeField, TextField cognomeField, PasswordField passwordField, boolean isInterfaccia1) {
        LOGGER.info("🔥 Entrato in processaRegistrazione!");  // Se non appare, il metodo non viene mai chiamato

        String username = usernameField.getText().trim();
        String nome = nomeField.getText().trim();
        String cognome = cognomeField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || nome.isEmpty() || cognome.isEmpty() || password.isEmpty()) {
            LOGGER.warning("❌ Campi vuoti! Riempi tutti i campi.");
            return;
        }

        LOGGER.info("📌 Tentativo di registrazione per: " + username + " - " + nome + " " + cognome);

        Cliente cliente = new Cliente(username, nome, cognome, password);
        ClienteDAO clienteDAO = new ClienteDAOImpl(SessionController.getIsOnlineModeStatic());
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


    private void setupRegistrazioneHandler(boolean isInterfaccia1) {
        Parent registrazioneRoot = stage.getScene().getRoot();

        if (!(registrazioneRoot instanceof VBox)) {
            LOGGER.warning("❌ Errore: Registrazione View non è un VBox.");
            return;
        }

        VBox vbox = (VBox) registrazioneRoot;

        // Recuperiamo direttamente la View corretta
        RegistratiClienteOfflineView offlineView = isInterfaccia1 ? new RegistratiClienteOfflineView(stage) : null;
        RegistratiClienteOnlineView onlineView = isInterfaccia1 ? null : new RegistratiClienteOnlineView(stage);

        TextField usernameField = isInterfaccia1 ? offlineView.getUsernameField() : onlineView.getUsernameField();
        TextField nomeField = isInterfaccia1 ? offlineView.getNomeField() : onlineView.getNomeField();
        TextField cognomeField = isInterfaccia1 ? offlineView.getCognomeField() : onlineView.getCognomeField();
        PasswordField passwordField = isInterfaccia1 ? offlineView.getPasswordField() : onlineView.getPasswordField();
        Button registratiButton = isInterfaccia1 ? offlineView.getRegistratiButton() : onlineView.getRegistratiButton();

        // ✅ Verifica che i campi non siano NULL
        if (usernameField == null || nomeField == null || cognomeField == null || passwordField == null || registratiButton == null) {
            LOGGER.warning("❌ Errore: uno dei campi di registrazione è NULL!");
            return;
        }

        LOGGER.info("✅ Campi di registrazione trovati correttamente!");

        // ✅ Forza la rimozione di azioni esistenti sul bottone
        registratiButton.setOnAction(null);

        // ✅ Imposta l'azione corretta
        registratiButton.setOnAction(event -> {
            LOGGER.info("🔘 Bottone REGISTRATI premuto! Chiamo processaRegistrazione...");
            processaRegistrazione(usernameField, nomeField, cognomeField, passwordField, isInterfaccia1);
        });

        LOGGER.info("✅ Evento di registrazione associato al pulsante correttamente.");
    }


    public void navigateToNegozio() {
        LOGGER.info("🔄 Navigazione all'interfaccia del negozio...");
        Parent negozioView = new NegozioView().getRoot();
        if (negozioView != null) {
            stage.setScene(new Scene(negozioView, 600, 400));
            stage.setTitle("Negozio");
            LOGGER.info("✅ Navigazione al negozio completata.");
        } else {
            LOGGER.warning("❌ Errore: NegozioView è NULL!");
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
