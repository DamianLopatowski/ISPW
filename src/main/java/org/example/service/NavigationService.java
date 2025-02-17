package org.example.service;

import javafx.scene.Parent;

public interface NavigationService {
    void navigateToMainView();
    Parent navigateToGestioneView(boolean isOfflineMode, boolean isInterfaccia1);
    void navigateToLogin(boolean isInterfaccia1, boolean isCliente);
    void navigateToRegistrazioneCliente(boolean isInterfaccia1);
    Parent createMainView(); // AGGIUNTO per creare la schermata principale
}
