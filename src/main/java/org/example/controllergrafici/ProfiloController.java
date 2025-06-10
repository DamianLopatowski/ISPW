package org.example.controllergrafici;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.example.bean.ClienteBean;
import org.example.dao.ClienteDAO;
import org.example.dao.ClienteDAOImpl;
import org.example.model.Cliente;
import org.example.service.ClienteMapper;
import org.example.service.NavigationService;
import org.example.controllerapplicativo.SessionController;
import org.example.view.ProfiloView1;
import org.example.view.ProfiloView2;

public class ProfiloController {
    private final Object view;
    private final NavigationService navigationService;

    public ProfiloController(boolean isInterfaccia1, NavigationService navigationService) {
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
        ClienteBean cliente = ClienteMapper.toBean(navigationService.getClienteLoggato());

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
        ClienteBean cliente = ClienteMapper.toBean(navigationService.getClienteLoggato());
        ProfiloInput input = estraiDatiProfilo(v);

        boolean vuoleCambiarePassword = input.vuoleCambiarePassword();

        if (vuoleCambiarePassword) {
            if (!input.oldPwd.equals(cliente.getPassword())) {
                showAlert("Password attuale errata");
                return;
            }

            if (!input.newPwd.equals(input.confPwd)) {
                showAlert("Le nuove password non coincidono");
                return;
            }
        }

        ClienteBean nuovoClienteBean = new ClienteBean();
        nuovoClienteBean.setUsername(input.username.isEmpty() ? cliente.getUsername() : input.username);
        nuovoClienteBean.setNome(input.nome.isEmpty() ? cliente.getNome() : input.nome);
        nuovoClienteBean.setCognome(input.cognome.isEmpty() ? cliente.getCognome() : input.cognome);
        nuovoClienteBean.setPassword(vuoleCambiarePassword && !input.newPwd.isEmpty() ? input.newPwd : cliente.getPassword());
        nuovoClienteBean.setEmail(cliente.getEmail());
        nuovoClienteBean.setPartitaIva(cliente.getPartitaIva());
        nuovoClienteBean.setIndirizzo(cliente.getIndirizzo());
        nuovoClienteBean.setCivico(cliente.getCivico());
        nuovoClienteBean.setCap(cliente.getCap());
        nuovoClienteBean.setCitta(cliente.getCitta());

        ClienteDAO dao = new ClienteDAOImpl(SessionController.getIsOnlineModeStatic());
        boolean successo = dao.update(nuovoClienteBean.toCliente(), cliente.getUsername());

        if (successo) {
            Cliente clienteRicaricato = dao.findByUsername(nuovoClienteBean.getUsername());
            if (clienteRicaricato != null) {
                ClienteBean clienteBeanRicaricato = ClienteMapper.toBean(clienteRicaricato);
                SessionController.setClienteLoggato(clienteBeanRicaricato.toCliente());
                navigationService.setClienteLoggato(clienteBeanRicaricato.toCliente());
            }
            showAlert("Profilo aggiornato con successo");
        } else {
            showAlert("Errore durante il salvataggio");
        }
    }


    private ProfiloInput estraiDatiProfilo(Object v) {
        ProfiloInput input = new ProfiloInput();
        if (v instanceof ProfiloView1) {
            ProfiloView1 pv = (ProfiloView1) v;
            input.nome = pv.getNomeField().getText().trim();
            input.cognome = pv.getCognomeField().getText().trim();
            input.username = pv.getUsernameField().getText().trim();
            input.oldPwd = pv.getOldPasswordField().getText();
            input.newPwd = pv.getNewPasswordField().getText();
            input.confPwd = pv.getConfirmPasswordField().getText();
        } else if (v instanceof ProfiloView2) {
            ProfiloView2 pv = (ProfiloView2) v;
            input.nome = pv.getNomeField().getText().trim();
            input.cognome = pv.getCognomeField().getText().trim();
            input.username = pv.getUsernameField().getText().trim();
            input.oldPwd = pv.getOldPasswordField().getText();
            input.newPwd = pv.getNewPasswordField().getText();
            input.confPwd = pv.getConfirmPasswordField().getText();
        }
        return input;
    }

    private static class ProfiloInput {
        String nome;
        String cognome;
        String username;
        String oldPwd;
        String newPwd;
        String confPwd;

        boolean vuoleCambiarePassword() {
            return !oldPwd.isEmpty() || !newPwd.isEmpty() || !confPwd.isEmpty();
        }
    }

    private void showAlert(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }

    public Parent getRootView() {
        if (view instanceof ProfiloView1) {
            return ((ProfiloView1) view).getRoot();
        } else {
            return ((ProfiloView2) view).getRoot();
        }
    }
}