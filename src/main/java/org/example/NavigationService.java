package org.example;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public interface NavigationService {
    void navigateToGestisciProdotti(Stage stage);
    void display(VBox root, String title, Stage stage);
}
