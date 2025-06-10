package org.example.controllergrafici;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.bean.ClienteBean;
import org.example.bean.OrdineBean;
import org.example.bean.PagamentoBean;
import org.example.controllerapplicativo.SessionController;
import org.example.dao.OrdineDAO;
import org.example.dao.OrdineDAOImpl;
import org.example.dao.PagamentoDAO;
import org.example.dao.PagamentoDAOImpl;
import org.example.facade.ClienteFacade;
import org.example.service.*;
import org.example.view.GestioneSpedizioniView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GestioneSpedizioniController {

    private final GestioneSpedizioniView view;
    private final boolean isOnline;
    private final OrdineDAO ordineDAO;
    private final PagamentoService pagamentoService;
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final NavigationService navigationService;

    public GestioneSpedizioniController(GestioneSpedizioniView view, OrdineDAO ordineDAO, NavigationService navigationService) {
        this.view = view;
        this.navigationService = navigationService;
        this.isOnline = SessionController.getIsOnlineModeStatic();

        this.ordineDAO = (ordineDAO != null) ? ordineDAO : new OrdineDAOImpl(false);
        this.pagamentoService = new PagamentoService(new PagamentoDAOImpl(isOnline));

        setupListeners();
        caricaOrdini();
        caricaRiepilogoClienti();
    }

    public Parent getView() {
        return view.getRoot();
    }

    private void setupListeners() {
        view.getAggiornaButton().setOnAction(e -> {
            caricaOrdini();
            caricaRiepilogoClienti();
        });

        view.getSegnaSpeditoButton().setOnAction(e -> {
            OrdineTableRow selezionato = view.getOrdiniTable().getSelectionModel().getSelectedItem();
            if (selezionato == null) {
                showAlert(Alert.AlertType.WARNING, "Nessun ordine selezionato");
                return;
            }

            String codice = view.getCodiceSpedizioneField().getText().trim();
            if (codice.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Inserisci un codice di spedizione");
                return;
            }

            OrdineBean ordine = selezionato.toOrdineBean();
            ordine.setSpedito(true);
            ordine.setCodiceSpedizione(codice);

            ordineDAO.aggiornaStatoSpedizione(ordine.toOrdine());

            PagamentoDAO pagamentoDAO = new PagamentoDAOImpl(SessionController.getIsOnlineModeStatic());
            ClienteFacade facade = new ClienteFacade(pagamentoDAO);
            facade.inviaConfermaSpedizione(ordine.getCliente(), codice);

            showAlert(Alert.AlertType.INFORMATION, "Ordine marcato come spedito e email inviata!");
            caricaOrdini();
            caricaRiepilogoClienti();
        });

        view.getRegistraBonificoButton().setOnAction(e -> {
            String username = view.getClienteBonificoComboBox().getValue();
            if (username == null || username.trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Seleziona un cliente a cui assegnare il bonifico");
                return;
            }

            String input = view.getCampoImportoBonifico().getText().trim();
            double importo;
            try {
                importo = Double.parseDouble(input);
                if (importo <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Importo non valido");
                return;
            }

            PagamentoBean pagamento = new PagamentoBean();
            pagamento.setClienteUsername(username);
            pagamento.setImporto(importo);
            pagamento.setDataPagamento(LocalDateTime.now());

            pagamentoService.registraPagamento(pagamento.toPagamento());
            showAlert(Alert.AlertType.INFORMATION, "Bonifico registrato con successo!");

            caricaRiepilogoClienti();

            List<OrdineBean> ordiniCliente = ordineDAO.getOrdiniPerCliente(username).stream()
                    .map(o -> {
                        OrdineBean bean = new OrdineBean();
                        bean.setId(o.getId());
                        bean.setCliente(new ClienteBean()); // minimamente riempito per ottenere email
                        bean.getCliente().setEmail(o.getCliente().getEmail());
                        bean.getCliente().setNome(o.getCliente().getNome());
                        bean.setTotale(o.getTotale());
                        return bean;
                    }).collect(Collectors.toList());

            double totaleOrdini = ordiniCliente.stream().mapToDouble(OrdineBean::getTotale).sum();
            double totalePagato = pagamentoService.getPagamentiPerCliente(username).stream()
                    .mapToDouble(p -> new PagamentoBean() {{
                        setImporto(p.getImporto());
                    }}.getImporto()).sum();
            double residuo = totaleOrdini - totalePagato;

            if (!ordiniCliente.isEmpty()) {
                ClienteBean cliente = ordiniCliente.get(0).getCliente();
                PagamentoDAO pagamentoDAO = new PagamentoDAOImpl(SessionController.getIsOnlineModeStatic());
                ClienteFacade facade = new ClienteFacade(pagamentoDAO);
                facade.inviaConfermaPagamento(cliente, importo, totalePagato, residuo);
            }
        });

        view.getOrdiniTable().getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                view.getCodiceSpedizioneField().setText(newVal.getCodiceSpedizione());
            }
        });

        view.getTornaIndietroButton().setOnAction(e -> {
            boolean isInterfaccia1 = SessionController.getIsInterfaccia1Static();
            boolean isOffline = !SessionController.getIsOnlineModeStatic();

            Parent gestioneRoot = navigationService.navigateToGestioneView(isOffline, isInterfaccia1);
            if (gestioneRoot != null) {
                Stage stage = (Stage) view.getRoot().getScene().getWindow();
                stage.setScene(new Scene(gestioneRoot, 1100, 700));
                stage.setTitle("Gestione");
            } else {
                showAlert(Alert.AlertType.ERROR, "Errore nel caricamento della schermata di gestione.");
            }
        });
    }

    private void caricaOrdini() {
        List<OrdineBean> ordiniBean;
        try {
            ordiniBean = ordineDAO.getTuttiGliOrdini().stream()
                    .map(o -> {
                        OrdineBean bean = new OrdineBean();
                        ClienteBean clienteBean = new ClienteBean();
                        clienteBean.setUsername(o.getCliente().getUsername());
                        clienteBean.setNome(o.getCliente().getNome());
                        clienteBean.setEmail(o.getCliente().getEmail());
                        clienteBean.setIndirizzo(o.getCliente().getIndirizzo());
                        clienteBean.setCivico(o.getCliente().getCivico());
                        clienteBean.setCap(o.getCliente().getCap());
                        clienteBean.setCitta(o.getCliente().getCitta());

                        bean.setCliente(clienteBean);
                        bean.setTotale(o.getTotale());
                        bean.setData(o.getData());
                        bean.setCodiceSpedizione(o.getCodiceSpedizione());
                        bean.setSpedito(o.isSpedito());
                        return bean;
                    }).collect(Collectors.toList());
        } catch (Exception e) {
            logger.severe("Errore nel caricamento ordini: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Errore nel caricamento degli ordini.");
            return;
        }

        ObservableList<OrdineTableRow> rows = FXCollections.observableArrayList();
        for (OrdineBean ob : ordiniBean) {
            rows.add(new OrdineTableRow(ob));
        }

        view.getOrdiniTable().setItems(rows);
    }

    private void caricaRiepilogoClienti() {
        List<String> utenti;
        try {
            utenti = ordineDAO.getTuttiClientiConOrdini();
        } catch (Exception e) {
            logger.severe("Errore nel recuperare i clienti con ordini: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Errore nel recuperare i clienti.");
            return;
        }

        List<ClienteRiepilogoRow> riepiloghi = utenti.stream().map(username -> {
            List<OrdineBean> ordiniCliente = ordineDAO.getOrdiniPerCliente(username).stream()
                    .map(o -> {
                        OrdineBean ob = new OrdineBean();
                        ClienteBean clienteBean = new ClienteBean();
                        clienteBean.setUsername(o.getCliente().getUsername());
                        clienteBean.setNome(o.getCliente().getNome());
                        clienteBean.setEmail(o.getCliente().getEmail());
                        ob.setCliente(clienteBean);
                        ob.setTotale(o.getTotale());
                        return ob;
                    }).collect(Collectors.toList());

            double totaleOrdini = ordiniCliente.stream().mapToDouble(OrdineBean::getTotale).sum();
            double totalePagato = pagamentoService.getPagamentiPerCliente(username).stream()
                    .mapToDouble(p -> {
                        PagamentoBean pb = new PagamentoBean();
                        pb.setImporto(p.getImporto());
                        return pb.getImporto();
                    }).sum();

            ClienteBean cliente = ordiniCliente.isEmpty() ? new ClienteBean() : ordiniCliente.get(0).getCliente();
            return new ClienteRiepilogoRow(cliente.getUsername(), cliente.getNome(), totaleOrdini, totalePagato);
        }).collect(Collectors.toList());

        view.getTabellaRiepilogoClienti().getItems().setAll(riepiloghi);
        view.getClienteBonificoComboBox().getItems().setAll(utenti);
    }

    private void showAlert(Alert.AlertType tipo, String messaggio) {
        new Alert(tipo, messaggio, ButtonType.OK).showAndWait();
    }
}