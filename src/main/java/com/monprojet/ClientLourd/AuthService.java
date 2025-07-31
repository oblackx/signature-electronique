package com.monprojet.ClientLourd;

import org.json.JSONObject;
import com.monprojet.ClientLourd.db.DBConnection;

public class AuthService {
	public boolean authenticate(String email, String password) {
	    try {
	        System.out.println("🚀 Appel API login...");
	        String response = DBConnection.authenticateUser(email, password);
	        System.out.println("✅ Réponse JSON: " + response);

	        JSONObject jsonResponse = new JSONObject(response);

	        if (jsonResponse.has("access_token") && jsonResponse.has("user")) {
	            JSONObject user = jsonResponse.getJSONObject("user");
	            Session.startSession(
	                user.getInt("id"),
	                user.getString("nom"),
	                user.getString("email"),
	                jsonResponse.getString("access_token") // using access token 
	            );
	            return true;
	        }else {
	            System.out.println("❌ Champs manquants (access_token/user)");
	            return false;
	        }
	    } catch (Exception e) {
	        System.out.println("💥 Exception dans authenticate()");
	        e.printStackTrace();
	        return false;
	    }
	}



    public boolean registerUser(String nom, String prenom, String email, String password) {
        try {
            String response = DBConnection.registerUser(nom, prenom, email, password);
            JSONObject jsonResponse = new JSONObject(response);
            
            if (jsonResponse.has("token") && jsonResponse.has("user")) {
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
}
