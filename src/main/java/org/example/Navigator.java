package org.example;

import javafx.stage.Stage;

public class Navigator {

    private Stage primaryStage;

    public Navigator(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Metodo per navigare alla pagina Gestione
    public void showGestionePage() {
        GestionePage gestionePage = new GestionePage(this);  // Passa il Navigator
        gestionePage.start(primaryStage);
    }

    // Metodo per navigare alla pagina Gestisci Prodotti
    public void showGestisciProdottiPage() {
        GestisciProdottiPage gestisciProdottiPage = new GestisciProdottiPage(this);  // Passa il Navigator
        gestisciProdottiPage.start(primaryStage);
    }

    public void showMainPage() {
        LoginPage loginPage = new LoginPage();
        loginPage.showMainPage(primaryStage);  // Passa lo Stage per visualizzare la pagina principale
    }
}
