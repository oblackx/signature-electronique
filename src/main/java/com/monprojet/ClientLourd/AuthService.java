package com.monprojet.ClientLourd;

import com.monprojet.ClientLourd.db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    public boolean authenticate(String email, String password) {
        // À compléter avec ta logique d'authentification (requête SQL + vérification BCrypt)
        return false;
    }

    public boolean registerUser(String nom, String prenom, String email, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();

            String checkQuery = "SELECT COUNT(*) FROM users WHERE email = ?";
            stmt = conn.prepareStatement(checkQuery);
            stmt.setString(1, email);
            rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false; // email déjà utilisé
            }
            stmt.close();

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            String insertQuery = "INSERT INTO users (nom, prenom, email, password) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(insertQuery);
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setString(3, email);
            stmt.setString(4, hashedPassword);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
