package org.example.controllergrafici;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.bean.ClienteBean;
import org.example.bean.ProdottoBean;
import org.example.controllerapplicativo.SessionController;
import org.example.dao.OrdineDAOImpl;
import org.example.dao.PagamentoDAOImpl;
import org.example.dao.ProdottoDAOImpl;
import org.example.model.Prodotto;
import org.example.service.ClienteMapper;
import org.example.service.NavigationService;
import org.example.service.OrdineService;
import org.example.view.NegozioView1;
import org.example.view.NegozioView2;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class NegozioController {
    private static final Logger logger = Logger.getLogger(NegozioController.class.getName());
    private final Object view;
    private final ProdottoDAOImpl prodottoDAO;
    private final NavigationService navigationService;
    private final OrdineService ordineService;
    private final ClienteBean cliente;
    private final Map<ProdottoBean, Integer> carrello;

    public NegozioController(boolean isOnlineMode, boolean isInterfaccia1, NavigationService navigationService) {
        this.view = isInterfaccia1 ? new NegozioView1() : new NegozioView2();
        this.prodottoDAO = new ProdottoDAOImpl(isOnlineMode);
        this.navigationService = navigationService;
        this.ordineService = new OrdineService(navigationService);
        this.cliente = ClienteMapper.toBean(navigationService.getClienteLoggato());
        this.carrello = SessionController.getCarrello();

        aggiornaListaProdotti();
        aggiornaCarrello(); // mostra il carrello se già pieno

        if (view instanceof NegozioView1) {
            NegozioView1 v1 = (NegozioView1) view;
            v1.getInviaOrdineButton().setOnAction(e -> handleConfermaOrdine());
            v1.getLogoutButton().setOnAction(e -> {
                SessionController.svuotaCarrello();
                navigationService.navigateToMainView();
            });
            v1.getProfiloButton().setOnAction(e -> navigationService.navigateToProfilo());
            v1.getVisualizzaPagamentiButton().setOnAction(e -> {
                boolean isOnline = prodottoDAO.isOnline();
                PagamentoDAOImpl pagamentoDAO = new PagamentoDAOImpl(isOnline);
                OrdineDAOImpl ordineDAO = new OrdineDAOImpl(isOnline);
                PagamentiController controller = new PagamentiController(pagamentoDAO, ordineDAO, navigationService);
                Parent root = controller.getRoot();
                Stage stage = (Stage) v1.getRoot().getScene().getWindow();
                stage.setScene(new Scene(root, 600, 500));
                stage.setTitle("Storico Pagamenti");
            });
        }

        if (view instanceof NegozioView2) {
            NegozioView2 v2 = (NegozioView2) view;
            v2.getInviaOrdineButton().setOnAction(e -> handleConfermaOrdine());
            v2.getLogoutButton().setOnAction(e -> navigationService.navigateToMainView());
            v2.getProfiloButton().setOnAction(e -> navigationService.navigateToProfilo());
            v2.getVisualizzaPagamentiButton().setOnAction(e -> {
                boolean isOnline = prodottoDAO.isOnline();
                PagamentoDAOImpl pagamentoDAO = new PagamentoDAOImpl(isOnline);
                OrdineDAOImpl ordineDAO = new OrdineDAOImpl(isOnline);
                PagamentiController controller = new PagamentiController(pagamentoDAO, ordineDAO, navigationService);
                Parent root = controller.getRoot();
                Stage stage = (Stage) v2.getRoot().getScene().getWindow();
                stage.setScene(new Scene(root, 600, 500));
                stage.setTitle("Storico Pagamenti");
            });
        }
    }

    public void handleConfermaOrdine() {
        if (cliente == null) {
            logger.warning("Cliente non presente! Ordine annullato.");
            showAlert("Errore: cliente non loggato.");
            return;
        }

        if (carrello.isEmpty()) {
            showAlert("Il carrello è vuoto. Aggiungi almeno un prodotto per procedere.");
            return;
        }

        // 🔽 Costruzione messaggio con riepilogo ordine
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
                    .append(" → €").append(String.format("%.2f", prezzoTotale)).append("\n");
        }

        msgBuilder.append("Totale: €").append(String.format("%.2f", totale));
        String msg = msgBuilder.toString();

        //Pulsanti dell'Alert
        ButtonType annulla = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType modifica = new ButtonType("Modifica Spedizione");
        ButtonType conferma = new ButtonType("Conferma Ordine");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, msg, annulla, modifica, conferma);
        alert.setTitle("Conferma Ordine");
        alert.setHeaderText("Vuoi procedere con l’ordine?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == annulla) {
                // ordine annullato
            } else if (result.get() == modifica) {
                boolean isInterfaccia1 = SessionController.getIsInterfaccia1Static();
                navigationService.navigateToProfilo(isInterfaccia1);
            } else if (result.get() == conferma) {
                ordineService.procediOrdine(); // salva ordine + pagamento offline se necessario

                // riduci quantità prodotti
                boolean isOnline = prodottoDAO.isOnline();
                for (Map.Entry<ProdottoBean, Integer> entry : carrello.entrySet()) {
                    prodottoDAO.riduciQuantita(entry.getKey().getId(), entry.getValue());
                    logger.log(Level.INFO, "🛒 Ordinato: {0} x{1}", new Object[]{entry.getKey().getNome(), entry.getValue()});
                }

                carrello.clear();
                aggiornaCarrello();
                aggiornaListaProdotti();

                showAlert("Ordine inviato con successo!");
            }
        }
    }

    private void aggiornaListaProdotti() {
        if (view instanceof NegozioView1) {
            NegozioView1 v1 = (NegozioView1) view;
            FlowPane contenitore = v1.getFlowPaneProdotti();
            contenitore.getChildren().clear();

            for (ProdottoBean p : prodottoDAO.getAllProdotti().stream().map(Prodotto::toBean).collect(Collectors.toList())) {
                VBox boxProdotto = new VBox(5);
                boxProdotto.setPadding(new Insets(10));
                boxProdotto.setStyle("-fx-border-color: lightgray; -fx-background-color: white;");
                boxProdotto.setPrefWidth(200);

                ImageView imgView = new ImageView();
                if (p.getImmagine() != null) {
                    Image img = new Image(new ByteArrayInputStream(p.getImmagine()));
                    imgView.setImage(img);
                    imgView.setFitHeight(80);
                    imgView.setPreserveRatio(true);
                }

                Label nome = new Label(p.getNome());
                Label prezzo = new Label("€" + p.getPrezzoVendita());
                Label disponibilita = new Label("Disponibili: " + p.getQuantita());

                Spinner<Integer> spinner = new Spinner<>(1, p.getQuantita(), 1);
                Button aggiungi = new Button("Aggiungi");

                aggiungi.setOnAction(e -> {
                    int quantita = spinner.getValue();
                    carrello.put(p, carrello.getOrDefault(p, 0) + quantita);
                    aggiornaCarrello();
                });

                boxProdotto.getChildren().addAll(imgView, nome, prezzo, disponibilita, spinner, aggiungi);
                contenitore.getChildren().add(boxProdotto);
            }

        } else if (view instanceof NegozioView2) {
            NegozioView2 v2 = (NegozioView2) view;
            ListView<String> lista = v2.getListaProdotti();
            lista.getItems().clear();
            Map<String, ProdottoBean> prodottiMap = new HashMap<>();

            for (ProdottoBean p : prodottoDAO.getAllProdotti().stream().map(Prodotto::toBean).collect(Collectors.toList())) {
                String nomeVisualizzato = p.getNome() + " - €" + p.getPrezzoVendita();
                prodottiMap.put(nomeVisualizzato, p);
                lista.getItems().add(nomeVisualizzato);
            }

            if (!lista.getItems().isEmpty()) {
                lista.getSelectionModel().selectFirst();
                aggiornaDettagliProdotto(lista.getSelectionModel().getSelectedItem(), prodottiMap, v2);
            }

            lista.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldVal, newVal) -> aggiornaDettagliProdotto(newVal, prodottiMap, v2)
            );
        }
    }

    private void aggiornaDettagliProdotto(String nome, Map<String, ProdottoBean> prodottiMap, NegozioView2 v2) {
        ProdottoBean selezionato = prodottiMap.get(nome);
        if (selezionato == null) return;

        if (selezionato.getImmagine() != null) {
            Image img = new Image(new ByteArrayInputStream(selezionato.getImmagine()));
            v2.getImageView().setImage(img);
        } else {
            v2.getImageView().setImage(null);
        }

        v2.getDisponibilitaLabel().setText("Disponibili: " + selezionato.getQuantita());

        v2.getAggiungiButton().setOnAction(e -> {
            try {
                int q = Integer.parseInt(v2.getQuantitaField().getText().trim());
                if (q > 0 && q <= selezionato.getQuantita()) {
                    carrello.put(selezionato, carrello.getOrDefault(selezionato, 0) + q);
                    aggiornaCarrello();
                } else {
                    showAlert("Quantità non valida");
                }
            } catch (NumberFormatException ex) {
                showAlert("Inserisci un numero valido");
            }
        });
    }


    private void aggiornaCarrello() {
        VBox righeBox;
        Label totaleLabel;

        if (view instanceof NegozioView1) {
            NegozioView1 v1 = (NegozioView1) view;
            righeBox = v1.getRigheCarrelloBox();
            totaleLabel = v1.getTotaleLabel();
        } else if (view instanceof NegozioView2) {
            NegozioView2 v2 = (NegozioView2) view;
            righeBox = v2.getRigheCarrelloBox();
            totaleLabel = v2.getTotaleLabel();
        } else {
            return;
        }

        righeBox.getChildren().clear();
        double totale = 0.0;

        for (Map.Entry<ProdottoBean, Integer> entry : carrello.entrySet()) {
            ProdottoBean prodotto = entry.getKey();
            int quantita = entry.getValue();
            double prezzoUnitario = prodotto.getPrezzoVendita();
            double subtotale = prezzoUnitario * quantita;
            totale += subtotale;

            Label nome = new Label(prodotto.getNome());
            nome.setWrapText(true);
            nome.setMaxWidth(130);
            nome.setStyle("-fx-font-weight: bold;");

            Label prezzo = new Label("Prezzo: €" + String.format("%.2f", prezzoUnitario));
            Label qta = new Label("Quantità: x" + quantita);
            Label subtot = new Label("Subtotale: €" + String.format("%.2f", subtotale));

            VBox infoRiga = new VBox(2, nome, prezzo, qta, subtot);
            infoRiga.setPadding(new Insets(5, 5, 2, 5));
            infoRiga.setStyle("-fx-alignment: CENTER_LEFT;");

            Button plus = new Button("➕");
            Button minus = new Button("➖");
            Button remove = new Button("❌");

            plus.setOnAction(e -> {
                SessionController.aggiungiAlCarrello(prodotto);
                aggiornaCarrello();
            });

            minus.setOnAction(e -> {
                SessionController.rimuoviUnitaDalCarrello(prodotto);
                aggiornaCarrello();
            });

            remove.setOnAction(e -> {
                SessionController.rimuoviDalCarrello(prodotto);
                aggiornaCarrello();
            });

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            HBox bottoniRiga = new HBox(10, spacer, plus, minus, remove);
            bottoniRiga.setPadding(new Insets(0, 5, 10, 5));
            bottoniRiga.setAlignment(Pos.CENTER_RIGHT);

            VBox bloccoProdotto = new VBox(2, infoRiga, bottoniRiga);
            bloccoProdotto.setStyle("-fx-border-color: #ddd; -fx-border-width: 0 0 1 0;");
            bloccoProdotto.setPadding(new Insets(3, 0, 3, 0));

            righeBox.getChildren().add(bloccoProdotto);
        }

        totaleLabel.setText("Totale: €" + String.format("%.2f", totale));
    }



    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.showAndWait();
    }

    public Parent getRootView() {
        if (view instanceof NegozioView1) return ((NegozioView1) view).getRoot();
        else if (view instanceof NegozioView2) return ((NegozioView2) view).getRoot();
        return null;
    }
}
