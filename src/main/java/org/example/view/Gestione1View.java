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

        menuTendina = new ComboBox<>();
        menuTendina.getItems().addAll("Gestione Prodotti", "Gestione Soglia", "Gestione Spedizioni");

        confermaButton = new Button("Seleziona un'opzione");
        confermaButton.setDisable(true);

        menuTendina.setOnAction(e -> {
            String selected = menuTendina.getValue();
            if (selected != null) {
                confermaButton.setText(selected);
                confermaButton.setDisable(false);
            }
        });

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
