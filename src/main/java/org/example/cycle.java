package org.example;

import javafx.stage.Stage;

public class cycle implements CycleInterface{
    public void showGestionePage(Stage primaryStage) {
        GestionePage gestionePage = new GestionePage();
        gestionePage.start(primaryStage);
    }
}
