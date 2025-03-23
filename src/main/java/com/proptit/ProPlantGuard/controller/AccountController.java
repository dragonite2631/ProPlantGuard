package com.proptit.ProPlantGuard.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

import static com.proptit.ProPlantGuard.model.AccountManager.validateCredentials;

public class AccountController {
    private static final String USER_DATA_FILE = "userdata.txt";

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    public void initialize(){
        loginButton.setOnMouseEntered(e -> loginButton.setStyle(
                "-fx-background-color: #388E3C; -fx-text-fill: white; -fx-background-radius: 8px; -fx-font-size: 14px; -fx-padding: 8px 18px;"
        ));
        loginButton.setOnMouseExited(e -> loginButton.setStyle(
                "-fx-background-color: #43A047; -fx-text-fill: white; -fx-background-radius: 8px; -fx-font-size: 14px; -fx-padding: 8px 18px;"
        ));

        registerButton.setOnMouseEntered(e -> registerButton.setStyle(
                "-fx-background-color: #57A05A; -fx-text-fill: white; -fx-background-radius: 8px; -fx-font-size: 14px; -fx-padding: 8px 18px;"
        ));
        registerButton.setOnMouseExited(e -> registerButton.setStyle(
                "-fx-background-color: #66BB6A; -fx-text-fill: white; -fx-background-radius: 8px; -fx-font-size: 14px; -fx-padding: 8px 18px;"
        ));

        usernameField.setOnMouseEntered(e -> usernameField.setStyle(
                "-fx-background-color: white; -fx-border-color: #66BB6A; -fx-border-radius: 10px; -fx-padding: 8px; -fx-font-size: 14px;"
        ));
        usernameField.setOnMouseExited(e -> usernameField.setStyle(
                "-fx-background-color: #f5f5f5; -fx-border-color: transparent; -fx-border-radius: 10px; -fx-padding: 8px; -fx-font-size: 14px;"
        ));

        usernameField.setOnMouseClicked(e -> usernameField.setStyle(
                "-fx-background-color: white; -fx-border-color: #43A047; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-padding: 8px; -fx-font-size: 14px;"
        ));
        usernameField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                usernameField.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: transparent; -fx-border-radius: 10px; -fx-padding: 8px; -fx-font-size: 14px;");
            }
        });

        passwordField.setOnMouseEntered(e -> passwordField.setStyle(
                "-fx-background-color: white; -fx-border-color: #66BB6A; -fx-border-radius: 10px; -fx-padding: 8px; -fx-font-size: 14px;"
        ));
        passwordField.setOnMouseExited(e -> passwordField.setStyle(
                "-fx-background-color: #f5f5f5; -fx-border-color: transparent; -fx-border-radius: 10px; -fx-padding: 8px; -fx-font-size: 14px;"
        ));

        passwordField.setOnMouseClicked(e -> passwordField.setStyle(
                "-fx-background-color: white; -fx-border-color: #43A047; -fx-border-width: 2px; -fx-border-radius: 10px; -fx-padding: 8px; -fx-font-size: 14px;"
        ));
        passwordField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                passwordField.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: transparent; -fx-border-radius: 10px; -fx-padding: 8px; -fx-font-size: 14px;");
            }
        });
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void onRegisterButtonClick(ActionEvent actionEvent) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            showAlert("Error", "Username and password must not be empty.");
            return;
        }
        try {
            java.nio.file.Path path = java.nio.file.Paths.get(USER_DATA_FILE);
            if (!java.nio.file.Files.exists(path)) {
                java.nio.file.Files.createFile(path);
            }
            java.util.List<String> lines = java.nio.file.Files.readAllLines(path);
            for (String line : lines) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String existingUsername = parts[0].trim();
                    if (existingUsername.equals(username)) {
                        showAlert("Error", "Username already exists. Please choose a different username.");
                        return;
                    }
                }
            }
            java.nio.file.Files.write(path, (username + ":" + password + System.lineSeparator()).getBytes(), java.nio.file.StandardOpenOption.APPEND, java.nio.file.StandardOpenOption.CREATE);
            showAlert("Success", "Account registered successfully!");
        }catch(IOException e){
            showAlert("Error", "Could not register account. Please try again.");
        }
    }
    @FXML
    public void onLoginButtonClick(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            showAlert("Error", "Username and password must not be empty.");
            return;
        }
        if (validateCredentials(username, password, USER_DATA_FILE)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/dashboard.fxml"));
                Parent dashboardRoot = loader.load();
                Stage stage = (Stage) usernameField.getScene().getWindow();

                Scene dashboardScene = new Scene(dashboardRoot);
                stage.setScene(dashboardScene);
                stage.setTitle("Dashboard");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        showAlert("Login Failed", "Invalid username or password.");
    }
}