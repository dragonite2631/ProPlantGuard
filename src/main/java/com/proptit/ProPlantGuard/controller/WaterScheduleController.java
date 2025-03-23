package com.proptit.ProPlantGuard.controller;

import com.proptit.ProPlantGuard.model.Tree;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class WaterScheduleController {

    @FXML
    private ComboBox<String> waterScheduleComboBox;

    @FXML
    private TextField customDaysField;

    @FXML
    private ComboBox<String> customUnitComboBox;

    @FXML
    private Button submitButton;

    private Stage currentStage;
    private Tree selectedTree;

    @FXML
    private void initialize() {
        ObservableList<String> wateringFrequencyOptions = FXCollections.observableArrayList(
                "Every day",
                "Every 2 days",
                "Every 3 days",
                "Every week",
                "Every 2 weeks",
                "Custom"
        );
        waterScheduleComboBox.setItems(wateringFrequencyOptions);

        ObservableList<String> customUnitOptions = FXCollections.observableArrayList(
                "Days",
                "Weeks"
        );
        customUnitComboBox.setItems(customUnitOptions);
        customUnitComboBox.setValue("Days");

        waterScheduleComboBox.setOnAction(event -> {
            boolean isCustom = "Custom".equals(waterScheduleComboBox.getValue());
            customDaysField.setDisable(!isCustom);
            customUnitComboBox.setDisable(!isCustom);
        });

        submitButton.setOnAction(event -> handleSubmit());
    }
    public void setSelectedTree(Tree tree) {
        this.selectedTree = tree;
        if (tree.getWaterSchedule() != null) {
            waterScheduleComboBox.setValue(tree.getWaterSchedule());
        }
    }

    public void setStage(Stage stage) {
        this.currentStage = stage;
    }

    private void handleSubmit() {
        String selectedSchedule = waterScheduleComboBox.getValue();
        if (selectedSchedule != null && selectedTree != null) {
            if ("Custom".equals(selectedSchedule)) {
                try {
                    int customDays = Integer.parseInt(customDaysField.getText());
                    String unit = customUnitComboBox.getValue();
                    selectedSchedule = "Every " + customDays + " " + unit.toLowerCase();
                } catch (NumberFormatException e) {
                    showAlert("Invalid Input", "Please enter a valid number for custom schedule.");
                    return;
                }
            }
            updateTreeSchedule(selectedTree, selectedSchedule);
            currentStage.close();
        } else {
            showAlert("Invalid Selection", "Please select a watering schedule.");
        }
    }

    private void updateTreeSchedule(Tree selectedTree, String selectedSchedule) {
        selectedTree.setWaterSchedule(selectedSchedule);
        LocalDate nextWateringDate = LocalDate.now();

        switch (selectedSchedule) {
            case "Every day":
                nextWateringDate = nextWateringDate.plusDays(1);
                break;
            case "Every 2 days":
                nextWateringDate = nextWateringDate.plusDays(2);
                break;
            case "Every 3 days":
                nextWateringDate = nextWateringDate.plusDays(3);
                break;
            case "Every week":
                nextWateringDate = nextWateringDate.plusWeeks(1);
                break;
            case "Every 2 weeks":
                nextWateringDate = nextWateringDate.plusWeeks(2);
                break;
            default:
                if (selectedSchedule.startsWith("Every ")) {
                    String[] parts = selectedSchedule.split(" ");
                    if (parts.length == 3) {
                        int value = Integer.parseInt(parts[1]);
                        String unit = parts[2];
                        if (unit.equalsIgnoreCase("days") || unit.equalsIgnoreCase("day")) {
                            nextWateringDate = nextWateringDate.plusDays(value);
                        } else if (unit.equalsIgnoreCase("weeks") || unit.equalsIgnoreCase("week")) {
                            nextWateringDate = nextWateringDate.plusWeeks(value);
                        }
                    }
                }
                break;
        }
        selectedTree.setNextWateringDate(nextWateringDate.toString());
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}