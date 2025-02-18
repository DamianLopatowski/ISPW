package org.example.view;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.controllerapplicativo.NavigationController;
import org.example.dao.ClienteDAO;
import org.example.dao.ClienteDAOImpl;
import org.example.model.Cliente;

import java.util.logging.Logger;

public class LoginOfflineView {
    private final VBox root;
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final Button avantiButton;
    private final Button loginButton;
    private final Label statusLabel;
    private static final Logger LOGGER = Logger.getLogger(LoginOfflineView.class.getName());


    private final NavigationController navigationController;

    public LoginOfflineView(NavigationController navigationController) {
        this.navigationController = navigationController;

        root = new VBox(15);
        statusLabel = new Label("Inserisci l'admin:");
        usernameField = new TextField();
        avantiButton = new Button("Avanti");

        passwordField = new PasswordField();
        passwordField.setPromptText("Inserisci la password");
        loginButton = new Button("Login Offline");

        LOGGER.info("✅ Bottone di login OFFLINE creato.");

        passwordField.setVisible(false);
        loginButton.setVisible(false);

        avantiButton.setOnAction(event -> showPasswordField());

        root.getChildren().addAll(statusLabel, usernameField, avantiButton, passwordField, loginButton);

        // **Aggiungi l'azione per il login offline**
        loginButton.setOnAction(event -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            LOGGER.info("🔑 Tentativo di login OFFLINE con username: " + username);

            ClienteDAO clienteDAO = new ClienteDAOImpl(false); // Assicuriamoci che sia offline
            Cliente cliente = clienteDAO.findByUsername(username);

            if (cliente != null && cliente.getPassword().equals(password)) {
                LOGGER.info("✅ Login OFFLINE riuscito!");
                statusLabel.setText("✅ Accesso effettuato!");
                // Navigazione al negozio offline
                navigationController.navigateToNegozio();
            } else {
                LOGGER.warning("❌ Credenziali errate o utente inesistente in modalità offline!");
                statusLabel.setText("❌ Errore: credenziali non valide.");
            }
        });
    }


    private void showPasswordField() {
        if (!usernameField.getText().trim().isEmpty()) {
            statusLabel.setText("Ora inserisci la password:");
            avantiButton.setDisable(true);
            usernameField.setDisable(true);

            passwordField.setVisible(true);
            passwordField.setDisable(false);  // ✅ Assicuriamoci che sia attiva!

            loginButton.setVisible(true);
            loginButton.setDisable(false);  // ✅ Anche il bottone deve essere attivo!
        } else {
            statusLabel.setText("⚠️ Inserisci un nome utente prima di proseguire!");
        }
    }

    public VBox getRoot() {
        return root;
    }

    public Button getLoginButton() {
        LOGGER.info("📌 LoginButton richiesto.");
        if (loginButton == null) {
            LOGGER.warning("⚠️ LoginButton è NULL! Controlla l'inizializzazione.");
        }
        return loginButton;
    }


    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public Button getAvantiButton() {
        return avantiButton;
    }

    public void enableLogin() {
        avantiButton.setDisable(false);
        usernameField.setDisable(false);
        passwordField.setVisible(true);
        loginButton.setVisible(true);
    }

}
