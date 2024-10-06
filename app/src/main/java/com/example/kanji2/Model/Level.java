package com.example.kanji2.Model;

public class Level {

    private String levelName,letter;
    private boolean isWriteCompleted,isSpeakCompleted;

    public Level() {
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public boolean isWriteCompleted() {
        return isWriteCompleted;
    }

    public void setWriteCompleted(boolean writeCompleted) {
        isWriteCompleted = writeCompleted;
    }

    public boolean isSpeakCompleted() {
        return isSpeakCompleted;
    }

    public void setSpeakCompleted(boolean speakCompleted) {
        isSpeakCompleted = speakCompleted;
    }
}
