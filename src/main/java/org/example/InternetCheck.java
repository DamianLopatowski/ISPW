package org.example;

import java.net.HttpURLConnection;
import java.net.URL;

public class InternetCheck {

    // Costruttore privato per impedire l'istanza della classe
    private InternetCheck() {
        // Non è necessario implementare nulla qui
    }

    public static boolean isConnected() {
        try {
            URL url = new URL("http://www.google.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(3000);
            connection.connect();
            return connection.getResponseCode() == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            return false;
        }
    }
}