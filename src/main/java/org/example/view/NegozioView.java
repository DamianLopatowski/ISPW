package org.example.view;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class NegozioView {
    private final VBox root;
    private final Label welcomeLabel;


    public NegozioView() {
        root = new VBox(15);
        welcomeLabel = new Label("Benvenuto nel negozio!");
        root.getChildren().add(welcomeLabel);
    }

    public VBox getRoot() {
        return root;
    }

}
