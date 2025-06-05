package org.example.controllergrafici;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.example.model.Cliente;
import org.example.service.NavigationService;
import org.example.controllerapplicativo.SessionController;
import org.example.view.ProfiloView1;
import org.example.view.ProfiloView2;

public class ProfiloController {
    private final Object view;
    private final NavigationService navigationService;
    private final Cliente cliente;

    public ProfiloController(boolean isOnlineMode, boolean isInterfaccia1, NavigationService navigationService) {
        this.view = isInterfaccia1 ? new ProfiloView1() : new ProfiloView2();
        this.navigationService = navigationService;
        this.cliente = SessionController.getClienteLoggato();

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

        if (!oldPwd.equals(cliente.getPassword())) {
            showAlert("❌ Password attuale errata");
            return;
        }

        if (!newPwd.equals(confPwd)) {
            showAlert("❌ Le nuove password non coincidono");
            return;
        }

        // Ricostruisce l'oggetto Cliente con Builder
        Cliente nuovoCliente = new Cliente.Builder()
                .username(username)
                .nome(nome)
                .cognome(cognome)
                .password(newPwd.isEmpty() ? cliente.getPassword() : newPwd)
                .email(cliente.getEmail())
                .partitaIva(cliente.getPartitaIva())
                .indirizzo(cliente.getIndirizzo())
                .civico(cliente.getCivico())
                .cap(cliente.getCap())
                .citta(cliente.getCitta())
                .build();

        SessionController.setClienteLoggato(nuovoCliente);

        // (Opzionale) salva anche nel DB se online
        showAlert("✅ Profilo aggiornato con successo");
    }

    private void showAlert(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }

    public Parent getRootView() {
        if (view instanceof ProfiloView1) return ((ProfiloView1) view).getRoot();
        else return ((ProfiloView2) view).getRoot();
    }
}
