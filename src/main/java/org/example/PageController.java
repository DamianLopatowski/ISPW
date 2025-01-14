package org.example;

import javafx.stage.Stage;

public class PageController {
    private Stage primaryStage;

    public PageController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    private void showGestionePage(Stage primaryStage, GestisciProdottiPage gestisciProdottiPage) {
        GestionePage gestionePage = new GestionePage(gestisciProdottiPage);
        gestionePage.start(primaryStage);
    }


    public void showGestisciProdottiPage() {
        GestisciProdottiPage gestisciProdottiPage = new GestisciProdottiPage();
        gestisciProdottiPage.start(primaryStage);
    }
}
