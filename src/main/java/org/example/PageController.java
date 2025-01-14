package org.example;

import javafx.stage.Stage;

public class PageController {
    private Stage primaryStage;

    public PageController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showGestionePage() {
        GestionePage gestionePage = new GestionePage();
        gestionePage.start(primaryStage);
    }

    public void showGestisciProdottiPage() {
        GestisciProdottiPage gestisciProdottiPage = new GestisciProdottiPage();
        gestisciProdottiPage.start(primaryStage);
    }
}
