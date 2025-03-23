package com.proptit.ProPlantGuard.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proptit.ProPlantGuard.model.Tree;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TreeListController {

    private static final String FILE_NAME = "trees.json";

    @FXML
    private TableView<Tree> treeTable;

    @FXML
    private TableColumn<Tree, String> nameColumn;

    @FXML
    private TableColumn<Tree, String> speciesColumn;

    @FXML
    private TableColumn<Tree, String> descriptionColumn;

    @FXML
    private TableColumn<Tree, String> imageColumn;

    @FXML
    private TableColumn<Tree, Void> actionsColumn;

    @FXML
    private TableColumn<Tree, Void> waterScheduleColumn;

    @FXML
    private TableColumn<Tree, String> wateredTodayColumn;
    @FXML
    private TableColumn<Tree, Void> growthViewerColumn;

    @FXML
    private Button btnAddTree;

    private ObservableList<Tree> trees;

    @FXML
    public void initialize() {
        try {
            setupTableColumns();
            trees = loadTreesFromJson();
            treeTable.setItems(trees);
            addButtonAction();
            btnAddTree.setOnAction(event -> addTree());
            setupWaterScheduleColumn();
            setupWateredTodayColumn();
        } catch (Exception e) {

        }
    }

    private void setupWaterScheduleColumn() {
        waterScheduleColumn.setCellFactory(new Callback<TableColumn<Tree, Void>, TableCell<Tree, Void>>() {
            @Override
            public TableCell<Tree, Void> call(TableColumn<Tree, Void> param) {
                return new TableCell<Tree, Void>() {
                    private final ToggleButton toggleButton = new ToggleButton("Đặt lịch tưới");

                    {
                        toggleButton.setStyle(
                                "-fx-background-color: #81c784; " +
                                        "-fx-text-fill: white; " +
                                        "-fx-font-size: 14px; " +
                                        "-fx-font-weight: bold; " +
                                        "-fx-border-radius: 5; " +
                                        "-fx-cursor: hand; " +
                                        "-fx-padding: 8px 16px; " +
                                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
                        );

                        toggleButton.selectedProperty().addListener((obs, oldVal, newVal) -> {
                            if (newVal) {
                                toggleButton.setStyle(
                                        "-fx-background-color: #4CAF50; " +
                                                "-fx-text-fill: white; " +
                                                "-fx-font-size: 14px; " +
                                                "-fx-font-weight: bold; " +
                                                "-fx-border-radius: 5; " +
                                                "-fx-cursor: hand; " +
                                                "-fx-padding: 8px 16px; " +
                                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 7, 0, 0, 3);"
                                );
                            } else {
                                toggleButton.setStyle(
                                        "-fx-background-color: #81c784; " +
                                                "-fx-text-fill: white; " +
                                                "-fx-font-size: 14px; " +
                                                "-fx-font-weight: bold; " +
                                                "-fx-border-radius: 5; " +
                                                "-fx-cursor: hand; " +
                                                "-fx-padding: 8px 16px; " +
                                                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 5, 0, 0, 2);"
                                );
                            }
                        });

                        toggleButton.setOnAction(event -> {
                            Tree tree = getTableView().getItems().get(getIndex());
                            if (toggleButton.isSelected()) {
                                setWaterSchedule(tree);
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(toggleButton);
                        }
                    }
                };
            }
        });
    }

    private void setupWateredTodayColumn() {
        wateredTodayColumn.setCellFactory(new Callback<TableColumn<Tree, String>, TableCell<Tree, String>>() {
            @Override
            public TableCell<Tree, String> call(TableColumn<Tree, String> param) {
                return new TableCell<Tree, String>() {
                    private final CheckBox checkBox = new CheckBox();
                    private final Label label = new Label();

                    {
                        checkBox.setOnAction(event -> {
                            Tree tree = getTableView().getItems().get(getIndex());
                            if (checkBox.isSelected()) {
                                LocalDate today = LocalDate.now();
                                if (today.toString().equals(tree.getNextWateringDate())) {
                                    tree.setWateredToday("true");
                                    label.setText("Đã tưới ngày: " + today);
                                    checkBox.setVisible(false);
                                    tree.setNextWateringDate(null);
                                    saveTreesToJson(trees.stream().collect(Collectors.toList()));
                                }
                            }
                        });
                    }

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || getTableView().getItems().get(getIndex()).getNextWateringDate() == null) {
                            setGraphic(null);
                        } else {
                            Tree tree = getTableView().getItems().get(getIndex());
                            LocalDate today = LocalDate.now();
                            if (today.toString().equals(tree.getNextWateringDate())) {
                                checkBox.setVisible(true);
                                label.setText("Tưới nước hôm nay!");
                                setGraphic(new HBox(checkBox, label));
                            } else {
                                checkBox.setVisible(false);
                                label.setText("Lịch tưới nước: " + tree.getNextWateringDate());
                                setGraphic(label);
                            }
                        }
                    }
                };
            }
        });
    }

    private void addButtonAction() {
        actionsColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = new Button("Sửa thông tin");
            private final Button deleteButton = new Button("-");
            private final Button addMoreImagesButton = new Button("➕ Thêm ảnh");
            private final VBox buttonBox = new VBox(editButton, deleteButton, addMoreImagesButton);

            {
                buttonBox.setSpacing(10);
                buttonBox.setAlignment(Pos.CENTER);
                editButton.setOnAction(event -> {
                    Tree tree = getTableView().getItems().get(getIndex());
                    editTree(tree);
                });

                deleteButton.setOnAction(event -> {
                    Tree tree = getTableView().getItems().get(getIndex());
                    removeTree(tree);
                });

                addMoreImagesButton.setOnAction(event -> {
                    Tree tree = getTableView().getItems().get(getIndex());
                    addMoreImages(tree);
                });
                deleteButton.setStyle(
                        "-fx-background-color: #FF5722; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-size: 12px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-border-radius: 15; " +
                                "-fx-cursor: hand; " +
                                "-fx-padding: 6px;"
                );

                editButton.setStyle(
                        "-fx-background-color: #4CAF50; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-size: 12px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-border-radius: 15; " +
                                "-fx-cursor: hand; " +
                                "-fx-padding: 6px;"
                );

                addMoreImagesButton.setStyle(
                        "-fx-background-color: #2196F3; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-size: 12px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-border-radius: 15; " +
                                "-fx-cursor: hand; " +
                                "-fx-padding: 6px;"
                );

                addMoreImagesButton.setOnMouseEntered(e -> addMoreImagesButton.setStyle(
                        "-fx-background-color: #1976D2; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-size: 12px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-border-radius: 15; " +
                                "-fx-cursor: hand; " +
                                "-fx-padding: 6px;"
                ));

                addMoreImagesButton.setOnMouseExited(e -> addMoreImagesButton.setStyle(
                        "-fx-background-color: #2196F3; " + // Quay lại màu ban đầu
                                "-fx-text-fill: white; " +
                                "-fx-font-size: 12px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-border-radius: 15; " +
                                "-fx-cursor: hand; " +
                                "-fx-padding: 6px;"
                ));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonBox);
                }
            }
        });
    }

    private void refreshTreeTable() {
        trees.clear();
        List<Tree> treeListFromJson = loadTreesFromJson();
        trees.setAll(treeListFromJson);
        treeTable.refresh();
    }

    private void setupTableColumns() {
        treeTable.getColumns().clear();
        growthViewerColumn = new TableColumn<>("Xem quá trình");
        setupGrowthViewerColumn();
        wateredTodayColumn.setCellValueFactory(new PropertyValueFactory<>("nextWateringDate"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        speciesColumn.setCellValueFactory(new PropertyValueFactory<>("species"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imageUrl"));
        wateredTodayColumn.setCellValueFactory(new PropertyValueFactory<>("wateredToday"));

        imageColumn.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            {
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(false);
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

        treeTable.getColumns().addAll(nameColumn, speciesColumn, descriptionColumn, imageColumn, wateredTodayColumn,waterScheduleColumn, growthViewerColumn, actionsColumn);
        treeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private ObservableList<Tree> loadTreesFromJson() {
        File file = new File(FILE_NAME);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (file.exists() && file.length() > 0) {
                return FXCollections.observableArrayList(objectMapper.readValue(file, new TypeReference<List<Tree>>() {}));
            }
        } catch (IOException e) {

        }
        return FXCollections.observableArrayList();
    }

    private void saveTreesToJson(List<Tree> treeList) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(com.fasterxml.jackson.databind.SerializationFeature.INDENT_OUTPUT);
        try {
            File file = new File(FILE_NAME);
            objectMapper.writeValue(file, treeList);
        } catch (IOException e) {

        }
    }

    private void addTree() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/treeaddform.fxml"));
            Parent addTreePane = loader.load();
            TreeAdderController controller = loader.getController();
            Stage stage = new Stage();
            stage.setTitle("Add Tree");
            stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/img/app_icon.png"))));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(addTreePane));
            stage.showAndWait();
            refreshTreeTable();
        } catch (IOException e) {

            showAlert("Error", "Unable to load the Add Tree form.");
        }
    }

    private void editTree(Tree tree) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/treeaddform.fxml"));
            Parent editTreePane = loader.load();
            TreeAdderController controller = loader.getController();
            controller.setTreeForEditing(tree);
            Stage stage = new Stage();
            stage.setTitle("Edit Tree");
            stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/img/app_icon.png"))));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(editTreePane));
            controller.setOnTreeEditedCallback(editedTree -> {
                int index = trees.indexOf(tree);
                if (index != -1) {
                    trees.set(index, editedTree);
                    saveTreesToJson(new ArrayList<>(trees));
                    treeTable.refresh();
                }
            });

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load the Edit Tree form.");
        }
    }

    private void removeTree(Tree tree) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Removal");
        confirmationAlert.setHeaderText("Are you sure you want to remove this tree?");
        confirmationAlert.setContentText("Tree: " + tree.getName());
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                trees.remove(tree);
                saveTreesToJson(trees.stream().collect(Collectors.toList()));
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                infoAlert.setTitle("Tree Removed");
                infoAlert.setHeaderText(null);
                infoAlert.setContentText("Tree \"" + tree.getName() + "\" has been removed.");
                infoAlert.showAndWait();
            }
        });
    }

    private void setWaterSchedule(Tree tree) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/waterschedule.fxml"));
            Parent waterSchedulePane = loader.load();
            WaterScheduleController controller = loader.getController();
            controller.setSelectedTree(tree);
            Stage stage = new Stage();
            stage.setTitle("Set Water Schedule");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(waterSchedulePane));
            controller.setStage(stage);
            stage.showAndWait();
            saveTreesToJson(trees.stream().collect(Collectors.toList()));
            treeTable.refresh();
        } catch (IOException e) {

            showAlert("Error", "Unable to load the water schedule form.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void setupGrowthViewerColumn() {
        growthViewerColumn.setCellFactory(column -> new TableCell<>() {
            private final Button viewButton = new Button("View Growth");

            {
                viewButton.setOnAction(event -> {
                    Tree tree = getTableView().getItems().get(getIndex());
                    showGrowthViewer(tree);
                });
                viewButton.setStyle(
                        "-fx-background-color: #4CAF50; " +
                                "-fx-text-fill: white; " +
                                "-fx-font-size: 12px; " +
                                "-fx-font-weight: bold; " +
                                "-fx-border-radius: 5; " +
                                "-fx-cursor: hand; " +
                                "-fx-padding: 6px 12px;"
                );
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(viewButton);
                }
            }
        });
    }

    private void showGrowthViewer(Tree tree) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/plantgrowthviewer.fxml"));
            Parent growthViewerPane = loader.load();
            PlantGrowthViewerController controller = loader.getController();
            controller.setTree(tree);
            Stage stage = new Stage();
            stage.setTitle("Quá trình sinh trưởng cây - " + tree.getName());
            stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/img/growth.png"))));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(growthViewerPane));
            stage.show();
        } catch (IOException e) {
            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Error loading Plant Growth Viewer", e);
            showAlert("Error", "Unable to load the Plant Growth Viewer.");
        }
    }

    private void addMoreImages(Tree tree) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select More Plant Images");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        Stage currentStage = new Stage();
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(currentStage);

        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Image Selection");
            alert.setHeaderText("Are you sure you want to add these images?");
            alert.setContentText("Selected images: " + selectedFiles.size() + "\nPress OK to confirm.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isEmpty() || result.get() != ButtonType.OK) {
                return;
            }

            File folder = new File(tree.getImagesFolderPath());
            if (!folder.exists()) {
                folder.mkdirs();
            }

            for (File selectedFile : selectedFiles) {
                try {
                    String fileName = System.currentTimeMillis() + "_" + selectedFile.getName();
                    File destFile = new File(folder, fileName);
                    Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    tree.addImageUrl(destFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            saveTreesToJson(trees.stream().collect(Collectors.toList()));
            treeTable.refresh();
        }
    }
}
