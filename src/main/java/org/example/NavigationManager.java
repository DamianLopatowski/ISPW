package org.example;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class NavigationManager implements NavigationService {
    private final Stage stage;
    private final Logger logger = Logger.getLogger(NavigationManager.class.getName());

    public NavigationManager(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void navigateToGestisciProdotti(Stage stage) {
        GestisciProdottiPage gestisciProdottiPage = new GestisciProdottiPage();
        gestisciProdottiPage.start(stage);
    }

    @Override
    public void display(VBox root, String title, Stage stage) {
        try {
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            logger.warning("Errore durante il caricamento della vista: " + e.getMessage());
        }
    }
}
