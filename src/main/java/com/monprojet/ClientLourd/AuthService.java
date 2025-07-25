package com.monprojet.ClientLourd;

import org.json.JSONObject;
import com.monprojet.ClientLourd.db.DBConnection;

public class AuthService {
    public boolean authenticate(String email, String password) {
        try {
            String response = DBConnection.authenticateUser(email, password);
            JSONObject jsonResponse = new JSONObject(response);
            
            if (jsonResponse.getBoolean("success")) {
                JSONObject user = jsonResponse.getJSONObject("user");
                Session.startSession(
                    user.getInt("id"),
                    user.getString("nom"),
                    user.getString("email"),
                    jsonResponse.getString("token")
                );
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registerUser(String nom, String prenom, String email, String password) {
        try {
            String response = DBConnection.registerUser(nom, prenom, email, password);
            JSONObject jsonResponse = new JSONObject(response);
            return jsonResponse.getBoolean("success");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}