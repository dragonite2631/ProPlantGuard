package com.proptit.ProPlantGuard.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proptit.ProPlantGuard.model.Tree;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DashBoardController {
    @FXML
    public Button btnLogout;
    @FXML
    private BorderPane rootPane;

    @FXML
    private Button btnLibrary;

    @FXML
    private Button btnTips;


    private static final String FILE_NAME = "trees.json";



    @FXML
    public void initialize() {
        loadFXMLToCenter("/fxml/treelist.fxml");
        btnLibrary.setOnAction(event -> loadFXMLToCenter("/fxml/treelist.fxml"));
        btnTips.setOnAction(event -> loadFXMLToCenter("/fxml/treechat.fxml"));
        btnLogout.setOnAction(event -> {
            loadFXMLToNewWindow("/fxml/mainwindow.fxml");
            ((Stage) btnLogout.getScene().getWindow()).close();

        });
    }

    private void loadFXMLToCenter(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            if (loader.getLocation() == null) {
                throw new IllegalArgumentException("FXML file not found: " + fxmlPath);
            }
            rootPane.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load FXML: " + fxmlPath);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }



    private List<Tree> loadTreesFromJson() {
        File file = new File(FILE_NAME);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (file.exists() && file.length() > 0) {
                return objectMapper.readValue(file, new TypeReference<List<Tree>>() {});
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading trees from JSON file.");
        }
        return new ArrayList<>();
    }
    private void loadFXMLToNewWindow(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            if (loader.getLocation() == null) {
                throw new IllegalArgumentException("FXML file not found: " + fxmlPath);
            }
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("ProPlantGuard");
            stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/img/app_icon.png"))));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load FXML: " + fxmlPath);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }
}