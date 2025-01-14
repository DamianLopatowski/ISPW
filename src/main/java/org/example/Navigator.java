package org.example;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public interface Navigator {
    void navigateToMainPage();
    void navigateToGestisciProdotti();
    void navigateToGestionePage();
    void display(VBox root, String title, Stage stage);
}
