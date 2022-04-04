package com.example.client.models.profile;

public class ProfileModel {
    private int id;
    private String email;
    private String avatar;
    private String fullname;
    private String birthday;
    private String phone;
    private String address;
    private int isactive;
    private int isadmin;
    private Double wallet;

    public ProfileModel() {
    }

    public ProfileModel(int id, String email, String avatar, String fullname, String birthday, String phone, String address, int isactive, int isadmin, Double wallet) {
        this.id = id;
        this.email = email;
        this.avatar = avatar;
        this.fullname = fullname;
        this.birthday = birthday;
        this.phone = phone;
        this.address = address;
        this.isadmin = isadmin;
        this.wallet = wallet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIsactive() {
        return isactive;
    }

    public void setIsactive(int isactive) {
        this.isactive = isactive;
    }

    public int getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(int isadmin) {
        this.isadmin = isadmin;
    }

    public Double getWallet() {
        return wallet;
    }

    public void setWallet(Double wallet) {
        this.wallet = wallet;
    }
}
