package com.edubot.backend;

import org.springframework.web.bind.annotation.*;

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
    public Map<String, Object> create(@RequestBody Consultation body) {
        Map<String, Object> res = new HashMap<>();

        if (body == null
                || body.getName() == null || body.getName().trim().isEmpty()
                || body.getEmail() == null || body.getEmail().trim().isEmpty()
                || body.getPhone() == null || body.getPhone().trim().isEmpty()) {
            res.put("ok", false);
            res.put("error", "Missing required fields");
            return res;
        }

        body.setName(body.getName().trim());
        body.setEmail(body.getEmail().trim());
        body.setPhone(body.getPhone().trim());
        if (body.getQuestion() != null) body.setQuestion(body.getQuestion().trim());

        Consultation saved = consultationRepository.save(body);

        res.put("ok", true);
        res.put("item", saved);
        return res;
    }

    @GetMapping("/consultations")
    public Map<String, Object> list() {
        Map<String, Object> res = new HashMap<>();
        List<Consultation> items = consultationRepository.findAll();
        res.put("ok", true);
        res.put("items", items);
        return res;
    }
}
