package org.example;

import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PageNavigator {
    private final Stage primaryStage;
    private final Map<String, Supplier<NavigablePage>> pageRegistry = new HashMap<>();

    public PageNavigator(Stage primaryStage) {
        this.primaryStage = primaryStage;
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
