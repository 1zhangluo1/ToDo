package com.example.needtodo;

public class Messages {
    public static String SEND_BY_ME = "me";
    public static String SEND_BY_AI = "ai";

    private String message;
    private String sendBy;

    public static String getSendByMe() {
        return SEND_BY_ME;
    }

    public static void setSendByMe(String sendByMe) {
        SEND_BY_ME = sendByMe;
    }

    public static String getSendByAi() {
        return SEND_BY_AI;
    }

    public static void setSendByAi(String sendByAi) {
        SEND_BY_AI = sendByAi;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendBy() {
        return sendBy;
    }

    public void setSendBy(String sendBy) {
        this.sendBy = sendBy;
    }

    public Messages(String message, String sendBy) {
        this.message = message;
        this.sendBy = sendBy;
    }
}
