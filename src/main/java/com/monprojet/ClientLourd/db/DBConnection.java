package com.monprojet.ClientLourd.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/uiz_esign"; // nom base
    private static final String USER = "root";  // utilisateur MySQL (souvent root)
    private static final String PASSWORD = "";  // mot de passe MySQL (vide si XAMPP)

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            System.out.println("Connexion OK !");
        } catch (SQLException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
        }
    }

}
