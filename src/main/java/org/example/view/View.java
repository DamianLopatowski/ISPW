package org.example.view;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.service.NavigationService;
import java.util.logging.Logger;

public class View {
    private final VBox root;
    private final RadioButton interfaccia1Option;
    private final RadioButton interfaccia2Option;
    private final Button loginButton;
    private NavigationService navigationService; // Aggiunto NavigationService
    private static final Logger LOGGER = Logger.getLogger(View.class.getName());




    public View() {
        root = new VBox(15);

        interfaccia1Option = new RadioButton("Interfaccia 1");
        interfaccia2Option = new RadioButton("Interfaccia 2");
        ToggleGroup toggleGroup = new ToggleGroup();
        interfaccia1Option.setToggleGroup(toggleGroup);
        interfaccia2Option.setToggleGroup(toggleGroup);

        loginButton = new Button("Login");
        root.getChildren().addAll(interfaccia1Option, interfaccia2Option, loginButton);
        root.setAlignment(Pos.CENTER);

        setupHandlers();
    }

    // ✅ Metodo per impostare il NavigationService
    public void setNavigationService(NavigationService navigationService) {
        this.navigationService = navigationService;
    }

    private void setupHandlers() {
        loginButton.setOnAction(event -> {
            if (navigationService == null) {
                LOGGER.warning("❌ ERRORE: NavigationService è NULL!");
                return;
            }

            boolean isInterfaccia1 = interfaccia1Option.isSelected();
            navigationService.navigateToLogin(isInterfaccia1);
        });
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
}
