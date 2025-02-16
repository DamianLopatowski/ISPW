package org.example.controllerapplicativo;

import javafx.stage.Stage;
import org.example.ApplicationContext;
import org.example.controllergrafici.GestioneController;

public class NavigationController {
    private final Stage stage;
    private final ApplicationContext context;

    public NavigationController(Stage stage, ApplicationContext context) {
        this.stage = stage;
        this.context = context;
    }

    public void navigateToMainView() {
        stage.getScene().setRoot(context.getMainView().getRoot());
    }

    public void navigateToGestioneView() {
        GestioneController gestioneController = new GestioneController(stage, context);
        stage.getScene().setRoot(gestioneController.getGestioneView().getRoot());
    }
}
