package org.example.controllergrafici;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.controllerapplicativo.SessionController;
import org.example.dao.ClienteDAO;
import org.example.model.Cliente;
import org.example.service.EmailService;
import org.example.service.NavigationService;
import org.example.view.RegistratiCliente1View;
import org.example.view.RegistratiCliente2View;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class RegistratiClienteController {
    private static final Logger LOGGER = Logger.getLogger(RegistratiClienteController.class.getName());

    private final Parent viewRoot;
    private final ClienteDAO clienteDAO;
    private final boolean isOnlineMode;
    private final NavigationService navigationService;
    private final String codiceUnivoco;
    private int tentativiErrati = 0;
    private static final int MAX_TENTATIVI = 3;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");

    public RegistratiClienteController(Stage stage, ClienteDAO clienteDAO, NavigationService navigationService, boolean isInterfaccia1) {
        this.isOnlineMode = SessionController.getIsOnlineModeStatic();
        this.clienteDAO = clienteDAO;
        this.navigationService = navigationService;
        this.codiceUnivoco = caricaCodiceUnivoco();

        // 🔹 Usa il parametro `isInterfaccia1` per decidere la view
        if (isInterfaccia1) {
            RegistratiCliente1View offlineView = new RegistratiCliente1View(stage, this);
            this.viewRoot = offlineView.getRoot();
            offlineView.getRegistratiButton().setOnAction(e -> registraCliente(offlineView));
        } else {
            RegistratiCliente2View onlineView = new RegistratiCliente2View(stage, this);
            this.viewRoot = onlineView.getRoot();
            onlineView.getRegistratiButton().setOnAction(e -> registraCliente(onlineView));
        }

    }

    public Parent getViewRoot() {
        return viewRoot;
    }

    private String caricaCodiceUnivoco() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
            return properties.getProperty("codiceUnivoco", "");
        } catch (IOException e) {
            mostraMessaggioErrore("Errore nel caricamento del codice univoco.");
            return "";
        }
    }

    public void aggiornaStatoRegistratiButton(RegistratiCliente1View view) {
        boolean isFilled = !view.getUsernameField().getText().trim().isEmpty() &&
                !view.getEmailField().getText().trim().isEmpty() &&
                !view.getNomeField().getText().trim().isEmpty() &&
                !view.getCognomeField().getText().trim().isEmpty() &&
                !view.getPasswordField().getText().trim().isEmpty() &&
                !view.getConfirmPasswordField().getText().trim().isEmpty() &&
                !view.getCodiceUnivocoField().getText().trim().isEmpty();

        view.getRegistratiButton().setDisable(!isFilled);
    }

    private void registraCliente(Object view) {
        String username;
        String nome;
        String cognome;
        String password;
        String confirmPassword;
        String codiceInserito;
        String email;

        if (view instanceof RegistratiCliente1View) {
            RegistratiCliente1View offlineView = (RegistratiCliente1View) view;
            username = offlineView.getUsernameField().getText().trim();
            nome = offlineView.getNomeField().getText().trim();
            cognome = offlineView.getCognomeField().getText().trim();
            password = offlineView.getPasswordField().getText().trim();
            confirmPassword = offlineView.getConfirmPasswordField().getText().trim();
            codiceInserito = offlineView.getCodiceUnivocoField().getText().trim();
            email = offlineView.getEmailField().getText().trim();
        } else {
            RegistratiCliente2View onlineView = (RegistratiCliente2View) view;
            username = onlineView.getUsernameField().getText().trim();
            nome = onlineView.getNomeField().getText().trim();
            cognome = onlineView.getCognomeField().getText().trim();
            password = onlineView.getPasswordField().getText().trim();
            confirmPassword = onlineView.getConfirmPasswordField().getText().trim();
            codiceInserito = onlineView.getCodiceUnivocoField().getText().trim();
            email = onlineView.getEmailField().getText().trim();
        }

        // Controllo campi vuoti
        if (username.isEmpty() || nome.isEmpty() || cognome.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || codiceInserito.isEmpty() || email.isEmpty()) {
            mostraMessaggioErrore("Tutti i campi devono essere compilati!");
            return;
        }

        // Controllo lunghezza username
        if (username.length() < 8) {
            mostraMessaggioErrore("Lo username deve essere di almeno 8 caratteri!");
            return;
        }

        // Controllo formato email
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            mostraMessaggioErrore("Inserisci un'email valida!");
            return;
        }

        // Controllo password
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            mostraMessaggioErrore("La password deve essere di almeno 8 caratteri e contenere una maiuscola, una minuscola, un numero e un carattere speciale!");
            return;
        }

        // Controllo password coincidenti
        if (!password.equals(confirmPassword)) {
            mostraMessaggioErrore("Le password non coincidono!");
            return;
        }

        // Controllo codice univoco
        if (!codiceInserito.equals(codiceUnivoco)) {
            tentativiErrati++;
            if (tentativiErrati >= MAX_TENTATIVI) {
                mostraMessaggioErrore("Hai sbagliato il codice univoco per 3 volte. L'app verrà chiusa.");
                System.exit(0);
            } else {
                mostraMessaggioErrore("Codice univoco errato! Tentativi rimasti: " + (MAX_TENTATIVI - tentativiErrati));
            }
            return;
        }

        // Creazione e salvataggio del cliente
        Cliente nuovoCliente = new Cliente(username, nome, cognome, password, email);
        clienteDAO.saveCliente(nuovoCliente);

        if (isOnlineMode) {
            LOGGER.log(Level.INFO, "📧 Invio email di conferma a: {0}", email);
            EmailService.sendConfirmationEmail(email, username);
        }

        mostraMessaggioSuccesso(isOnlineMode ? "Cliente registrato con successo nel database! Email inviata." : "Cliente registrato offline!");
        navigationService.navigateToLogin(true, true);
    }

    public void aggiungiValidazioneUsername(TextField usernameField, Label usernameFeedback, VBox root) {
        usernameField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue.length() < 8) {
                usernameField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                usernameFeedback.setText("Lo username deve avere almeno 8 caratteri ⚠");
                usernameFeedback.setTextFill(Color.RED);
                if (!root.getChildren().contains(usernameFeedback)) {
                    root.getChildren().add(root.getChildren().indexOf(usernameField) + 1, usernameFeedback);
                }
            } else {
                usernameField.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
                usernameFeedback.setText("✅ Username valido");
                usernameFeedback.setTextFill(Color.GREEN);
                if (!root.getChildren().contains(usernameFeedback)) {
                    root.getChildren().add(root.getChildren().indexOf(usernameField) + 1, usernameFeedback);
                }
            }
        });

        usernameField.focusedProperty().addListener((obs, oldVal, isFocused) -> {
            if (!isFocused && usernameField.getText().length() >= 8) {
                root.getChildren().remove(usernameFeedback);
            }
        });
    }



    private void mostraMessaggioErrore(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    private void mostraMessaggioSuccesso(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successo");
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
}