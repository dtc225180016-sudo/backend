package com.edubot.backend;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chats") // Dữ liệu sẽ được lưu vào bảng tên là "chats"
public class ChatEntity {
    @Id
    private String id;
    private String userMessage;  // Chứa câu hỏi
    private String botResponse;  // Chứa câu trả lời

    // Các hàm này để giúp Code đọc/ghi dữ liệu (Getters & Setters)
    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getBotResponse() {
        return botResponse;
    }

    public void setBotResponse(String botResponse) {
        this.botResponse = botResponse;
    }
}