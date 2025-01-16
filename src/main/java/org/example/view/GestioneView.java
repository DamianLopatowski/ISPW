package org.example.view;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class GestioneView {
    private final VBox root;
    private final Button gestioneProdottiButton;
    private final Button gestioneSogliaButton;
    private final Button gestioneSpedizioniButton;
    private final Button logoutButton;

    public GestioneView() {
        root = new VBox(15);
        gestioneProdottiButton = new Button("Gestione Prodotti");
        gestioneSogliaButton = new Button("Gestione Soglia");
        gestioneSpedizioniButton = new Button("Gestione Spedizioni");
        logoutButton = new Button("Logout");

        root.getChildren().addAll(gestioneProdottiButton, gestioneSogliaButton, gestioneSpedizioniButton, logoutButton);
        root.setSpacing(10);
    }

    public VBox getRoot() {
        return root;
    }

    public Button getGestioneProdottiButton() {
        return gestioneProdottiButton;
    }

    public Button getGestioneSogliaButton() {
        return gestioneSogliaButton;
    }

    public Button getGestioneSpedizioniButton() {
        return gestioneSpedizioniButton;
    }

    public Button getLogoutButton() {
        return logoutButton;
    }
}