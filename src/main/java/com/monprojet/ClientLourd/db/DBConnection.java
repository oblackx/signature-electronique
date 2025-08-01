package com.monprojet.ClientLourd.db;

import java.io.*;
import java.net.*;
import org.json.JSONObject;

public class DBConnection { 
	private static final String API_BASE_URL ="http://10.33.212.73:8000/api/";
	public static String authenticateUser(String email, String password) throws Exception {
	    String endpoint = "login";
	    JSONObject jsonInput = new JSONObject();
	    jsonInput.put("email", email);
	    jsonInput.put("password", password);

	    // 🔍 debug
	    System.out.println("📤 Email: " + email);
	    System.out.println("📤 Password: " + password);
	    System.out.println("📤 JSON: " + jsonInput.toString());

	    return sendRequest(endpoint, "POST", jsonInput.toString());
	}

        private static String sendRequest(String endpoint, String method, String jsonInput) throws Exception {
            URL url = new URI(API_BASE_URL + endpoint).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            System.out.println("➡️ Sending to URL: " + url.toString());
            System.out.println("➡️ Method: " + method);
            System.out.println("➡️ Body: " + jsonInput);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInput.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            System.out.println("⬅️ Response code: " + responseCode);

            if (responseCode >= 400) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "utf-8"))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        errorResponse.append(line.trim());
                    }
                    System.out.println("❌ API Error Response: " + errorResponse);
                    throw new RuntimeException("Erreur API: " + errorResponse.toString());
                }
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
                System.out.println("✅ API Success Response: " + response.toString());
                return response.toString();
            }
        }
        }
