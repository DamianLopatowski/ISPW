package org.example;

public class SessionContext {
    private static boolean isOffline;

    public static boolean isOffline() {
        return isOffline;
    }

    public static void setOffline(boolean offline) {
        isOffline = offline;
    }
}
