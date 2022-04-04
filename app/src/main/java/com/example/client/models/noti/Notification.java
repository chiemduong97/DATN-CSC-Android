package com.example.client.models.noti;

public class Notification {
    private int id;
    private String action;
    private String description;
    private int user;
    private String createAt;

    public Notification(int id, String action, String description, int user, String createAt) {
        this.id = id;
        this.action = action;
        this.description = description;
        this.user = user;
        this.createAt = createAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }
}
