package com.monprojet.ClientLourd.db;

import java.io.*;
import java.net.*;
import org.json.JSONObject;

public class DBConnection {
    private static final String API_BASE_URL = "http://127.0.0.1:8000/api/"; // Assurez-vous que l'URL est exacte

    public static String authenticateUser(String email, String password) throws Exception {
        String endpoint = "login"; // Correspond à la route POST /api/login dans Laravel
        JSONObject jsonInput = new JSONObject();
        jsonInput.put("email", email);
        jsonInput.put("password", password);
        
        return sendRequest(endpoint, "POST", jsonInput.toString());
    }
        public static String registerUser(String nom, String prenom, String email, String password) throws Exception {
            String endpoint = "register"; // Correspond à POST /api/register dans Laravel
            JSONObject jsonInput = new JSONObject();
            jsonInput.put("nom", nom);
            jsonInput.put("prenom", prenom);
            jsonInput.put("email", email);
            jsonInput.put("password", password);
            
            return sendRequest(endpoint, "POST", jsonInput.toString());
        }
    
    private static String sendRequest(String endpoint, String method, String jsonInput) throws Exception {
        URL url = new URI(API_BASE_URL + endpoint).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInput.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Gestion des erreurs HTTP
        if (conn.getResponseCode() >= 400) {
            try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getErrorStream(), "utf-8"))) {
                StringBuilder errorResponse = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    errorResponse.append(responseLine.trim());
                }
                throw new RuntimeException("Erreur API: " + errorResponse.toString());
            }
        }

        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(conn.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }
}