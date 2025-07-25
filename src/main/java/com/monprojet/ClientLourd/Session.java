package com.monprojet.ClientLourd;

public class Session {
    private static int userId;
    private static String nom;
    private static String email;
    private static String token;

    public static void startSession(int id, String name, String mail, String authToken) {
        userId = id;
        nom = name;
        email = mail;
        token = authToken;
    }

    public static void clear() {
        userId = 0;
        nom = null;
        email = null;
        token = null;
    }

    // Getters
    public static int getUserId() { return userId; }
    public static String getNom() { return nom; }
    public static String getEmail() { return email; }
    public static String getToken() { return token; }
    public static boolean isLoggedIn() { return token != null; }
}