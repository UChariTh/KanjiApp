package com.example.kanji2.ResponseModels;

public class SpeakingResponse {
    private String letter;

    // Default constructor required for Jackson JSON serialization/deserialization
    public SpeakingResponse() {
    }



    public String getNumber() {
        return letter;
    }
}
