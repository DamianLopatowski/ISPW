package org.example.view;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.logging.Logger;

public class NegozioView {
    private final VBox root;
    private final Label welcomeLabel;
    private static final Logger LOGGER = Logger.getLogger(NegozioView.class.getName());


    public NegozioView() {
        root = new VBox(15);
        welcomeLabel = new Label("Benvenuto nel negozio!");
        root.getChildren().add(welcomeLabel);
    }

    public VBox getRoot() {
        return root;
    }

}
