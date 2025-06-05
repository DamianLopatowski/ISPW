package org.example.controllergrafici;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.example.controllerapplicativo.SessionController;
import org.example.dao.ProdottoDAOImpl;
import org.example.model.Cliente;
import org.example.model.Prodotto;
import org.example.service.EmailService;
import org.example.service.NavigationService;
import org.example.service.OrdineService;
import org.example.view.NegozioView1;
import org.example.view.NegozioView2;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.logging.Logger;

public class NegozioController {
    private static final Logger logger = Logger.getLogger(NegozioController.class.getName());
    private final Object view;
    private final ProdottoDAOImpl prodottoDAO;
    private final NavigationService navigationService;
    private final OrdineService ordineService;
    private final Cliente cliente;
    private final Map<Prodotto, Integer> carrello;

    public NegozioController(boolean isOnlineMode, boolean isInterfaccia1, NavigationService navigationService) {
        this.view = isInterfaccia1 ? new NegozioView1() : new NegozioView2();
        this.prodottoDAO = new ProdottoDAOImpl(isOnlineMode);
        this.navigationService = navigationService;
        this.ordineService = new OrdineService();
        this.cliente = navigationService.getClienteLoggato();
        this.carrello = SessionController.getCarrello();

        aggiornaListaProdotti();
        aggiornaCarrello(); // mostra il carrello se già pieno

        if (view instanceof NegozioView1) {
            NegozioView1 v1 = (NegozioView1) view;
            v1.getInviaOrdineButton().setOnAction(e -> handleConfermaOrdine());
            v1.getLogoutButton().setOnAction(e -> {
                SessionController.svuotaCarrello(); // 🔁 Svuota il carrello
                navigationService.navigateToMainView();
            });
            v1.getProfiloButton().setOnAction(e -> navigationService.navigateToProfilo());
        }

        if (view instanceof NegozioView2) {
            NegozioView2 v2 = (NegozioView2) view;
            v2.getInviaOrdineButton().setOnAction(e -> handleConfermaOrdine());
            v2.getLogoutButton().setOnAction(e -> navigationService.navigateToMainView());
            v2.getProfiloButton().setOnAction(e -> navigationService.navigateToProfilo());
        }
    }

    private void inviaOrdine() {
        boolean isOnline = prodottoDAO instanceof ProdottoDAOImpl && ((ProdottoDAOImpl) prodottoDAO).isOnline();

        // 🔁 Riduzione quantità per ciascun prodotto ordinato
        for (Map.Entry<Prodotto, Integer> entry : carrello.entrySet()) {
            Prodotto prodotto = entry.getKey();
            int quantita = entry.getValue();

            prodottoDAO.riduciQuantita(prodotto.getId(), quantita); // ✅ gestisce online e offline
            logger.info("🛒 Ordinato: " + prodotto.getNome() + " x" + quantita);
        }

        // 💾 Salva ordine (solo online, simulazione)
        if (isOnline) {
            ordineService.salvaOrdineOnline(cliente, carrello);
        }

        // ✉️ Invia email di riepilogo ordine
        EmailService.sendOrderSummaryEmail(
                cliente.getEmail(),
                cliente.getNome(),
                new HashMap<>(carrello) // passa una copia per evitare problemi dopo lo svuotamento
        );

        // 🧹 Svuota carrello e aggiorna interfaccia
        SessionController.svuotaCarrello();
        carrello.clear(); // ridondante, ma sicuro
        aggiornaCarrello();
        aggiornaListaProdotti();
    }


    public void handleConfermaOrdine() {
        if (cliente == null) {
            logger.warning("❌ Cliente non presente! Ordine annullato.");
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
        for (Map.Entry<Prodotto, Integer> entry : carrello.entrySet()) {
            Prodotto prodotto = entry.getKey();
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

        // 🔽 Pulsanti dell'Alert
        ButtonType annulla = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType modifica = new ButtonType("Modifica Spedizione");
        ButtonType conferma = new ButtonType("Conferma Ordine");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, msg, annulla, modifica, conferma);
        alert.setTitle("Conferma Ordine");
        alert.setHeaderText("Vuoi procedere con l’ordine?");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == annulla) {
                // ordine annullato, non fare nulla
            } else if (result.get() == modifica) {
                boolean isInterfaccia1 = SessionController.getIsInterfaccia1Static();
                navigationService.navigateToProfilo(isInterfaccia1);
            } else if (result.get() == conferma) {
                ordineService.procediOrdine();
                inviaOrdine();
                showAlert("✅ Ordine inviato con successo!");
            }
        }
    }

    private void aggiornaListaProdotti() {
        if (view instanceof NegozioView1) {
            NegozioView1 v1 = (NegozioView1) view;
            FlowPane contenitore = v1.getFlowPaneProdotti();
            contenitore.getChildren().clear();

            for (Prodotto p : prodottoDAO.getAllProdotti()) {
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
            Map<String, Prodotto> prodottiMap = new HashMap<>();

            for (Prodotto p : prodottoDAO.getAllProdotti()) {
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

    private void aggiornaDettagliProdotto(String nome, Map<String, Prodotto> prodottiMap, NegozioView2 v2) {
        Prodotto selezionato = prodottiMap.get(nome);
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

        if (view instanceof NegozioView1) {
            NegozioView1 v1 = (NegozioView1) view;
            righeBox = v1.getRigheCarrelloBox();
        } else if (view instanceof NegozioView2) {
            NegozioView2 v2 = (NegozioView2) view;
            righeBox = v2.getRigheCarrelloBox();
        } else {
            return;
        }

        righeBox.getChildren().clear();

        for (Map.Entry<Prodotto, Integer> entry : carrello.entrySet()) {
            righeBox.getChildren().add(new Label(entry.getKey().getNome() + " x" + entry.getValue()));
        }
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
