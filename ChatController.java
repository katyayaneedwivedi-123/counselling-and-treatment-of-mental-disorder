package com.example.chatbot1;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class ChatController {

    private List<Map<String, String>> faqData;
    private boolean initialGreetingSent = false;

    public ChatController() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            faqData = objectMapper.readValue(Paths.get("src/main/resources/faq.json").toFile(), List.class);
        } catch (Exception e) {
            System.out.println("Error loading FAQ file: " + e.getMessage());
        }
    }

    // Home route
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/bot")
    public String bot() {
        return "bot";
    }

    @GetMapping("/appointment")
    public String appointment() {
        return "appointment";
    }

    @GetMapping("/Lifestyle")
    public String lifestyle() {
        return "assessment";
    }

    @GetMapping("/Mental-Health")
    public String mentalHealth() {
        return "assessment1";
    }

    @GetMapping("/Lifestyle-Test")
    public String lifestyleTest() {
        return "LAssessment";
    }

    @GetMapping("/treatment")
    public String treatment() {
        return "treatment";
    }

    @GetMapping("/Mental-Health-Test")
    public String mentalHealthTest() {
        return "MAssessment";
    }

    @GetMapping("/nearyou")
    public String nearyou() {
        return "nearyou";
    }

    @GetMapping("/user-profile")
    public String userProfile() {
        return "userprofile";
    }

    @PostMapping("/chat")
    @ResponseBody
    public ResponseEntity<String> chat(@RequestParam String userMessage) {
        String response = processMessage(userMessage);
        return ResponseEntity.ok(response);
    }

    private String processMessage(String userMessage) {
        // Check if user message is a question
        if (userMessage.trim().endsWith("?")) {
            String userQuestion = userMessage.trim().substring(0, userMessage.length() - 1);

            Optional<Map<String, String>> matchedQA = faqData.stream()
                    .filter(qa -> qa.get("question").equalsIgnoreCase(userQuestion))
                    .findFirst();

            if (matchedQA.isPresent()) {
                return "Bot: " + matchedQA.get().get("answer");
            } else {
                return "Bot: Sorry, I couldn't find an answer to your question.";
            }
        } else {
            // Analyze sentiment of the user's message
            String sentiment = SentimentAnalysis.analyze(userMessage);
            if ("positive".equals(sentiment)) {
                return "Bot: That's great to hear!";
            } else if ("negative".equals(sentiment)) {
                return "Bot: I'm sorry to hear that you're experiencing difficulties.";
            } else {
                return "Bot: Thank you for sharing!";
            }
        }
    }
}
