package com.proptit.ProPlantGuard.controller;

import com.proptit.ProPlantGuard.model.Tree;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlantGrowthViewerController {

    @FXML
    private ImageView imageView;

    @FXML
    private Button playPauseButton;

    private List<Image> images;
    private int currentIndex;
    private Timeline timeline;
    private boolean isPlaying;

    private Tree tree;

    public void initialize() {
        images = new ArrayList<>();
        currentIndex = 0;
        isPlaying = false;
        playPauseButton.setOnAction(event -> togglePlayPause());
        setupTimeline();
        imageView.setFitWidth(600);
        imageView.setFitHeight(600);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
    }

    public void setTree(Tree tree) {
        this.tree = tree;
        loadImages();
        if (!images.isEmpty()) {
            imageView.setImage(images.get(0));
        }
    }

    private void loadImages() {
        File folder = new File(tree.getImagesFolderPath());
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && file.getName().toLowerCase().matches(".*\\.(jpg|jpeg|png|gif)$")) {
                    images.add(new Image(file.toURI().toString()));
                }
            }
        }
    }

    private void setupTimeline() {
        timeline = new Timeline(
            new KeyFrame(Duration.seconds(0.4), event -> {
                showNextImage();
                if(currentIndex >= images.size()){
                    currentIndex = 0;
                    timeline.stop();
                }
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    @FXML
    private void togglePlayPause() {
        if (isPlaying) {
            timeline.pause();
            playPauseButton.setText("Play");
            isPlaying = false;
        } else {
            timeline.play();
            playPauseButton.setText("Pause");
            isPlaying = true;
        }
    }

    private void showNextImage() {
        if (!images.isEmpty()) {
            imageView.setImage(images.get((currentIndex++) % images.size()));
        }
    }
}