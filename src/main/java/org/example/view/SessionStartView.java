package org.example.view;

import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class SessionStartView {
    private final VBox root;
    private final RadioButton onlineOption;
    private final RadioButton offlineOption;
    private final Button confermaButton;

    public SessionStartView() {
        root = new VBox(15);

        onlineOption = new RadioButton("Avvia Sessione Online");
        offlineOption = new RadioButton("Avvia Sessione Offline");
        ToggleGroup group = new ToggleGroup();
        onlineOption.setToggleGroup(group);
        offlineOption.setToggleGroup(group);

        confermaButton = new Button("Conferma");
        confermaButton.setDisable(true);

        // Abilita il bottone solo quando un'opzione è selezionata
        group.selectedToggleProperty().addListener((obs, oldVal, newVal) ->
                confermaButton.setDisable(newVal == null)
        );

        root.getChildren().addAll(onlineOption, offlineOption, confermaButton);
    }

    public VBox getRoot() {
        return root;
    }

    public RadioButton getOnlineOption() {
        return onlineOption;
    }

    public RadioButton getOfflineOption() {
        return offlineOption;
    }

    public Button getConfermaButton() {
        return confermaButton;
    }
}
