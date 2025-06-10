package org.example.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ProfiloView2 {
    private final VBox root;
    private final VBox datiPersonaliPane;
    private final VBox passwordPane;
    private final TextField nomeField = new TextField();
    private final TextField cognomeField = new TextField();
    private final TextField usernameField = new TextField();
    private final PasswordField oldPasswordField = new PasswordField();
    private final PasswordField newPasswordField = new PasswordField();
    private final PasswordField confirmPasswordField = new PasswordField();

    private final Button avantiButton = new Button("Avanti ➡");
    private final Button indietroButton = new Button("<- Indietro");
    private final Button saveButton = new Button("Salva");
    private final Button backButton = new Button("<- Torna al Negozio");

    public ProfiloView2() {
        root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #f0f0f0;");

        // Pannello 1: Dati personali
        datiPersonaliPane = new VBox(10);
        datiPersonaliPane.getChildren().addAll(
                new Label("Nome"), nomeField,
                new Label("Cognome"), cognomeField,
                new Label("Username"), usernameField,
                avantiButton
        );

        // Pannello 2: Password
        passwordPane = new VBox(10);
        passwordPane.getChildren().addAll(
                new Label("Password Attuale"), oldPasswordField,
                new Label("Nuova Password"), newPasswordField,
                new Label("Conferma Nuova Password"), confirmPasswordField,
                indietroButton, saveButton
        );

        // Mostra inizialmente il primo pannello
        root.getChildren().addAll(datiPersonaliPane, backButton);
        passwordPane.setVisible(false);

        // Gestione navigazione
        avantiButton.setOnAction(e -> {
            if (validateDatiPersonali()) {
                root.getChildren().remove(datiPersonaliPane);
                root.getChildren().add(0, passwordPane);
                passwordPane.setVisible(true);
            }
        });

        indietroButton.setOnAction(e -> {
            root.getChildren().remove(passwordPane);
            root.getChildren().add(0, datiPersonaliPane);
        });
    }

    private boolean validateDatiPersonali() {
        if (nomeField.getText().trim().isEmpty() ||
                cognomeField.getText().trim().isEmpty() ||
                usernameField.getText().trim().isEmpty()) {
            showAlert("❗ Compila tutti i campi prima di continuare");
            return false;
        }
        return true;
    }

    private void showAlert(String msg) {
        new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK).showAndWait();
    }

    // Getter per controller
    public Parent getRoot() { return root; }
    public TextField getNomeField() { return nomeField; }
    public TextField getCognomeField() { return cognomeField; }
    public TextField getUsernameField() { return usernameField; }
    public PasswordField getOldPasswordField() { return oldPasswordField; }
    public PasswordField getNewPasswordField() { return newPasswordField; }
    public PasswordField getConfirmPasswordField() { return confirmPasswordField; }
    public Button getSaveButton() { return saveButton; }
    public Button getBackButton() { return backButton; }
}
