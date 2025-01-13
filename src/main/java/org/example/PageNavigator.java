package org.example;

import javafx.stage.Stage;

public class PageNavigator {
    private Stage primaryStage;

    public PageNavigator(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void navigateToPage(String pageName, Stage primaryStage) {
        switch (pageName) {
            case "GestisciProdotti":
                GestisciProdottiPage gestisciProdottiPage = new GestisciProdottiPage();
                gestisciProdottiPage.start(primaryStage);  // Passa primaryStage correttamente
                break;
            // Aggiungi altre pagine se necessario
        }
    }
}
