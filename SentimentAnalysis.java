package com.example.chatbot2;

public class SentimentAnalysis {
    public static String analyze(String message) {

        if (message.contains("good") || message.contains("happy")) {
            return "positive";
        } else if (message.contains("bad") || message.contains("sad")) {
            return "negative";
        } else {
            return "neutral";
        }
    }
}
