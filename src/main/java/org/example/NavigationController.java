package org.example;

import javafx.stage.Stage;

public class NavigationController {

    private Stage primaryStage;

    public NavigationController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Metodo per navigare alla GestionePage
    public void showGestionePage() {
        GestionePage gestionePage = new GestionePage();
        gestionePage.start(primaryStage);
    }

    // Metodo per navigare alla GestisciProdottiPage
    public void showGestisciProdottiPage() {
        GestisciProdottiPage gestisciProdottiPage = new GestisciProdottiPage();
        gestisciProdottiPage.start(primaryStage);
    }
}
