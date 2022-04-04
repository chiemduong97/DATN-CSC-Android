package com.example.client.models.message;

public class MessageModel {
    private boolean status;
    private int code;
    private String accessToken;

    public MessageModel() {
    }

    public MessageModel(boolean status, int code, String accessToken) {
        this.status = status;
        this.code = code;
        this.accessToken = accessToken;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isStatus() {
        return status;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
