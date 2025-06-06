package org.example.controllerapplicativo;

import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.example.ApplicationContext;
import org.example.controllergrafici.LoginController;
import org.example.controllergrafici.ViewController;
import org.example.dao.GestoreDAOImpl;
import org.example.model.Cliente;
import org.example.model.Prodotto;
import org.example.service.NavigationService;
import org.example.service.OrdineService;
import org.example.view.Login1View;
import org.example.view.Login2View;
import org.example.view.View;

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
    private static Cliente clienteLoggato;
    private static final Map<Prodotto, Integer> carrello = new HashMap<>();

    public SessionController(Stage stage, boolean isOnlineMode, NavigationService navigationService) {
        this.stage = stage;
        this.isOnlineMode = isOnlineMode;
        this.navigationService = navigationService;
        setIsOnlineModeStatic(isOnlineMode);

        View mainView = new View();
        new ViewController(mainView, navigationService);
        this.context = new ApplicationContext(stage, mainView);
        initializeView();
    }

    public static void setIsOnlineModeStatic(boolean mode) {
        isOnlineModeStatic = mode;
    }

    public static boolean getIsInterfaccia1Static() {
        return isInterfaccia1;
    }

    public static boolean getIsOnlineModeStatic() {
        return isOnlineModeStatic;
    }

    private void initializeView() {
        View view = context.getMainView();

        if (LOGGER.isLoggable(java.util.logging.Level.INFO)) {
            LOGGER.info(String.format("✅ Modalità al riavvio: %s", isOnlineMode ? "ONLINE" : "OFFLINE"));
        }

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
            Login1View loginView = new Login1View();
            new LoginController(loginView, navigationService, isOnlineMode);
            loginRoot = loginView.getRoot();
            loginButton = loginView.getLoginButton();
            usernameField = loginView.getUsernameField();
            passwordField = loginView.getPasswordField();
        } else {
            Login2View loginView = new Login2View();
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
                    LOGGER.warning("❌ Login fallito: credenziali errate.");
                }
            });
        } else {
            LOGGER.warning("⚠️ Bottone login nullo.");
        }

        if (loginRoot != null) {
            stage.setScene(new Scene(new VBox(loginRoot), 400, 300));
            stage.setTitle(String.format("Login - %s", isInterfaccia1 ? INTERFACCIA_1_LABEL : INTERFACCIA_2_LABEL));
        } else {
            LOGGER.warning("❌ Root login nullo.");
        }
    }

    private void navigateToGestione() {
        LOGGER.info("🔁 Navigazione all'interfaccia del negozio...");
        Parent gestioneView = navigationService.navigateToGestioneView(isOnlineMode, isInterfaccia1);

        if (gestioneView != null) {
            stage.setScene(new Scene(gestioneView, 1100, 700));
            stage.setResizable(true);
            stage.setTitle(String.format("Negozio - %s", isInterfaccia1 ? INTERFACCIA_1_LABEL : INTERFACCIA_2_LABEL));
        } else {
            LOGGER.warning("❌ gestioneView null!");
        }
    }

    public static void setIsInterfaccia1Static(boolean value) {
        isInterfaccia1 = value;
    }

    public static Cliente getClienteLoggato() {
        return clienteLoggato;
    }

    public static void setClienteLoggato(Cliente cliente) {
        clienteLoggato = cliente;
    }

    public static Map<Prodotto, Integer> getCarrello() {
        return carrello;
    }

    public static void svuotaCarrello() {
        carrello.clear();
    }

    public static void aggiungiAlCarrello(Prodotto prodotto) {
        carrello.put(prodotto, carrello.getOrDefault(prodotto, 0) + 1);
    }

    public static void rimuoviUnitaDalCarrello(Prodotto prodotto) {
        carrello.computeIfPresent(prodotto, (k, v) -> (v > 1) ? v - 1 : null);
    }

    public static void rimuoviDalCarrello(Prodotto prodotto) {
        carrello.remove(prodotto);
    }


    public static void salvaOrdineCorrente() {
        if (clienteLoggato == null || carrello.isEmpty()) {
            LOGGER.warning("⚠️ Nessun cliente loggato o carrello vuoto.");
            return;
        }

        // Calcolo totale ordine
        double totale = carrello.entrySet().stream()
                .mapToDouble(e -> e.getKey().getPrezzoVendita() * e.getValue())
                .sum();

        // Creazione istanza servizio ordini
        OrdineService ordineService = new OrdineService();

        // Salvataggio ordine
        ordineService.salvaOrdineOnline(clienteLoggato, new HashMap<>(carrello));

        LOGGER.info("📦 Ordine salvato con successo.");
        svuotaCarrello();
    }

}
