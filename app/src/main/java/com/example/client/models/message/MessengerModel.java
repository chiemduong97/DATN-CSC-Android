package com.example.client.models.message;

public class MessengerModel {
    private int id;
    private String data;
    private String createAt;
    private boolean fromMe;

    public MessengerModel(int id, String data, String createAt, boolean fromMe) {
        this.id = id;
        this.data = data;
        this.createAt = createAt;
        this.fromMe = fromMe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public boolean isFromMe() {
        return fromMe;
    }

    public void setFromMe(boolean fromMe) {
        this.fromMe = fromMe;
    }
}
