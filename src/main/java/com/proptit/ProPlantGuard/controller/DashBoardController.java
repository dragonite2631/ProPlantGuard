package com.proptit.ProPlantGuard.controller;

import com.proptit.ProPlantGuard.model.TreeAdder;
import com.proptit.ProPlantGuard.model.TreeManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
public class DashBoardController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private Button btnLibrary;

    @FXML
    private Button btnAdd;


    @FXML
    private Button btnSchedule;

    @FXML
    private Button btnLog;

    @FXML
    private Button btnTips;

    @FXML
    public void initialize() {
        btnLibrary.setOnAction(event -> TreeManager.showTreeViewer(rootPane));
        btnAdd.setOnAction(event -> TreeAdder.showAddContent(rootPane));
        btnSchedule.setOnAction(event -> showScheduleContent());
        btnLog.setOnAction(event -> showLogContent());
        btnTips.setOnAction(event -> showTipsContent());
    }


    private void showScheduleContent() {
        Pane schedulePane = new Pane(new Text("Watering Schedule"));
        schedulePane.setStyle("-fx-background-color: #e6f2ff;");
        rootPane.setCenter(schedulePane);
    }

    private void showLogContent() {
        Pane logPane = new Pane(new Text("Care Log"));
        logPane.setStyle("-fx-background-color: #ffffcc;");
        rootPane.setCenter(logPane);
    }

    private void showTipsContent() {
        Pane tipsPane = new Pane(new Text("Care Tips"));
        tipsPane.setStyle("-fx-background-color: #f2e6ff;");
        rootPane.setCenter(tipsPane);
    }
}