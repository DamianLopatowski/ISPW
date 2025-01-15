package org.example.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.view.GestioneView;
import org.example.view.View;

public class GestioneController {
    private final GestioneView gestioneView;
    private final Stage stage;
    private final View mainView;

    public GestioneController(Stage stage, View mainView) {
        this.stage = stage;
        this.mainView = mainView;
        this.gestioneView = new GestioneView();
        setupHandlers();
    }

    private void setupHandlers() {
        gestioneView.getGestioneProdottiButton().setOnAction(event -> {
            System.out.println("Apertura gestione prodotti...");
            // Logica per gestione prodotti
        });

        gestioneView.getGestioneSogliaButton().setOnAction(event -> {
            System.out.println("Apertura gestione soglia...");
            // Logica per gestione soglia
        });

        gestioneView.getGestioneSpedizioniButton().setOnAction(event -> {
            System.out.println("Apertura gestione spedizioni...");
            // Logica per gestione spedizioni
        });

        gestioneView.getLogoutButton().setOnAction(event -> {
            stage.getScene().setRoot(mainView.getRoot());
        });
    }

    public GestioneView getGestioneView() {
        return gestioneView;
    }
}
