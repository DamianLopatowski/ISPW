package org.example.view;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ProfiloView1 {
    private final VBox root;
    private final TextField nomeField = new TextField();
    private final TextField cognomeField = new TextField();
    private final TextField usernameField = new TextField();
    private final PasswordField oldPasswordField = new PasswordField();
    private final PasswordField newPasswordField = new PasswordField();
    private final PasswordField confirmPasswordField = new PasswordField();
    private final Button saveButton = new Button("Salva Modifiche");
    private final Button backButton = new Button("Torna Indietro");

    public ProfiloView1() {
        root = new VBox(10);
        root.setPadding(new Insets(20));

        root.getChildren().addAll(
                new Label("Nome:"), nomeField,
                new Label("Cognome:"), cognomeField,
                new Label("Username:"), usernameField,
                new Label("Vecchia Password:"), oldPasswordField,
                new Label("Nuova Password:"), newPasswordField,
                new Label("Conferma Nuova Password:"), confirmPasswordField,
                saveButton, backButton
        );
    }

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
