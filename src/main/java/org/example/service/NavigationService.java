package org.example.service;

import javafx.scene.Parent;

public interface NavigationService {
    void navigateToMainView();
    Parent navigateToGestioneView(boolean isOfflineMode, boolean isInterfaccia1);
    void navigateToLogin(boolean isInterfaccia1); // ✅ Aggiunto metodo per navigare al login
    Parent createMainView(); // AGGIUNTO per creare la schermata principale
}
