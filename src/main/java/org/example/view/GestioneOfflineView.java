package org.example.view;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class GestioneOfflineView {
    private final VBox root;
    private final ComboBox<String> menuTendina;
    private final Button confermaButton;
    private final Button logoutButton;

    public GestioneOfflineView() {
        root = new VBox(15);

        // Menu a tendina con le opzioni disponibili
        menuTendina = new ComboBox<>();
        menuTendina.getItems().addAll("Gestione Prodotti", "Gestione Soglia", "Gestione Spedizioni");
        menuTendina.setValue("Gestione Prodotti"); // Imposta un valore di default

        // Bottone di conferma
        confermaButton = new Button(menuTendina.getValue()); // Imposta il nome in base alla selezione iniziale

        // Cambio dinamico del nome del bottone
        menuTendina.setOnAction(e -> confermaButton.setText(menuTendina.getValue()));

        // Bottone di logout
        logoutButton = new Button("Logout");

        root.getChildren().addAll(menuTendina, confermaButton, logoutButton);
    }

    public VBox getRoot() {
        return root;
    }

    public ComboBox<String> getMenuTendina() {
        return menuTendina;
    }

    public Button getConfermaButton() {
        return confermaButton;
    }

    public Button getLogoutButton() {
        return logoutButton;
    }
}
