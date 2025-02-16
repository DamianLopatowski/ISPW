package org.example.service;

import javafx.scene.Parent;

public interface NavigationService {
    void navigateToMainView();
    Parent navigateToGestioneView(boolean isOfflineMode); // Modificato per restituire la vista corretta
}
