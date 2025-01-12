package org.example;

import javafx.stage.Stage;

public class PageNavigator {

    public static void navigateToGestionePage(Stage primaryStage) {
        GestionePage gestionePage = new GestionePage();
        gestionePage.start(primaryStage);
    }

    public static void navigateToGestisciProdottiPage(Stage primaryStage) {
        GestisciProdottiPage gestisciProdottiPage = new GestisciProdottiPage();
        gestisciProdottiPage.start(primaryStage);
    }
}
