package org.example.controllergrafici;

import org.example.dao.ClienteDAO;
import org.example.dao.ClienteDAOImpl;
import org.example.model.Cliente;
import org.example.service.NavigationService;
import org.example.view.Login1View;
import org.example.view.Login2View;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController {
    private final Object view; // Può essere Login1View o Login2View
    private final NavigationService navigationService;
    private final boolean isOnlineMode;
    private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());

    private TextField usernameField;
    private PasswordField passwordField;
    private Button avantiButton;
    private Button loginButton;
    private Label statusLabel;

    public LoginController(Object view, NavigationService navigationService, boolean isOnlineMode) {
        this.view = view;
        this.navigationService = navigationService;
        this.isOnlineMode = isOnlineMode;
        initialize();
    }

    private void initialize() {
        if (view instanceof Login1View) {
            Login1View loginView = (Login1View) view;
            usernameField = loginView.getUsernameField();
            passwordField = loginView.getPasswordField();
            avantiButton = loginView.getAvantiButton();
            loginButton = loginView.getLoginButton();
            statusLabel = loginView.getStatusLabel();

            avantiButton.setOnAction(event -> showPasswordField());
        } else if (view instanceof Login2View) {
            Login2View loginView = (Login2View) view;
            usernameField = loginView.getUsernameField();
            passwordField = loginView.getPasswordField();
            loginButton = loginView.getLoginButton();
            statusLabel = new Label("Login");

            // Non serve "Avanti" per Login2View, si inserisce tutto subito
        }

        loginButton.setOnAction(event -> performLogin());
    }

    private void showPasswordField() {
        if (!usernameField.getText().trim().isEmpty()) {
            statusLabel.setText("Ora inserisci la password:");
            avantiButton.setDisable(true);
            usernameField.setDisable(true);
            passwordField.setVisible(true);
            passwordField.setDisable(false);
            loginButton.setVisible(true);
            loginButton.setDisable(false);
        } else {
            statusLabel.setText("⚠️ Inserisci un nome utente prima di proseguire!");
        }
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        LOGGER.log(Level.INFO, "🔑 Tentativo di login con username: {0}", username);

        ClienteDAO clienteDAO = new ClienteDAOImpl(isOnlineMode);
        Cliente cliente = clienteDAO.findByUsername(username);

        if (cliente != null && cliente.getPassword().equals(password)) {
            LOGGER.info("✅ Login riuscito!");
            statusLabel.setText("✅ Accesso effettuato!");
            navigationService.navigateToNegozio();
        } else {
            LOGGER.warning("❌ Credenziali errate o utente inesistente!");
            statusLabel.setText("❌ Errore: credenziali non valide.");
        }
    }
}
