package org.example;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class NetworkUtils {

    // Costruttore privato per impedire l'istanza
    private NetworkUtils() {
        throw new UnsupportedOperationException("Questa è una classe utility e non può essere istanziata.");
    }

    public static boolean isInternetAvailable() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("8.8.8.8", 53), 1500); // Google DNS
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
