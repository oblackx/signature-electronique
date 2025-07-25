package com.monprojet.ClientLourd.db;

import java.io.*;
import java.net.*;
import org.json.JSONObject;

public class DBConnection {
	private static final String API_BASE_URL = "http://127.0.0.1:8000/api/";

    public static String authenticateUser(String email, String password) throws Exception {
        String endpoint = "auth/login";
        String jsonInput = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", 
            URLEncoder.encode(email, "UTF-8"), 
            URLEncoder.encode(password, "UTF-8"));
        return sendRequest(endpoint, "POST", jsonInput);
    }

    public static String registerUser(String nom, String prenom, String email, String password) throws Exception {
        String endpoint = "auth/register";
        String jsonInput = String.format(
            "{\"nom\":\"%s\",\"prenom\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}",
            URLEncoder.encode(nom, "UTF-8"),
            URLEncoder.encode(prenom, "UTF-8"),
            URLEncoder.encode(email, "UTF-8"),
            URLEncoder.encode(password, "UTF-8")
        );
        return sendRequest(endpoint, "POST", jsonInput);
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