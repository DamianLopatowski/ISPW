package org.example.controllerapplicativo;

import javafx.stage.Stage;
import org.example.ApplicationContext;
import org.example.controllergrafici.GestioneController;
import org.example.service.NavigationService;

public class NavigationController implements NavigationService {
    private final Stage stage;
    private final ApplicationContext context;

    public NavigationController(Stage stage, ApplicationContext context) {
        this.stage = stage;
        this.context = context;
    }

    @Override
    public void navigateToMainView() {
        stage.getScene().setRoot(context.getMainView().getRoot());
    }

    @Override
    public void navigateToGestioneView() {
        GestioneController gestioneController = new GestioneController(stage, context, this);
        stage.getScene().setRoot(gestioneController.getGestioneView().getRoot());
    }
}
