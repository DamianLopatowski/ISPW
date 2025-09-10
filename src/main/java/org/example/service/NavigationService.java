package org.example.service;
import javafx.scene.Parent;
import org.example.model.Cliente;

public interface NavigationService {
    void navigateToNegozio();
    void navigateToMainView();
    Parent navigateToGestioneView(boolean isOfflineMode, boolean isInterfaccia1);
    void navigateToLogin(boolean isInterfaccia1, boolean isCliente);
    Parent navigateToRegistrazioneCliente(boolean isInterfaccia1);
    void setInterfaccia1(boolean isInterfaccia1);
    void navigateToProfilo();
    void navigateToProfilo(boolean isInterfaccia1);
    void setClienteLoggato(Cliente cliente);
    Cliente getClienteLoggato();
    Parent navigateToGestioneProdottiView();
    Parent navigateToGestioneSpedizioniView();
    Parent navigateToGestioneSogliaView();

}
