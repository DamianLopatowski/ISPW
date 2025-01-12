package org.example;

import javafx.stage.Stage;

public class Navigator {

    private Stage primaryStage;

    public Navigator(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showGestionePage() {
        GestionePage gestionePage = new GestionePage(this); // Passa Navigator a GestionePage
        gestionePage.start(primaryStage);
    }

    public void showGestisciProdottiPage() {
        GestisciProdottiPage gestisciProdottiPage = new GestisciProdottiPage(this); // Passa Navigator
        gestisciProdottiPage.start(primaryStage);
    }

    public void showMainPage() {
        LoginPage loginPage = new LoginPage();
        loginPage.showMainPage(primaryStage);  // Chiamata al metodo in LoginPage
    }
}

