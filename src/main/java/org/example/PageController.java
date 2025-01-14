package org.example;

import javafx.stage.Stage;

public class PageController {
    private final Stage primaryStage;

    public PageController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showGestionePage() {
        GestionePage gestionePage = new GestionePage(this);
        gestionePage.start(primaryStage);
    }

    public void showGestisciProdottiPage() {
        GestisciProdottiPage gestisciProdottiPage = new GestisciProdottiPage(this);
        gestisciProdottiPage.start(primaryStage);
    }
}
