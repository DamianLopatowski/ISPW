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
    private final boolean isOnlineMode;
    private static boolean isInterfaccia1;
    protected static boolean isOnlineModeStatic = true;
    private static final String INTERFACCIA_1_LABEL = "Interfaccia 1";
    private static final String INTERFACCIA_2_LABEL = "Interfaccia 2";


    // ✅ Costruttore
    public SessionController(Stage stage, boolean isOnlineMode, NavigationService navigationService) {
        this.stage = stage;
        this.isOnlineMode = isOnlineMode;
        this.navigationService = navigationService;
        setIsOnlineModeStatic(isOnlineMode); // imposta variabile statica coerente

        View mainView = new View(); // UI iniziale
        new ViewController(mainView, navigationService); // collega eventi view → navigazione

        this.context = new ApplicationContext(stage, mainView); // contesto dell'app
        initializeView();
    }

    // ✅ Getter/setter statici
    public static void setIsOnlineModeStatic(boolean mode) {
        isOnlineModeStatic = mode;
    }

    public static boolean getIsInterfaccia1Static() {
        return isInterfaccia1;
    }

    public static boolean getIsOnlineModeStatic() {
        return isOnlineModeStatic;
    }

    // ✅ Schermata iniziale: selezione interfaccia e avvio login
    private void initializeView() {
        View view = context.getMainView();

        LOGGER.info("✅ Modalità al riavvio: " + (isOnlineMode ? "ONLINE" : "OFFLINE"));

        view.getLoginButton().setOnAction(event -> {
            if (view.getInterfaccia1Option().isSelected()) {
                SessionController.setIsInterfaccia1Static(true);
            } else if (view.getInterfaccia2Option().isSelected()) {
                SessionController.setIsInterfaccia1Static(false);
            } else {
                LOGGER.warning("❌ Nessuna interfaccia selezionata.");
                return;
            }

            LOGGER.info(String.format("🔄 Avvio login (%s) - %s",
                    isInterfaccia1 ? INTERFACCIA_1_LABEL : INTERFACCIA_2_LABEL,
                    isOnlineMode ? "ONLINE" : "OFFLINE"));
            startLogin();
        });

        stage.setScene(new Scene(view.getRoot(), 400, 300));
        stage.setTitle("Selezione Interfaccia");
        stage.show();
    }

    // ✅ Avvia schermata login (diversa per interfaccia scelta)
    private void startLogin() {
        LOGGER.info("🔐 Avvio schermata login...");

        GestoreDAOImpl gestoreDAO = new GestoreDAOImpl();
        AuthController authController = new AuthController(gestoreDAO);

        if (isOnlineMode) {
            gestoreDAO.refreshOnlineCredentials();
            LOGGER.info("🌐 Credenziali ricaricate dal database.");
        } else {
            LOGGER.info("🟢 Modalità offline: credenziali locali.");
        }

        Parent loginRoot;
        Button loginButton;
        TextField usernameField;
        PasswordField passwordField;

        if (isInterfaccia1) {
            Login1View loginView = new Login1View();  // Interfaccia 1 → Login1
            new LoginController(loginView, navigationService, isOnlineMode);
            loginRoot = loginView.getRoot();
            loginButton = loginView.getLoginButton();
            usernameField = loginView.getUsernameField();
            passwordField = loginView.getPasswordField();
        } else {
            Login2View loginView = new Login2View();  // Interfaccia 2 → Login2
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

                LOGGER.info("🔑 Tentativo di login con username: " + username);
                boolean loginSuccess = authController.handleLogin(username, password, !isOnlineMode);

                if (loginSuccess) {
                    LOGGER.info("✅ Login riuscito!");
                    navigateToGestione();
                } else {
                    LOGGER.warning("❌ Login fallito: credenziali errate.");
                }
            });
        } else {
            LOGGER.warning("⚠️ Bottone login nullo.");
        }

        if (loginRoot != null) {
            stage.setScene(new Scene(new VBox(loginRoot), 400, 300));
            stage.setTitle("Login - " + (isInterfaccia1 ? "Interfaccia 1" : "Interfaccia 2"));
        } else {
            LOGGER.warning("❌ Root login nullo.");
        }
    }

    // ✅ Dopo login, naviga al negozio con la vista corretta
    private void navigateToGestione() {
        LOGGER.info("🔁 Navigazione all'interfaccia del negozio...");
        Parent gestioneView = navigationService.navigateToGestioneView(isOnlineMode, isInterfaccia1);

        if (gestioneView != null) {
            stage.setScene(new Scene(gestioneView, 800, 600));
            stage.setTitle("Negozio - " + (isInterfaccia1 ? "Interfaccia 1" : "Interfaccia 2"));
        } else {
            LOGGER.warning("❌ gestioneView null!");
        }
    }

    public static void setIsInterfaccia1Static(boolean value) {
        isInterfaccia1 = value;
    }

}
