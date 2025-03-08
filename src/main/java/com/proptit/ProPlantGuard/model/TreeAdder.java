package com.proptit.ProPlantGuard.model;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
    private static void clearFields(TextField nameField, TextField speciesField, TextField ageField, TextArea descriptionArea) {
        nameField.clear();
        speciesField.clear();
        ageField.clear();
        descriptionArea.clear();
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
        GridPane addPane = new GridPane();
        addPane.setStyle("-fx-background-color: #e6ffe6;");
        addPane.setHgap(10);
        addPane.setVgap(10);
        Label nameLabel = new Label("Tree Name:");
        TextField nameField = new TextField();
        Label speciesLabel = new Label("Species:");
        TextField speciesField = new TextField();
        Label ageLabel = new Label("Age:");
        TextField ageField = new TextField();
        Label descriptionLabel = new Label("Description:");
        TextArea descriptionArea = new TextArea();
        Label imageLabel = new Label("Tree Image:");
        Button selectImageButton = new Button("Select Image");
        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);
        final String[] selectedImagePath = new String[1];
        selectImageButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Tree Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
            );
            File selectedFile = fileChooser.showOpenDialog(rootPane.getScene().getWindow());
            if (selectedFile != null) {
                selectedImagePath[0] = selectedFile.getAbsolutePath();
                Image image = new Image(selectedFile.toURI().toString());
                imageView.setImage(image);
            }
        });
        Button submitButton = new Button("Add Tree");
        Button cancelButton = new Button("Cancel");
        addPane.add(nameLabel, 0, 0);
        addPane.add(nameField, 1, 0);
        addPane.add(speciesLabel, 0, 1);
        addPane.add(speciesField, 1, 1);
        addPane.add(ageLabel, 0, 2);
        addPane.add(ageField, 1, 2);
        addPane.add(descriptionLabel, 0, 3);
        addPane.add(descriptionArea, 1, 3);
        addPane.add(imageLabel, 0, 4);
        addPane.add(selectImageButton, 1, 4);
        addPane.add(imageView, 1, 5);
        VBox buttonBox = new VBox(10, submitButton, cancelButton);
        addPane.add(buttonBox, 1, 6);
        submitButton.setOnAction(e -> {
            String name = nameField.getText();
            String species = speciesField.getText();
            String age = ageField.getText();
            String description = descriptionArea.getText();
            if (name.isEmpty() || species.isEmpty() || age.isEmpty() || description.isEmpty()) {
                showAlert("Error", "Please fill in all the fields!");
            } else if (selectedImagePath[0] == null || selectedImagePath[0].isEmpty()) {
                showAlert("Error", "Please select an image for the tree!");
            } else {
                saveTree(new Tree(name, species, age, description, selectedImagePath[0]));
                showAlert("Success", "Tree added successfully!");
            }
        });
        rootPane.setCenter(addPane);
        }

}