package com.proptit.ProPlantGuard.model;

import com.proptit.ProPlantGuard.model.Message;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

public class MessageCell extends ListCell<Message> {
    private final ImageView avatarUser = new ImageView(new Image(String.valueOf(getClass().getResource("/img/user.png"))));
    private final ImageView avatarBot = new ImageView(new Image(String.valueOf(getClass().getResource("/img/bot.png "))));

    public MessageCell() {
        avatarUser.setFitWidth(30);
        avatarUser.setFitHeight(30);
        avatarBot.setFitWidth(30);
        avatarBot.setFitHeight(30);
    }

    @Override
    protected void updateItem(Message message, boolean empty) {
        super.updateItem(message, empty);

        if (empty || message == null) {
            setGraphic(null);
            return;
        }

        Label contentLabel = new Label(message.getContent());
        contentLabel.setWrapText(true);
        contentLabel.setMaxWidth(450);
        contentLabel.setStyle("-fx-padding: 10px; -fx-background-radius: 10px;");

        HBox messageContainer = new HBox(10);
        if (message.isUserMessage()) {
            contentLabel.setStyle("-fx-background-color: #e5e5ea; -fx-text-fill: black;");
            messageContainer.setAlignment(Pos.TOP_RIGHT);
            messageContainer.getChildren().addAll(contentLabel, avatarUser);
        } else {
            if (message.getContent().equals("Bot đang trả lời...")) {
                contentLabel.setStyle("-fx-background-color: #dcdcdc; -fx-text-fill: gray;");
                startTypingAnimation(contentLabel);
            } else {
                contentLabel.setStyle("-fx-background-color: #e5e5ea; -fx-text-fill: black;");
            }
            messageContainer.setAlignment(Pos.TOP_LEFT);
            messageContainer.getChildren().addAll(avatarBot, contentLabel);
        }

        setGraphic(messageContainer);
    }


    private void startTypingAnimation(Label label) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), e -> label.setText("Bot đang trả lời.")),
                new KeyFrame(Duration.seconds(1.0), e -> label.setText("Bot đang trả lời..")),
                new KeyFrame(Duration.seconds(1.5), e -> label.setText("Bot đang trả lời..."))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

}
