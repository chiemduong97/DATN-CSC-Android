package com.example.client.models.category;

import java.io.Serializable;

public class CategoryModel implements Serializable {
    private int id;
    private String name;
    private String avatar;
    public CategoryModel(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
