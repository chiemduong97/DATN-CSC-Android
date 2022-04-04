package com.example.client.models.banner;

public class BannerModel {
    private int id;
    private String link;

    public BannerModel() {
    }

    public BannerModel(int id, String link) {
        this.id = id;
        this.link = link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
