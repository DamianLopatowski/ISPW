package org.example.view;

import javafx.scene.control.*;
import javafx.scene.layout.*;

public class Gestione1View {
    private final VBox root;
    private final ComboBox<String> menuTendina;
    private final Button confermaButton;
    private final Button logoutButton;

    public Gestione1View() {
        root = new VBox(15);

        // Menu a tendina con le opzioni disponibili
        menuTendina = new ComboBox<>();
        menuTendina.getItems().addAll("Gestione Prodotti", "Gestione Soglia", "Gestione Spedizioni");

        // Bottone di conferma inizialmente disabilitato finché l'utente non sceglie un'opzione
        confermaButton = new Button("Seleziona un'opzione");
        confermaButton.setDisable(true);

        // Attiva il bottone e aggiorna il testo quando viene selezionata un'opzione
        menuTendina.setOnAction(e -> {
            String selected = menuTendina.getValue();
            if (selected != null) {
                confermaButton.setText(selected);
                confermaButton.setDisable(false);
            }
        });

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