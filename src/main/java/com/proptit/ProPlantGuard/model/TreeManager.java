package com.proptit.ProPlantGuard.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TreeManager {
    private static final String FILE_NAME = "tree.json";
    private static ObservableList<Tree> loadTreesFromJson() {
        File file = new File(FILE_NAME);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<Tree> trees = objectMapper.readValue(file, new TypeReference<List<Tree>>() {});
            return FXCollections.observableArrayList(trees);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while loading trees from JSON file.");
            return FXCollections.observableArrayList();
        }
    }
    public static void saveTreesToJson(List<Tree> trees) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT);
        try {
            File file = new File(FILE_NAME);
            objectMapper.writeValue(file, trees);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error while saving trees to JSON file.");
        }
    }
    public static void showTreeViewer(BorderPane rootPane) {
        TableView<Tree> table = new TableView<>();
        TableColumn<Tree, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Tree, String> speciesColumn = new TableColumn<>("Species");
        speciesColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        TableColumn<Tree, String> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        TableColumn<Tree, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        TableColumn<Tree, String> imageColumn = new TableColumn<>("Image");
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));
        imageColumn.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();
            {
                imageView.setFitWidth(200);
                imageView.setFitHeight(200);
                imageView.setPreserveRatio(true);
            }
            @Override
            protected void updateItem(String imageUrl, boolean empty) {
                super.updateItem(imageUrl, empty);
                if (empty || imageUrl == null || imageUrl.isEmpty()) {
                    setGraphic(null);
                } else {
                    try {
                        Image image = new Image("file:" + imageUrl);
                        imageView.setImage(image);
                        setGraphic(imageView);
                    } catch (Exception e) {
                        setGraphic(null);
                    }
                }
            }
        });
        table.getColumns().addAll(nameColumn, speciesColumn, ageColumn, descriptionColumn, imageColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ObservableList<Tree> treeList = loadTreesFromJson();
        table.setItems(treeList);
        Button removeButton = new Button("Xóa cây");
        removeButton.setOnAction(event -> {
            Tree selectedTree = table.getSelectionModel().getSelectedItem();
            if (selectedTree != null) {
                treeList.remove(selectedTree);
                saveTreesToJson(treeList.stream().collect(Collectors.toList()));
                table.setItems(treeList);
            }
        });
        rootPane.setBottom(removeButton);
        rootPane.setCenter(table);

    }
}