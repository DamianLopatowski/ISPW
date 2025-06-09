package org.example.controllergrafici;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import org.example.bean.ClienteBean;
import org.example.bean.OrdineBean;
import org.example.bean.PagamentoBean;
import org.example.controllerapplicativo.SessionController;
import org.example.dao.OrdineDAO;
import org.example.dao.PagamentoDAO;
import org.example.model.Ordine;
import org.example.model.Pagamento;
import org.example.service.NavigationService;
import org.example.service.OrdineTableRow;
import org.example.service.PagamentoService;
import org.example.service.PagamentoTableRow;
import org.example.view.PagamentiView;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PagamentiController {
    private final PagamentiView view;
    private final PagamentoService pagamentoService;
    private final OrdineDAO ordineDAO;
    private final NavigationService navigationService;
    private static final Logger LOGGER = Logger.getLogger(PagamentiController.class.getName());

    public PagamentiController(PagamentoDAO pagamentoDAO, OrdineDAO ordineDAO, NavigationService navigationService) {
        this.view = new PagamentiView();
        this.pagamentoService = new PagamentoService(pagamentoDAO);
        this.ordineDAO = ordineDAO;
        this.navigationService = navigationService;

        caricaDatiUtenteLoggato();
        configuraBottoneIndietro();
    }

    private void caricaDatiUtenteLoggato() {
        ClienteBean cliente = SessionController.getClienteLoggato().toBean();
        if (cliente == null) {
            LOGGER.warning("Nessun cliente loggato. Impossibile caricare i dati dei pagamenti.");
            return;
        }

        String username = cliente.getUsername();
        List<OrdineBean> ordini = ordineDAO.getOrdiniPerCliente(username).stream().map(Ordine::toBean).collect(Collectors.toList());
        List<PagamentoBean> pagamenti = pagamentoService.getPagamentiPerCliente(username).stream().map(Pagamento::toBean).collect(Collectors.toList());

        if (ordini.isEmpty()) {
            LOGGER.info("Nessun ordine trovato per " + username);
        }
        if (pagamenti.isEmpty()) {
            LOGGER.info("Nessun pagamento trovato per " + username);
        }

        double totaleOrdini = calcolaTotaleOrdini(ordini);
        double totalePagato = calcolaTotalePagamenti(pagamenti);
        double daPagare = totaleOrdini - totalePagato;

        aggiornaEtichette(totaleOrdini, totalePagato, daPagare);
        popolaTabellaOrdini(ordini);
        popolaTabellaPagamenti(pagamenti);
    }

    private double calcolaTotaleOrdini(List<OrdineBean> ordini) {
        return ordini.stream().mapToDouble(OrdineBean::getTotale).sum();
    }

    private double calcolaTotalePagamenti(List<PagamentoBean> pagamenti) {
        return pagamenti.stream().mapToDouble(PagamentoBean::getImporto).sum();
    }

    private void aggiornaEtichette(double totaleOrdini, double totalePagato, double daPagare) {
        view.getTotaleOrdiniLabel().setText("Totale ordini: € " + String.format("%.2f", totaleOrdini));
        view.getTotalePagatoLabel().setText("Totale pagato: € " + String.format("%.2f", totalePagato));
        view.getResiduoLabel().setText("Residuo da pagare: € " + String.format("%.2f", daPagare));
    }

    private void popolaTabellaOrdini(List<OrdineBean> ordini) {
        TableView<OrdineTableRow> tabella = view.getOrdiniTable();
        tabella.getItems().clear();
        for (OrdineBean ordine : ordini) {
            tabella.getItems().add(new OrdineTableRow(ordine));
        }
    }

    private void popolaTabellaPagamenti(List<PagamentoBean> pagamenti) {
        TableView<PagamentoTableRow> tabellaPagamenti = view.getPagamentiTable();
        tabellaPagamenti.getItems().clear();
        for (PagamentoBean pagamento : pagamenti) {
            tabellaPagamenti.getItems().add(new PagamentoTableRow(pagamento));
        }
    }

    private void configuraBottoneIndietro() {
        Button indietroButton = view.getTornaAlNegozioButton();
        indietroButton.setOnAction(e -> navigationService.navigateToNegozio());
    }

    public Parent getRoot() {
        return view.getRoot();
    }
}
