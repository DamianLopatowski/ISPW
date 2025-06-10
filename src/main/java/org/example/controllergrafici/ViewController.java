package org.example.controllergrafici;

import org.example.service.NavigationService;
import org.example.view.View;
import java.util.logging.Logger;

public class ViewController {
    private final View view;
    private final NavigationService navigationService;
    private static final Logger LOGGER = Logger.getLogger(ViewController.class.getName());

    public ViewController(View view, NavigationService navigationService) {
        this.view = view;
        this.navigationService = navigationService;
        initialize();
    }

    private void initialize() {
        view.getLoginButton().setOnAction(event -> navigateToLogin(false));
        view.getLoginClienteButton().setOnAction(event -> navigateToLogin(true));
        view.getRegistratiClienteButton().setOnAction(event -> navigateToRegistrazioneCliente());
    }

    private void navigateToLogin(boolean isCliente) {
        if (navigationService == null) {
            LOGGER.warning("ERRORE: NavigationService è NULL! Assicurati di impostarlo prima di usare la navigazione.");
            return;
        }

        boolean isInterfaccia1 = view.getInterfaccia1Option().isSelected();
        navigationService.setInterfaccia1(isInterfaccia1);
        navigationService.navigateToLogin(isInterfaccia1, isCliente);
    }


    private void navigateToRegistrazioneCliente() {
        if (navigationService == null) {
            LOGGER.warning("ERRORE: NavigationService è NULL! Assicurati di impostarlo prima di usare la navigazione.");
            return;
        }

        boolean isInterfaccia1 = view.getInterfaccia1Option().isSelected();
        navigationService.setInterfaccia1(isInterfaccia1);
        navigationService.navigateToRegistrazioneCliente(isInterfaccia1);
    }
}
