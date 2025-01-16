package org.example.view;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class View {
    private final VBox root;
    private final RadioButton offlineOption;
    private final RadioButton onlineOption;
    private final Button loginButton;
    private final Button registratiButton;
    private final Button loginPersonaleButton;

    public View() {
        root = new VBox(15);

        offlineOption = new RadioButton("Entra Offline");
        onlineOption = new RadioButton("Entra Online");
        ToggleGroup toggleGroup = new ToggleGroup();
        offlineOption.setToggleGroup(toggleGroup);
        onlineOption.setToggleGroup(toggleGroup);

        loginButton = new Button("Login");
        registratiButton = new Button("Registrati");
        loginPersonaleButton = new Button("LOGIN PERSONALE");

        root.getChildren().addAll(offlineOption, onlineOption, loginButton, registratiButton, loginPersonaleButton);
        root.setAlignment(Pos.CENTER);
    }

    public void setOnlineOptionEnabled(boolean enabled) {
        onlineOption.setDisable(!enabled);
        if (!enabled) {
            onlineOption.setSelected(false);
        }
    }

    public VBox getRoot() {
        return root;
    }

    public RadioButton getOfflineOption() {
        return offlineOption;
    }

    public RadioButton getOnlineOption() {
        return onlineOption;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Button getRegistratiButton() {
        return registratiButton;
    }

    public Button getLoginPersonaleButton() {
        return loginPersonaleButton;
    }

    public List<javafx.scene.Node> getComponents() {
        return new ArrayList<>(root.getChildren());
    }
}