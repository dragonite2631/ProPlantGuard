package com.proptit.ProPlantGuard.model;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class AccountManager {
    public static boolean validateCredentials(String username, String password, String filePath) {
        if (username == null || password == null || username.isBlank() || password.isBlank()) {
            return false;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String fileUsername = parts[0].trim();
                    String filePassword = parts[1].trim();
                    if (fileUsername.equals(username) && filePassword.equals(password)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading user data: " + e.getMessage());
        }
        return false;

    }

}
