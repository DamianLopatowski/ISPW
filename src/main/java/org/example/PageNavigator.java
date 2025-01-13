package org.example;

import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PageNavigator {
    private static PageNavigator instance; // Istanza singleton
    private final Stage primaryStage;
    private final Map<String, Supplier<NavigablePage>> pageRegistry = new HashMap<>();

    private PageNavigator(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Metodo per ottenere l'istanza singleton
    public static PageNavigator getInstance(Stage primaryStage) {
        if (instance == null) {
            instance = new PageNavigator(primaryStage);
        }
        return instance;
    }

    public void registerPage(String pageName, Supplier<NavigablePage> pageSupplier) {
        pageRegistry.put(pageName, pageSupplier);
    }

    public void navigateToPage(String pageName) {
        if (!pageRegistry.containsKey(pageName)) {
            System.err.println("Pagine registrate: " + pageRegistry.keySet());
            throw new IllegalArgumentException("Pagina non trovata: " + pageName);
        }
        Supplier<NavigablePage> pageSupplier = pageRegistry.get(pageName);
        NavigablePage page = pageSupplier.get();
        page.start(primaryStage);
    }
}
