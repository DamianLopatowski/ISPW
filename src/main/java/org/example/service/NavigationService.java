package org.example.service;

import javafx.scene.Parent;

public interface NavigationService {
    void navigateToNegozio();
    void navigateToMainView();
    Parent navigateToGestioneView(boolean isOfflineMode, boolean isInterfaccia1);
    void navigateToLogin(boolean isInterfaccia1, boolean isCliente);
    Parent navigateToRegistrazioneCliente(boolean isInterfaccia1);
    void setInterfaccia1(boolean isInterfaccia1);
    void navigateToProfilo();



}
