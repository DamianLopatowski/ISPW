package org.example;

import javafx.stage.Stage;

public class PageController {
    private final Stage primaryStage;
    private GestionePage gestionePage;
    private GestisciProdottiPage gestisciProdottiPage;

    public PageController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Configura le pagine da gestire tramite il controller.
     *
     * @param gestionePage          Istanza della pagina GestionePage
     * @param gestisciProdottiPage  Istanza della pagina GestisciProdottiPage
     */
    public void setPages(GestionePage gestionePage, GestisciProdottiPage gestisciProdottiPage) {
        this.gestionePage = gestionePage;
        this.gestisciProdottiPage = gestisciProdottiPage;

        if (this.gestionePage == null || this.gestisciProdottiPage == null) {
            throw new IllegalStateException("Le pagine non sono state configurate correttamente.");
        }

        System.out.println("GestionePage e GestisciProdottiPage configurate correttamente.");
    }

    /**
     * Mostra la pagina principale.
     */
    public void showMainPage() {
        LoginPage loginPage = new LoginPage();
        loginPage.showMainPage(primaryStage, this);
    }

    /**
     * Naviga verso GestionePage.
     */
    public void showGestionePage() {
        if (gestionePage != null) {
            System.out.println("Navigazione verso GestionePage...");
            gestionePage.start(primaryStage);
        } else {
            System.err.println("GestionePage è null!");
            throw new IllegalStateException("GestionePage non è stata configurata.");
        }
    }

    /**
     * Naviga verso GestisciProdottiPage.
     */
    public void showGestisciProdottiPage() {
        if (gestisciProdottiPage != null) {
            System.out.println("Navigazione verso GestisciProdottiPage...");
            gestisciProdottiPage.start(primaryStage);
        } else {
            System.err.println("GestisciProdottiPage è null!");
            throw new IllegalStateException("GestisciProdottiPage non è stata configurata.");
        }
    }
}
