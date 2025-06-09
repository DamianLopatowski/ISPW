package org.example.controllergrafici;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.example.bean.ProdottoBean;
import org.example.controllerapplicativo.SessionController;
import org.example.dao.ProdottoDAO;
import org.example.service.NavigationService;
import org.example.service.ProdottoMapper;
import org.example.view.GestioneProdottiView;

import java.util.List;
import java.util.stream.Collectors;

public class GestioneProdottiController {
    private final GestioneProdottiView view;
    private final ProdottoDAO prodottoDAO;
    private final NavigationService navigationService;

    public GestioneProdottiController(ProdottoDAO prodottoDAO, NavigationService navigationService) {
        this.view = new GestioneProdottiView();
        this.prodottoDAO = prodottoDAO;
        this.navigationService = navigationService;

        caricaProdotti();
        configuraAzioni();
    }

    private void caricaProdotti() {
        List<ProdottoBean> prodotti = prodottoDAO.getAll().stream()
                .map(ProdottoMapper::toBean)
                .collect(Collectors.toList());
        view.mostraProdotti(prodotti);
    }

    private void configuraAzioni() {
        view.getAggiungiProdottoButton().setOnAction(e -> {
            ProdottoBean nuovo = view.creaProdottoDaInput();
            if (nuovo != null) {
                prodottoDAO.saveProdotto(nuovo.toModel());
                caricaProdotti();
                view.pulisciCampiInput();
            }
        });

        view.getAumentaQuantitaButton().setOnAction(e -> {
            ProdottoBean selezionato = view.getProdottoSelezionato();
            if (selezionato != null) {
                try {
                    int quantita = Integer.parseInt(view.getModificaQuantitaField().getText());
                    if (quantita > 0) {
                        int nuovaQuantita = selezionato.getQuantita() + quantita;
                        selezionato.setQuantita(nuovaQuantita);
                        prodottoDAO.aggiornaQuantita(selezionato.getId(), nuovaQuantita);
                        caricaProdotti();
                    } else {
                        mostraErrore("La quantità deve essere positiva");
                    }
                } catch (NumberFormatException ex) {
                    mostraErrore("Inserisci un numero valido per la quantità");
                }
            }
        });

        view.getDiminuisciQuantitaButton().setOnAction(e -> {
            ProdottoBean selezionato = view.getProdottoSelezionato();
            if (selezionato != null) {
                try {
                    int quantita = Integer.parseInt(view.getModificaQuantitaField().getText());
                    if (quantita > 0) {
                        int nuovaQuantita = Math.max(0, selezionato.getQuantita() - quantita);
                        selezionato.setQuantita(nuovaQuantita);
                        prodottoDAO.aggiornaQuantita(selezionato.getId(), nuovaQuantita);
                        caricaProdotti();
                    } else {
                        mostraErrore("La quantità deve essere positiva");
                    }
                } catch (NumberFormatException ex) {
                    mostraErrore("Inserisci un numero valido per la quantità");
                }
            }
        });

        view.getEliminaProdottoButton().setOnAction(e -> {
            ProdottoBean selezionato = view.getProdottoSelezionato();
            if (selezionato != null) {
                prodottoDAO.rimuoviProdotto(selezionato.getId());
                caricaProdotti();
            }
        });

        view.getTornaIndietroButton().setOnAction(e -> {
            boolean isOnline = SessionController.getIsOnlineModeStatic();
            boolean isInterfaccia1 = SessionController.getIsInterfaccia1Static();
            Parent root = navigationService.navigateToGestioneView(isOnline, isInterfaccia1);
            Stage stage = (Stage) view.getRoot().getScene().getWindow();
            stage.setScene(new Scene(root, 1100, 700));
        });
    }

    private void mostraErrore(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR, messaggio);
        alert.showAndWait();
    }

    public Parent getRoot() {
        return view.getRoot();
    }
}
