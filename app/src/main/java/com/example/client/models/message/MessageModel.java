package com.example.client.models.message;

public class MessageModel {
    private boolean isError;
    private int code;
    private String accessToken;
    private String ordercode;

    public MessageModel() {
    }

    public MessageModel(boolean status, int code, String accessToken) {
        this.isError = status;
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
        return isError;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setStatus(boolean status) {
        this.isError = status;
    }

    public String getOrdercode() {
        return ordercode;
    }

    public void setOrdercode(String ordercode) {
        this.ordercode = ordercode;
    }
}
