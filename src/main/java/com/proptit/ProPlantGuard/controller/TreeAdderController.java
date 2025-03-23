package com.proptit.ProPlantGuard.controller;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.proptit.ProPlantGuard.model.Tree;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TreeAdderController {

    private static final String FILE_NAME = "trees.json";

    @FXML
    private TextField nameField;

    @FXML
    private TextField speciesField;


    @FXML
    private TextArea descriptionArea;

    @FXML
    private ImageView imageView;

    @FXML
    private Button selectImageButton;

    @FXML
    private Button submitButton;


    @FXML
    private DatePicker lastWateredDatePicker;

    private Stage currentStage;
    private File selectedImageFile;

    private Tree treeToEdit;
    private Consumer<Tree> onTreeEditedCallback;

    @FXML
    private void initialize() {
        selectImageButton.setOnAction(event -> handleSelectImage());
        submitButton.setOnAction(event -> handleSubmit());
        lastWateredDatePicker.setValue(LocalDate.now());
    }

    private void handleSelectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Plant Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        Stage stage = (Stage) selectImageButton.getScene().getWindow();
        selectedImageFile = fileChooser.showOpenDialog(stage);
        
        if (selectedImageFile != null) {
            try {
                Image image = new Image(selectedImageFile.toURI().toString());
                imageView.setImage(image);
                imageView.setPreserveRatio(true);
                imageView.setFitHeight(100);
                imageView.setFitWidth(100);
            } catch (Exception e) {
                showAlert("Error", "Failed to load the selected image.");
                e.printStackTrace();
            }
        }
    }

    private void handleSubmit() {
        String name = nameField.getText().trim();
        String species = speciesField.getText().trim();
        String description = descriptionArea.getText().trim();
        LocalDate lastWateredDate = lastWateredDatePicker.getValue();
        if (name.isEmpty() || species.isEmpty() || description.isEmpty() || lastWateredDate == null) {
            showAlert("Validation Error", "All fields are required.");
            return;
        }

        List<Tree> existingTrees = loadExistingTrees();
        if (treeToEdit != null) {
            treeToEdit.setName(name);
            treeToEdit.setSpecies(species);
            treeToEdit.setDescription(description);
            treeToEdit.setLastWateredDate(lastWateredDate.toString());
            treeToEdit.setImageUrl(selectedImageFile != null ? selectedImageFile.getAbsolutePath() : treeToEdit.getImageUrl());
            int index = existingTrees.indexOf(treeToEdit);
            if (index != -1) {
                existingTrees.set(index, treeToEdit);
            } else {
                showAlert("Error", "Failed to find the tree in the list.");
                return;
            }
        } else {
            if (existingTrees.stream().anyMatch(t -> t.getName().equalsIgnoreCase(name))) {
                showAlert("Error", "A tree with the same name already exists.");
                return;
            }

            String folderPath = "images/" + name.replaceAll("\\s+", "_").toLowerCase();
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            Tree tree = new Tree(
                    name,
                    species,
                    description,
                    selectedImageFile != null ? selectedImageFile.getAbsolutePath() : null,
                    lastWateredDate.toString(),
                    "weekly",
                    "false",
                    folderPath
            );
            existingTrees.add(tree);
        }

        saveTreesToJson(existingTrees);
        showAlert("Success", treeToEdit != null ? "Plant details successfully updated!" : "Plant details successfully added!");
        closeStage();
    }


    private void closeStage() {
        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }


    private void saveTreeToJson(Tree tree) {
        List<Tree> treeList = loadExistingTrees();
        int index = treeList.indexOf(tree);
        if (index != -1) {
            treeList.set(index, tree);
        } else {
            treeList.add(tree);
        }
        saveTreesToJson(treeList);
    }
    private List<Tree> loadExistingTrees() {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(FILE_NAME);

        if (file.exists()) {
            try {
                CollectionType listType = objectMapper.getTypeFactory()
                        .constructCollectionType(ArrayList.class, Tree.class);
                return objectMapper.readValue(file, listType);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error while loading trees from JSON file.");
            }
        }

        return new ArrayList<>();
    }

    private void saveTreesToJson(List<Tree> treeList) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT);
        try {
            File file = new File(FILE_NAME);
            objectMapper.writeValue(file, treeList);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while saving trees to JSON file.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void setTreeForEditing(Tree tree) {
        this.treeToEdit = tree;
        nameField.setText(tree.getName());
        speciesField.setText(tree.getSpecies());
        descriptionArea.setText(tree.getDescription());
        lastWateredDatePicker.setValue(LocalDate.parse(tree.getLastWateredDate()));
        if (tree.getImageUrl() != null && !tree.getImageUrl().isEmpty()) {
            selectedImageFile = new File(tree.getImageUrl());
            Image image = new Image(selectedImageFile.toURI().toString());
            imageView.setImage(image);
        }
        submitButton.setText("Update");
    }

    public void setOnTreeEditedCallback(Consumer<Tree> callback) {
        this.onTreeEditedCallback = callback;
    }
}