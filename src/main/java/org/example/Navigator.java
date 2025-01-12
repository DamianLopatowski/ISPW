package org.example;

import javafx.stage.Stage;

public class Navigator {

    private Stage primaryStage;

    public Navigator(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showGestionePage() {
        GestionePage gestionePage = new GestionePage(this);  // Passa il Navigator a GestionePage
        gestionePage.start(primaryStage);
    }

    public void showGestisciProdottiPage() {
        GestisciProdottiPage gestisciProdottiPage = new GestisciProdottiPage(this); // Passa il Navigator
        gestisciProdottiPage.start(primaryStage);
    }

    public void showMainPage() {
        // Dopo il login, la navigazione alla MainPage
        LoginPage loginPage = new LoginPage();
        loginPage.showMainPage(primaryStage);
    }
}

