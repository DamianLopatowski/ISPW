package org.example.view;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class View {
    private final VBox root;
    private final RadioButton interfaccia1Option;
    private final RadioButton interfaccia2Option;
    private final Button loginButton;
    private final Button loginClienteButton;
    private final Button registratiClienteButton;
    public View() {
        root = new VBox(15);

        interfaccia1Option = new RadioButton("Interfaccia 1");
        interfaccia2Option = new RadioButton("Interfaccia 2");
        ToggleGroup toggleGroup = new ToggleGroup();
        interfaccia1Option.setToggleGroup(toggleGroup);
        interfaccia2Option.setToggleGroup(toggleGroup);

        loginButton = new Button("Login Personale");
        loginClienteButton = new Button("Login Cliente");
        registratiClienteButton = new Button("Registrati Cliente");

        root.getChildren().addAll(interfaccia1Option, interfaccia2Option, loginButton, loginClienteButton, registratiClienteButton);
        root.setAlignment(Pos.CENTER);
    }


    public VBox getRoot() {
        return root;
    }
    public RadioButton getInterfaccia1Option() {
        return interfaccia1Option;
    }
    public RadioButton getInterfaccia2Option() {
        return interfaccia2Option;
    }
    public Button getLoginButton() {
        return loginButton;
    }
    public Button getLoginClienteButton() {
        return loginClienteButton;
    }
    public Button getRegistratiClienteButton() {
        return registratiClienteButton;
    }
}