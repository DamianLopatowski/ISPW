package org.example.controller;

import javafx.stage.Stage;
import org.example.view.GestioneView;
import org.example.view.View;

import java.util.logging.Logger;

public class GestioneController {
    private static final Logger LOGGER = Logger.getLogger(GestioneController.class.getName());
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
        gestioneView.getGestioneProdottiButton().setOnAction(event ->
                LOGGER.info("Apertura gestione prodotti...")
        );

        gestioneView.getGestioneSogliaButton().setOnAction(event ->
                LOGGER.info("Apertura gestione soglia...")
        );

        gestioneView.getGestioneSpedizioniButton().setOnAction(event ->
                LOGGER.info("Apertura gestione spedizioni...")
        );

        gestioneView.getLogoutButton().setOnAction(event ->
                stage.getScene().setRoot(mainView.getRoot())
        );
    }

    public GestioneView getGestioneView() {
        return gestioneView;
    }
}
