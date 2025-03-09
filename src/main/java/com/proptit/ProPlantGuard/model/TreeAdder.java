package com.proptit.ProPlantGuard.model;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class TreeAdder {
    private static void saveTree(Tree tree) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            List<Tree> trees;
            File file = new File("tree.json");
            if (file.exists() && file.length() > 0) {
                trees = objectMapper.readValue(file, new TypeReference<List<Tree>>() {});
            } else {
                trees = new ArrayList<>();
            }
            trees.add(tree);
            objectMapper.writeValue(file, trees);
            System.out.println("Tree object saved to tree.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void showAlert(String title, String message) {
        Stage alertStage = new Stage();
        alertStage.setTitle(title);
        VBox alertBox = new VBox(10);
        alertBox.getChildren().add(new Text(message));
        alertBox.setStyle("-fx-padding: 10; -fx-background-color: white;");
        Scene alertScene = new Scene(alertBox, 300, 100);
        alertStage.setScene(alertScene);
        alertStage.show();
    }
    public static void showAddContent(BorderPane rootPane) {
        try {
            FXMLLoader loader = new FXMLLoader(TreeAdder.class.getResource("/fxml/treeaddform.fxml"));
            Pane addPane = loader.load();
            TextField nameField = (TextField) addPane.lookup("#nameField");
            TextField speciesField = (TextField) addPane.lookup("#speciesField");
            TextField ageField = (TextField) addPane.lookup("#ageField");
            TextArea descriptionArea = (TextArea) addPane.lookup("#descriptionArea");
            Button submitButton = (Button) addPane.lookup("#submitButton");
            Button selectImageButton = (Button) addPane.lookup("#selectImageButton");
            ImageView imageView = (ImageView) addPane.lookup("#imageView");
            selectImageButton.setOnAction(event -> handleSelectImage(imageView));
            submitButton.setOnAction(event -> {
                String name = nameField.getText();
                String species = speciesField.getText();
                String ageText = ageField.getText();
                String description = descriptionArea.getText();
                if (name.isEmpty() || species.isEmpty() || ageText.isEmpty() || description.isEmpty()) {
                    showAlert("Validation Error", "Cần điền đầy đủ thông tin");
                    return;
                }
                File selectedImage = null;
                if (imageView.getImage() != null) {
                    selectedImage = new File(imageView.getImage().getUrl().replace("file:/", ""));
                }else{
                    imageView.setImage(null);
                }
                Tree tree = new Tree(name, species, ageText, description,
                        selectedImage != null ? selectedImage.getAbsolutePath() : null);
                saveTree(tree);
                showAlert("Success", "Tree details successfully added!");
            });
            rootPane.setCenter(addPane);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

    }
    private static void handleSelectImage(ImageView imageView) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter(
                "Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp"
        );
        fileChooser.getExtensionFilters().add(imageFilter);
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);
        }
    }
}