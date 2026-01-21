package com.edubot.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ConsultationController {

    private final ConsultationRepository consultationRepository;

    public ConsultationController(ConsultationRepository consultationRepository) {
        this.consultationRepository = consultationRepository;
    }

    @PostMapping("/consultation")
    public ResponseEntity<Map<String, Object>> create(@RequestBody Map<String, Object> body) {
        Map<String, Object> res = new HashMap<>();

        try {
            String name = toStr(body.get("name"));
            String email = toStr(body.get("email"));
            String phone = toStr(body.get("phone"));
            String question = toStr(body.get("question"));

            if (isBlank(name) || isBlank(email) || isBlank(phone) || isBlank(question)) {
                res.put("ok", false);
                res.put("message", "Thiếu dữ liệu (name/email/phone/question)");
                return ResponseEntity.badRequest().body(res);
            }

            Consultation c = new Consultation();
            c.setName(name.trim());
            c.setEmail(email.trim());
            c.setPhone(phone.trim());
            c.setQuestion(question.trim());

            // 2 field này có trong Consultation.java của bạn
            c.setStatus("NEW");
            c.setCreatedAt(Instant.now());

            Consultation saved = consultationRepository.save(c);

            res.put("ok", true);
            res.put("item", saved);
            return ResponseEntity.ok(res);

        } catch (Exception e) {
            // Quan trọng: để Railway Logs thấy lỗi thật
            e.printStackTrace();

            res.put("ok", false);
            res.put("message", "Lỗi server khi lưu tư vấn");
            res.put("details", e.getMessage() == null ? "unknown" : e.getMessage());
            return ResponseEntity.status(500).body(res);
        }
    }

    @GetMapping("/consultations")
    public ResponseEntity<Map<String, Object>> list() {
        Map<String, Object> res = new HashMap<>();
        try {
            List<Consultation> items = consultationRepository.findAll();
            res.put("ok", true);
            res.put("items", items);
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            e.printStackTrace();
            res.put("ok", false);
            res.put("message", "Lỗi server khi lấy danh sách tư vấn");
            res.put("details", e.getMessage() == null ? "unknown" : e.getMessage());
            return ResponseEntity.status(500).body(res);
        }
    }

    private static String toStr(Object v) {
        return v == null ? null : String.valueOf(v);
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
