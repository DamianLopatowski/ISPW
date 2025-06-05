package org.example.controllergrafici;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.example.dao.ClienteDAO;
import org.example.dao.ClienteDAOImpl;
import org.example.model.Cliente;
import org.example.service.NavigationService;
import org.example.controllerapplicativo.SessionController;
import org.example.view.ProfiloView1;
import org.example.view.ProfiloView2;

public class ProfiloController {
    private final Object view;
    private final NavigationService navigationService;

    public ProfiloController(boolean isOnlineMode, boolean isInterfaccia1, NavigationService navigationService) {
        this.view = isInterfaccia1 ? new ProfiloView1() : new ProfiloView2();
        this.navigationService = navigationService;

        if (view instanceof ProfiloView1) {
            ProfiloView1 v1 = (ProfiloView1) view;
            riempiCampi(v1);
            v1.getSaveButton().setOnAction(e -> salvaDati(v1));
            v1.getBackButton().setOnAction(e -> navigationService.navigateToNegozio());
        } else if (view instanceof ProfiloView2) {
            ProfiloView2 v2 = (ProfiloView2) view;
            riempiCampi(v2);
            v2.getSaveButton().setOnAction(e -> salvaDati(v2));
            v2.getBackButton().setOnAction(e -> navigationService.navigateToNegozio());
        }
    }

    private void riempiCampi(Object v) {
        Cliente cliente = navigationService.getClienteLoggato(); // usa cliente aggiornato

        if (v instanceof ProfiloView1) {
            ProfiloView1 pv = (ProfiloView1) v;
            pv.getNomeField().setText(cliente.getNome());
            pv.getCognomeField().setText(cliente.getCognome());
            pv.getUsernameField().setText(cliente.getUsername());
        } else if (v instanceof ProfiloView2) {
            ProfiloView2 pv = (ProfiloView2) v;
            pv.getNomeField().setText(cliente.getNome());
            pv.getCognomeField().setText(cliente.getCognome());
            pv.getUsernameField().setText(cliente.getUsername());
        }
    }

    private void salvaDati(Object v) {
        Cliente cliente = navigationService.getClienteLoggato(); // cliente aggiornato

        String nome, cognome, username, oldPwd, newPwd, confPwd;

        if (v instanceof ProfiloView1) {
            ProfiloView1 pv = (ProfiloView1) v;
            nome = pv.getNomeField().getText().trim();
            cognome = pv.getCognomeField().getText().trim();
            username = pv.getUsernameField().getText().trim();
            oldPwd = pv.getOldPasswordField().getText();
            newPwd = pv.getNewPasswordField().getText();
            confPwd = pv.getConfirmPasswordField().getText();
        } else {
            ProfiloView2 pv = (ProfiloView2) v;
            nome = pv.getNomeField().getText().trim();
            cognome = pv.getCognomeField().getText().trim();
            username = pv.getUsernameField().getText().trim();
            oldPwd = pv.getOldPasswordField().getText();
            newPwd = pv.getNewPasswordField().getText();
            confPwd = pv.getConfirmPasswordField().getText();
        }

        // Verifica se l’utente vuole aggiornare la password
        boolean vuoleCambiarePassword = !oldPwd.isEmpty() || !newPwd.isEmpty() || !confPwd.isEmpty();

        if (vuoleCambiarePassword) {
            if (!oldPwd.equals(cliente.getPassword())) {
                showAlert("❌ Password attuale errata");
                return;
            }

            if (!newPwd.equals(confPwd)) {
                showAlert("❌ Le nuove password non coincidono");
                return;
            }
        }

        // Costruzione nuovo oggetto cliente con eventuali modifiche
        Cliente nuovoCliente = new Cliente.Builder()
                .username(username.isEmpty() ? cliente.getUsername() : username)
                .nome(nome.isEmpty() ? cliente.getNome() : nome)
                .cognome(cognome.isEmpty() ? cliente.getCognome() : cognome)
                .password(vuoleCambiarePassword && !newPwd.isEmpty() ? newPwd : cliente.getPassword())
                .email(cliente.getEmail())
                .partitaIva(cliente.getPartitaIva())
                .indirizzo(cliente.getIndirizzo())
                .civico(cliente.getCivico())
                .cap(cliente.getCap())
                .citta(cliente.getCitta())
                .build();

        ClienteDAO dao = new ClienteDAOImpl(SessionController.getIsOnlineModeStatic());
        boolean successo = dao.update(nuovoCliente, cliente.getUsername());

        if (successo) {
            Cliente clienteRicaricato = dao.findByUsername(nuovoCliente.getUsername());
            if (clienteRicaricato != null) {
                SessionController.setClienteLoggato(clienteRicaricato);
                navigationService.setClienteLoggato(clienteRicaricato); // AGGIUNTA NECESSARIA
            }
            showAlert("✅ Profilo aggiornato con successo");
        } else {
            showAlert("❌ Errore durante il salvataggio");
        }
    }

    private void showAlert(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }

    public Parent getRootView() {
        if (view instanceof ProfiloView1) return ((ProfiloView1) view).getRoot();
        else return ((ProfiloView2) view).getRoot();
    }
}
