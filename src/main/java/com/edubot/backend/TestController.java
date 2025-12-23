package com.edubot.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String xinChao() {
        return "<h1>Chúc mừng! EduBot của nhóm đã chạy thành công!</h1>";
    }
}