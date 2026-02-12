package org.example.controllergrafici;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.bean.ClienteBean;
import org.example.bean.ProdottoBean;
import org.example.boundary.NegozioBoundary;
import org.example.boundary.NegozioView1Adapter;
import org.example.boundary.NegozioView2Adapter;
import org.example.controllerapplicativo.OrdineAppController;
import org.example.controllerapplicativo.SessionController;
import org.example.dao.OrdineDAOImpl;
import org.example.dao.PagamentoDAOImpl;
import org.example.dao.ProdottoDAOImpl;
import org.example.service.ClienteMapper;
import org.example.service.NavigationService;
import org.example.service.ProdottoMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class OrdineController {

    private static final Logger logger = Logger.getLogger(OrdineController.class.getName());

    private final NegozioBoundary view;
    private final ProdottoDAOImpl prodottoDAO;
    private final NavigationService navigationService;

    private final OrdineAppController ordineAppController;

    private final ClienteBean cliente;
    private final Map<ProdottoBean, Integer> carrello;

    public OrdineController(boolean isOnlineMode, boolean isInterfaccia1, NavigationService navigationService) {
        this.navigationService = navigationService;

        this.view = isInterfaccia1 ? new NegozioView1Adapter() : new NegozioView2Adapter();

        this.prodottoDAO = new ProdottoDAOImpl(isOnlineMode);
        this.ordineAppController = new OrdineAppController(navigationService);

        this.cliente = ClienteMapper.toBean(navigationService.getClienteLoggato());
        this.carrello = SessionController.getCarrello();

        bindEventHandlers();
        refreshAll();
    }

    private void bindEventHandlers() {

        view.setOnInviaOrdine(this::handleConfermaOrdine);

        view.setOnLogout(() -> {
            SessionController.svuotaCarrello();
            navigationService.navigateToMainView();
        });

        view.setOnProfilo(() -> navigationService.navigateToProfilo());

        view.setOnVisualizzaPagamenti(this::openPagamenti);
    }

    private void handleConfermaOrdine() {

        if (cliente == null) {
            logger.warning("Cliente non presente! Ordine annullato.");
            view.showInfo("Errore: cliente non loggato.");
            return;
        }

        if (carrello.isEmpty()) {
            view.showInfo("Il carrello è vuoto. Aggiungi almeno un prodotto per procedere.");
            return;
        }

        String riepilogo = buildRiepilogoOrdineUI();

        Optional<NegozioBoundary.ConfermaChoice> choice = view.showConfermaOrdine(riepilogo);
        if (choice.isEmpty()) return;

        switch (choice.get()) {
            case ANNULLA -> {
            }
            case MODIFICA_SPEDIZIONE -> {
                boolean isInterfaccia1 = SessionController.getIsInterfaccia1Static();
                navigationService.navigateToProfilo(isInterfaccia1);
            }
            case CONFERMA -> {
                String esito = ordineAppController.procediOrdine();

                refreshAll();
                view.showInfo(esito);
            }
        }
    }

    private String buildRiepilogoOrdineUI() {
        StringBuilder msgBuilder = new StringBuilder();
        msgBuilder.append("Dati di spedizione:\n")
                .append("Nome: ").append(cliente.getNome()).append("\n")
                .append("Cognome: ").append(cliente.getCognome()).append("\n")
                .append("Indirizzo: ").append(cliente.getIndirizzo()).append(", ").append(cliente.getCivico()).append("\n")
                .append("CAP: ").append(cliente.getCap()).append(" - ").append(cliente.getCitta()).append("\n\n");

        msgBuilder.append("Riepilogo ordine:\n");

        double totale = 0.0;
        for (Map.Entry<ProdottoBean, Integer> entry : carrello.entrySet()) {
            ProdottoBean prodotto = entry.getKey();
            int quantita = entry.getValue();
            double prezzoTotale = prodotto.getPrezzoVendita() * quantita;
            totale += prezzoTotale;

            msgBuilder.append("- ")
                    .append(prodotto.getNome())
                    .append(" x").append(quantita)
                    .append(" → €").append(String.format("%.2f", prezzoTotale))
                    .append("\n");
        }

        msgBuilder.append("Totale: €").append(String.format("%.2f", totale));
        return msgBuilder.toString();
    }

    private void refreshAll() {
        refreshProdotti();
        refreshCarrello();
    }

    private void refreshProdotti() {
        List<ProdottoBean> prodotti = prodottoDAO.getAllProdotti().stream()
                .map(ProdottoMapper::toBean)
                .collect(Collectors.toList());

        view.renderProdotti(prodotti, (prodotto, qty) -> {
            if (qty <= 0) {
                view.showInfo("Quantità non valida");
                return;
            }
            carrello.put(prodotto, carrello.getOrDefault(prodotto, 0) + qty);
            refreshCarrello();
        });
    }

    private void refreshCarrello() {

        view.renderCarrello(
                carrello,
                p -> {
                    SessionController.aggiungiAlCarrello(p);
                    refreshCarrello();
                },
                p -> {
                    SessionController.rimuoviUnitaDalCarrello(p);
                    refreshCarrello();
                },
                p -> {
                    SessionController.rimuoviDalCarrello(p);
                    refreshCarrello();
                }
        );

        double totale = 0.0;
        for (Map.Entry<ProdottoBean, Integer> entry : carrello.entrySet()) {
            totale += entry.getKey().getPrezzoVendita() * entry.getValue();
        }
        view.setTotale(totale);
    }

    private void openPagamenti() {
        boolean isOnline = prodottoDAO.isOnline();

        PagamentoDAOImpl pagamentoDAO = new PagamentoDAOImpl(isOnline);
        OrdineDAOImpl ordineDAO = new OrdineDAOImpl(isOnline);

        PagamentiController controller = new PagamentiController(pagamentoDAO, ordineDAO, navigationService);
        Parent root = controller.getRoot();

        Stage stage = (Stage) view.getRoot().getScene().getWindow();
        stage.setScene(new Scene(root, 600, 500));
        stage.setTitle("Storico Pagamenti");
    }

    public Parent getRootView() {
        return view.getRoot();
    }
}
