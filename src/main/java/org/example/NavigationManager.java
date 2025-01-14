package org.example;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.logging.Logger;

public class NavigationManager implements NavigationService {

    private final MainPageNavigator mainPageNavigator;
    private final Stage stage;
    private final Logger logger = Logger.getLogger(NavigationManager.class.getName());

    public NavigationManager(Stage stage, MainPageNavigator mainPageNavigator) {
        this.stage = stage;
        this.mainPageNavigator = mainPageNavigator;
    }

    public void navigateToMainPage() {
        if (mainPageNavigator != null) {
            mainPageNavigator.showMainPage();
        } else {
            logger.warning("MainPageNavigator non è inizializzato. Impossibile navigare alla pagina principale.");
        }
    }

    // Restituisce lo stage principale
    public Stage getStage() {
        return stage;
    }

    @Override
    public void navigateToGestisciProdotti(Stage stage) {
        GestisciProdottiPage gestisciProdottiPage = new GestisciProdottiPage();
        gestisciProdottiPage.start(stage, mainPageNavigator); // Passa entrambi i parametri richiesti
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
