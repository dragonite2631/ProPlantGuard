
package com.proptit.ProPlantGuard;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.util.Objects;



public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent parent = FXMLLoader.load(Objects.requireNonNull(MainApplication.class.getResource("/fxml/mainwindow.fxml")));
        Scene scene = new Scene(parent);
        stage.setTitle("ProPlantGuard");
        stage.setScene(scene);
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/img/app_icon.png"))));
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        stage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }
    public static void launchApp(){
        MainApplication.launch(MainApplication.class,"");
    }
}




