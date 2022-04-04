package com.example.client.models.subject;

public class SubjectModel {
    private int id;
    private int category;
    private int teacher;
    private String avatar;
    private String name;
    private String description;
    private String start;
    private String end;
    private String time;
    private Double rate;
    private Double price;
    private String createAt;
    private int quantity;

    public SubjectModel() {
    }

    public SubjectModel(int id, int category, int teacher, String avatar, String name, String description, String start, String end, String time, Double rate, Double price, String createAt, int quantity) {
        this.id = id;
        this.category = category;
        this.teacher = teacher;
        this.avatar = avatar;
        this.name = name;
        this.description = description;
        this.start = start;
        this.end = end;
        this.time = time;
        this.rate = rate;
        this.price = price;
        this.createAt = createAt;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getTeacher() {
        return teacher;
    }

    public void setTeacher(int teacher) {
        this.teacher = teacher;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
