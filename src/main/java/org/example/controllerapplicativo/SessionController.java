package org.example.controllerapplicativo;

import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Parent;
import java.util.logging.Logger;
import org.example.ApplicationContext;
import org.example.controllergrafici.LoginController;
import org.example.controllergrafici.ViewController;
import org.example.dao.GestoreDAOImpl;
import org.example.service.NavigationService;
import org.example.view.Login1View;
import org.example.view.Login2View;
import org.example.view.View;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.Scene;

public class SessionController {
    private static final Logger LOGGER = Logger.getLogger(SessionController.class.getName());

    private final Stage stage;
    private final ApplicationContext context;
    private final NavigationService navigationService;
    private boolean isOnlineMode;
    private static boolean isInterfaccia1;
    protected static boolean isOnlineModeStatic = true;

    // ✅ Costruttore aggiornato
    public SessionController(Stage stage, boolean isOnlineMode, NavigationService navigationService) {
        this.stage = stage;
        this.isOnlineMode = isOnlineMode;
        this.navigationService = navigationService;

        // Creazione dell'istanza di View
        View mainView = new View();

        // Creazione del ViewController e assegnazione del servizio di navigazione
        new ViewController(mainView, navigationService);

        // Creazione del contesto con la View aggiornata
        this.context = new ApplicationContext(stage, mainView);
        initializeView();
    }

    // ✅ Set static per la modalità online
    public static void setIsOnlineModeStatic(boolean mode) {
        isOnlineModeStatic = mode;
    }

    // ✅ Set static per l'interfaccia scelta
    public static void setIsInterfaccia1(boolean value) {
        isInterfaccia1 = value;
    }

    // ✅ Get static sicuro per l'interfaccia scelta
    public static boolean getIsInterfaccia1Static() {
        return isInterfaccia1;
    }

    // ✅ Get static per online/offline
    public static boolean getIsOnlineModeStatic() {
        return isOnlineModeStatic;
    }

    // ⚙️ Inizializzazione schermata di selezione interfaccia
    private void initializeView() {
        View view = context.getMainView();

        if (LOGGER.isLoggable(java.util.logging.Level.INFO)) {
            LOGGER.info(String.format("✅ Modalità al riavvio: %s", isOnlineMode ? "ONLINE" : "OFFLINE"));
        }

        view.getLoginButton().setOnAction(event -> {
            if (view.getInterfaccia1Option().isSelected()) {
                isInterfaccia1 = true;
            } else if (view.getInterfaccia2Option().isSelected()) {
                isInterfaccia1 = false;
            } else {
                LOGGER.warning("❌ Nessuna interfaccia selezionata.");
                return;
            }

            LOGGER.info("🔄 Avvio login per " + (isOnlineMode ? "modalità ONLINE" : "modalità OFFLINE"));
            startLogin();
        });

        stage.setScene(new Scene(view.getRoot(), 400, 300));
        stage.setTitle("Selezione Interfaccia");
        stage.show();
    }

    // ⚙️ Navigazione alla gestione prodotti dopo login
    private void navigateToGestione() {
        LOGGER.info("🔄 Navigazione a GestioneProdotti...");
        Parent gestioneView = navigationService.navigateToGestioneView(isOnlineMode, isInterfaccia1);
        if (gestioneView != null) {
            LOGGER.info("✅ Cambio scena a GestioneProdotti...");
            stage.setScene(new Scene(gestioneView, 600, 400));
            stage.setTitle("Gestione Prodotti - " + (isInterfaccia1 ? "Interfaccia 1" : "Interfaccia 2"));
        } else {
            LOGGER.warning("❌ Errore: gestioneView è NULL!");
        }
    }

    // ⚙️ Login
    private void startLogin() {
        LOGGER.info("🔑 Avvio della schermata di login...");

        GestoreDAOImpl gestoreDAO = new GestoreDAOImpl();
        AuthController authController = new AuthController(gestoreDAO);

        if (!isOnlineMode) {
            LOGGER.info("🟢 Modalità offline, nessuna ricarica credenziali.");
        } else {
            LOGGER.info("🔄 Modalità online, ricarico le credenziali online...");
            gestoreDAO.refreshOnlineCredentials();
        }

        Parent loginRoot;
        Button loginButton;
        TextField usernameField;
        PasswordField passwordField;

        if (isInterfaccia1) {
            Login2View loginView = new Login2View();
            new LoginController(loginView, navigationService, isOnlineMode);
            loginRoot = loginView.getRoot();
            loginButton = loginView.getLoginButton();
            usernameField = loginView.getUsernameField();
            passwordField = loginView.getPasswordField();
        } else {
            Login1View loginView = new Login1View();
            new LoginController(loginView, navigationService, isOnlineMode);
            loginRoot = loginView.getRoot();
            loginButton = loginView.getLoginButton();
            usernameField = loginView.getUsernameField();
            passwordField = loginView.getPasswordField();
        }

        if (loginButton != null) {
            loginButton.setOnAction(event -> {
                String username = usernameField.getText();
                String password = passwordField.getText();
                LOGGER.info(String.format("🔑 Tentativo di login con username: %s", username));
                boolean loginSuccess = authController.handleLogin(username, password, !isOnlineMode);

                if (loginSuccess) {
                    LOGGER.info("✅ Login riuscito!");
                    navigateToGestione();
                } else {
                    LOGGER.warning("❌ Credenziali errate.");
                }
            });
        } else {
            LOGGER.warning("⚠️ Bottone di login è NULL! Verifica la creazione della View.");
        }

        if (loginRoot != null) {
            stage.setScene(new Scene(new VBox(loginRoot), 400, 300));
            stage.setTitle("Login - " + (isInterfaccia1 ? "Interfaccia 1" : "Interfaccia 2"));
            LOGGER.info("✅ Scena aggiornata con la schermata di login.");
        } else {
            LOGGER.warning("❌ Errore: loginRoot è NULL!");
        }
    }
}
