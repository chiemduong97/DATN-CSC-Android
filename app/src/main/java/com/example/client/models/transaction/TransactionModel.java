package com.example.client.models.transaction;

public class TransactionModel {
    private int id;
    private int user;
    private int subject;
    private String ordercode;
    private Double amount;
    private String createAt;
    private int status;
    public TransactionModel(){

    }

    public TransactionModel(int id, int user, int subject, String ordercode, Double amount, String createAt, int status) {
        this.id = id;
        this.user = user;
        this.subject = subject;
        this.ordercode = ordercode;
        this.amount = amount;
        this.createAt = createAt;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public int getSubject() {
        return subject;
    }

    public void setSubject(int subject) {
        this.subject = subject;
    }

    public String getOrdercode() {
        return ordercode;
    }

    public void setOrdercode(String ordercode) {
        this.ordercode = ordercode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }


}
