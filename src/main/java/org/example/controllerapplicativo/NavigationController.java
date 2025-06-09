package org.example.controllerapplicativo;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controllergrafici.*;
import org.example.dao.ClienteDAOImpl;
import org.example.dao.GestoreDAOImpl;
import org.example.dao.ProdottoDAO;
import org.example.dao.ProdottoDAOImpl;
import org.example.model.Cliente;
import org.example.service.NavigationService;
import org.example.view.*;

import java.io.FileInputStream;
import java.util.Properties;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NavigationController implements NavigationService {
    private static final Logger LOGGER = Logger.getLogger(NavigationController.class.getName());
    private final Stage stage;
    private static final String CONFIG_FILE_PATH = "config.properties";
    private Cliente clienteLoggato;

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
            LOGGER.warning("Errore: LoginView è NULL!");
        }
    }
    @Override
    public Parent navigateToRegistrazioneCliente(boolean isInterfaccia1) {
        //Acquisiamo la modalità online una volta SOLA e la passiamo
        boolean isOnlineMode = SessionController.getIsOnlineModeStatic();

        //Stampa di debug per verificare il valore corretto
        LOGGER.log(Level.INFO, "Modalità selezionata: {0} | Interfaccia: {1}",
                new Object[]{isOnlineMode ? "ONLINE" : "OFFLINE", isInterfaccia1 ? "1" : "2"});

        //Creiamo il DAO con la modalità corretta
        ClienteDAOImpl clienteDAO = new ClienteDAOImpl(isOnlineMode);

        //Carichiamo il codice univoco dal file di configurazione
        String codiceUnivoco = caricaCodiceUnivoco();

        //Creiamo il controller della registrazione con le scelte separate
        RegistratiClienteController registratiClienteController = new RegistratiClienteController(clienteDAO, this, codiceUnivoco, isInterfaccia1);
        Parent registrazioneView = registratiClienteController.getView();

        if (registrazioneView != null) {
            stage.setScene(new Scene(registrazioneView, 400, 300));
            stage.setTitle("Registrazione Cliente");
            LOGGER.log(Level.INFO, "Navigazione alla registrazione cliente - Modalità: {0} | Interfaccia: {1}",
                    new Object[]{isOnlineMode ? "Online" : "Offline", isInterfaccia1 ? "1" : "2"});
        } else {
            LOGGER.warning("❌ Errore: registrazioneView è NULL!");
        }
        return registrazioneView;
    }


    // Metodo per caricare il codice univoco da config.properties
    private String caricaCodiceUnivoco() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(fis);
            return properties.getProperty("codiceUnivoco", "DEFAULT-CODE");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Errore nel caricamento del codice univoco!", e);
            return "DEFAULT-CODE"; // Valore di fallback
        }
    }

    @Override
    public void navigateToMainView() {
        LOGGER.info("Creazione di una NUOVA istanza di View per il logout...");

        View newMainView = new View();
        new ViewController(newMainView, this);

        Parent root = newMainView.getRoot();
        if (root != null) {
            stage.setScene(new Scene(root, 400, 300));
            stage.setTitle("Schermata Principale");
            LOGGER.info("Navigazione alla nuova View completata.");

            new SessionController(stage, SessionController.getIsOnlineModeStatic(), this);
        } else {
            LOGGER.warning("Errore: View principale è NULL!");
        }
    }

    @Override
    public Parent navigateToGestioneView(boolean isOfflineMode, boolean isInterfaccia1) {
        if (LOGGER.isLoggable(java.util.logging.Level.INFO)) {
            LOGGER.info(String.format("Creazione di GestioneProdottoController per %s, Interfaccia: %s",
                    isOfflineMode ? "Offline" : "Online",
                    isInterfaccia1 ? "1" : "2"));
        }

        GestoreDAOImpl gestoreDAO = new GestoreDAOImpl();
        AuthController authController = new AuthController(gestoreDAO);

        GestioneController gestioneController = new GestioneController(stage, isInterfaccia1, this, authController);


        Parent rootView = gestioneController.getRootView();
        if (rootView == null) {
            LOGGER.warning("Errore: `getRootView()` ha restituito NULL!");
        } else {
            LOGGER.info("GestioneProdottoController creato con successo!");
        }

        return rootView;
    }

    @Override
    public void navigateToNegozio() {
        LOGGER.info("Navigazione all'interfaccia del negozio...");

        boolean isOnlineMode = SessionController.getIsOnlineModeStatic();
        boolean isInterfaccia1 = SessionController.getIsInterfaccia1Static(); // Usa la stessa logica già usata altrove

        LOGGER.log(Level.INFO, "Modalità: {0} | Interfaccia: {1}",
                new Object[]{isOnlineMode ? "ONLINE" : "OFFLINE", isInterfaccia1 ? "1" : "2"});

        NegozioController controller = new NegozioController(isOnlineMode, isInterfaccia1, this);
        Parent root = controller.getRootView();

        if (root != null) {
            stage.setScene(new Scene(root, 900, 600));
            stage.setTitle("Negozio");
            stage.show();
            LOGGER.info("Navigazione al negozio completata.");
        } else {
            LOGGER.warning("Errore: NegozioView è NULL!");
        }
    }

    @Override
    public void navigateToProfilo() {
        LOGGER.info("Navigazione al profilo utente...");
        boolean isInterfaccia1 = SessionController.getIsInterfaccia1Static();

        ProfiloController controller = new ProfiloController(isInterfaccia1, this);
        Parent root = controller.getRootView();

        if (root != null) {
            stage.setScene(new Scene(root, 600, 400));
            stage.setTitle("Profilo Utente");
            stage.show();
            LOGGER.info("Profilo utente caricato correttamente.");
        } else {
            LOGGER.warning("Errore: ProfiloView è NULL!");
        }
    }

    @Override
    public void navigateToProfilo(boolean isInterfaccia1) {
        LOGGER.info("Navigazione al profilo utente (con parametro)...");

        ProfiloController controller = new ProfiloController(isInterfaccia1, this);
        Parent root = controller.getRootView();

        if (root != null) {
            stage.setScene(new Scene(root, 600, 400));
            stage.setTitle("Profilo Utente");
            stage.show();
            LOGGER.info("Profilo utente caricato correttamente.");
        } else {
            LOGGER.warning("Errore: ProfiloView è NULL!");
        }
    }

    @Override
    public Parent navigateToGestioneProdottiView() {
        LOGGER.info("Navigazione alla GestioneProdottiView...");
        ProdottoDAO prodottoDAO = new ProdottoDAOImpl(SessionController.getIsOnlineModeStatic());
        GestioneProdottiController controller = new GestioneProdottiController(prodottoDAO, this);
        return controller.getRoot();
    }



    @Override
    public void setClienteLoggato(Cliente cliente) {
        this.clienteLoggato = cliente;

        SessionController.setClienteLoggato(cliente);
    }

    @Override
    public Cliente getClienteLoggato() {
        return clienteLoggato;
    }


    @Override
    public void setInterfaccia1(boolean isInterfaccia1) {
        SessionController.setIsInterfaccia1Static(isInterfaccia1);
    }

}