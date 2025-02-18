package org.example.view;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.example.controllerapplicativo.NavigationController;
import javafx.stage.Stage;
import org.example.controllerapplicativo.SessionController;
import org.example.dao.ClienteDAO;
import org.example.dao.ClienteDAOImpl;
import org.example.model.Cliente;
import org.example.service.NavigationService;

import java.util.logging.Logger;

public class LoginOnlineView {
    private final GridPane root;
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final Button loginButton;
    private final Label statusLabel;
    private static final Logger LOGGER = Logger.getLogger(LoginOnlineView.class.getName());

    // ✅ Dichiarazione delle variabili stage e navigationController
    private final Stage stage;
    private final NavigationService navigationService;

    public LoginOnlineView(Stage stage, NavigationService navigationService) {
        this.stage = stage;
        this.navigationService = navigationService;

        root = new GridPane();
        usernameField = new TextField();
        passwordField = new PasswordField();
        loginButton = new Button("Login Online");
        statusLabel = new Label("Accesso Online - Inserisci credenziali:");

        root.add(new Label("Username:"), 0, 0);
        root.add(usernameField, 1, 0);
        root.add(new Label("Password:"), 0, 1);
        root.add(passwordField, 1, 1);
        root.add(loginButton, 1, 2);
        root.add(statusLabel, 1, 3);

        // ✅ Assegniamo un'azione al bottone login
        loginButton.setOnAction(event -> {
            LOGGER.info("🔘 Bottone LOGIN premuto in LoginOnlineView!");

            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            LOGGER.info("🔑 Tentativo di login con username: " + username);

            ClienteDAO clienteDAO = new ClienteDAOImpl(SessionController.getIsOnlineModeStatic());
            Cliente cliente = clienteDAO.findByUsername(username);

            if (cliente != null) {
                LOGGER.info("✅ Cliente trovato: " + cliente.getUsername());
                LOGGER.info("🔒 Password salvata nel database: " + cliente.getPassword());

                if (cliente.getPassword().equals(password)) {
                    LOGGER.info("✅ Credenziali corrette! Navigazione al negozio...");
                    navigationService.navigateToNegozio();// ✅ Navigazione alla schermata del negozio!
                } else {
                    LOGGER.warning("❌ Password errata!");
                    statusLabel.setText("❌ Password errata! Riprova.");
                }
            } else {
                LOGGER.warning("❌ Nessun cliente trovato con username: " + username);
                statusLabel.setText("❌ Utente non trovato.");
            }
        });
    }

    public GridPane getRoot() {
        return root;
    }

    public Button getLoginButton() {
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
}
