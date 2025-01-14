package org.example;

import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class NavigationHandler {
    private static NavigationHandler instance;
    private final Map<String, NavigablePage> pages = new HashMap<>();

    private NavigationHandler() {}

    public static NavigationHandler getInstance() {
        if (instance == null) {
            instance = new NavigationHandler();
        }
        return instance;
    }

    public void registerPage(String pageName, NavigablePage page) {
        if (page == null) {
            throw new IllegalArgumentException("La pagina non può essere null: " + pageName);
        }
        pages.put(pageName, page);
        System.out.println("Pagina registrata: " + pageName);
    }

    public void navigateTo(String pageName, Stage stage) {
        NavigablePage page = pages.get(pageName);
        if (page != null) {
            System.out.println("Navigazione verso: " + pageName);
            page.start(stage);
        } else {
            System.err.println("Pagina non registrata: " + pageName);
            throw new IllegalStateException("Pagina non registrata: " + pageName);
        }
    }
}
