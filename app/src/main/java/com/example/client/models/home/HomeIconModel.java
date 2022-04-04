package com.example.client.models.home;

public class HomeIconModel {
    private int icon;
    private String title;
    public HomeIconModel(){

    }

    public HomeIconModel(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
