package com.edubot.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private GeminiService geminiService;

    @Autowired
    private ChatRepository chatRepository;

    // Health check
    @GetMapping("/api/health")
    public Map<String, Object> health() {
        Map<String, Object> res = new HashMap<>();
        res.put("ok", true);
        return res;
    }

    // React gọi cái này: POST /api/chat  { "message": "..." }
    @PostMapping("/api/chat")
    public Map<String, Object> chatApi(@RequestBody Map<String, Object> body) {
        String message = body == null || body.get("message") == null ? "" : body.get("message").toString().trim();

        Map<String, Object> res = new HashMap<>();
        if (message.isEmpty()) {
            res.put("error", "message is required");
            return res;
        }

        String reply = geminiService.callGemini(message);

        // lưu MongoDB
        try {
            ChatEntity chatLog = new ChatEntity();
            chatLog.setUserMessage(message);
            chatLog.setBotResponse(reply);
            chatRepository.save(chatLog);
        } catch (Exception e) {
            System.out.println("❌ Lỗi lưu database: " + e.getMessage());
        }

        res.put("reply", reply);
        return res;
    }

    // xem lịch sử chat
    @GetMapping("/api/logs")
    public java.util.List<ChatEntity> getLogs() {
        return chatRepository.findAll();
    }

    // GET /chat?message=hello (test nhanh)
    @GetMapping("/chat")
    public String chatOld(@RequestParam String message) {
        String response = geminiService.callGemini(message);

        try {
            ChatEntity chatLog = new ChatEntity();
            chatLog.setUserMessage(message);
            chatLog.setBotResponse(response);
            chatRepository.save(chatLog);
        } catch (Exception e) {
            System.out.println("❌ Lỗi lưu database: " + e.getMessage());
        }

        return response;
    }
}
