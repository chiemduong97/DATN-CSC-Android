package com.example.client.models.message;

public class MessageModel {
    private boolean status;
    private int code;
    private String accessToken;
    private String ordercode;

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

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getOrdercode() {
        return ordercode;
    }

    public void setOrdercode(String ordercode) {
        this.ordercode = ordercode;
    }
}
