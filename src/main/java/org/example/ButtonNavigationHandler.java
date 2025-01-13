package org.example;

import javafx.stage.Stage;
import javafx.scene.control.Button;

public class ButtonNavigationHandler implements NavigationHandler {
    private Stage stage;

    public ButtonNavigationHandler(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void navigateTo(Page page) {
        page.start(stage);  // Gestisce la transizione verso la pagina
    }

    // Metodo che può essere usato per collegare i bottoni
    public void handleButtonAction(Button button, Page page) {
        button.setOnAction(e -> navigateTo(page));
    }
}
