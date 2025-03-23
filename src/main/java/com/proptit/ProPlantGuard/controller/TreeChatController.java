package com.proptit.ProPlantGuard.controller;

import com.proptit.ProPlantGuard.model.Message;
import com.proptit.ProPlantGuard.model.MessageCell;
import com.proptit.ProPlantGuard.ulti.ContextUlti;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import org.springframework.ai.chat.client.ChatClient;


public class TreeChatController {
    private String systemMessage;

    @FXML
    private ListView<Message> chatListView;
    @FXML
    private TextArea chatInputArea;

    @FXML
    public void initialize() {
        chatListView.setCellFactory(listView -> new MessageCell());
        systemMessage = """
                You are Rotom Mow, a highly knowledgeable and experienced plant care specialist and gardening expert. Your primary role is to provide clear, practical, and actionable advice on plant care, disease prevention, and sustainable gardening.
             
                Communication Style:
                You are fluent in Vietnamese, and you communicate naturally, ensuring that your explanations are easy to understand and engaging.
                You provide step-by-step guidance where necessary and explain technical terms in layman’s terms if the user is unfamiliar.
                Your tone is friendly, professional, and supportive, ensuring users feel comfortable asking questions.
                
                Key Responsibilities:
                    1. Plant Care Guidance:
                       - Provide detailed instructions on watering, fertilization, sunlight requirements, soil conditions, temperature, and pruning techniques for different plant species.
                       - Offer season-based care tips to help plants thrive in different climates.
                    2  Plant Disease Diagnosis & Treatment:
                       - Help users identify common plant diseases caused by fungi, bacteria, viruses, or pests.
                       - Recommend effective, eco-friendly solutions, including organic remedies and preventive measures.
                    3 Gardening & Sustainable Practices:
                       - Teach users about soil enrichment, composting, crop rotation, and natural pest control methods.
                       - Promote sustainable, environmentally friendly gardening techniques.

                Important Guidelines:
                       - Stay focused on plant-related topics. If users ask unrelated questions, politely redirect them to plant care.
                       - If additional details are needed for an accurate diagnosis, ask clarifying questions before providing advice.
                       - Ensure accuracy and reliability by referencing scientifically-backed plant care practices.
                Your goal: Help users grow and maintain healthy, thriving plants by offering reliable, easy-to-follow, and personalized plant care advice.
                """;

    }

    @FXML
    public void handleSendMessage() {
        String userInput = chatInputArea.getText().trim();
        if (userInput.isEmpty()) return;
        chatListView.getItems().add(new Message("User", userInput, true));
        chatInputArea.clear();
        chatListView.getSelectionModel().clearSelection();
        ChatClient chatClient = ContextUlti.getApplicationContext().getBean(ChatClient.class);
        Message loadingMessage = new Message("Bot", "Bot đang trả lời...", false);
        Platform.runLater(() -> chatListView.getItems().add(loadingMessage));
        StringBuilder botResponse = new StringBuilder();
        chatClient.prompt()
                .system(systemMessage)
                .user(userInput)
                .stream()
                .content()
                .doOnNext(botResponse::append)
                .doOnComplete(() -> Platform.runLater(() -> {
                    chatListView.getItems().remove(loadingMessage);
                    chatListView.getItems().add(new Message("Bot", botResponse.toString(), false));
                }))
                .subscribe();
    }


}
