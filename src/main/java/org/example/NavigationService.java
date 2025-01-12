package org.example;

import javafx.stage.Stage;

public class NavigationService {

    private Stage primaryStage;

    public NavigationService(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void navigateToGestionePage() {
        GestionePage gestionePage = new GestionePage();
        gestionePage.start(primaryStage);
    }

    public void navigateToGestisciProdottiPage() {
        GestisciProdottiPage gestisciProdottiPage = new GestisciProdottiPage();
        gestisciProdottiPage.start(primaryStage);
    }

    // Altri metodi di navigazione se necessario
}
