package com.example.client.models.nhanvien;

public class NhanVienModel {
    private int id;
    private String name;
    private Double salary;
    private String department;
    private String address;
    private String position;

    public NhanVienModel(int id, String name, Double salary, String department, String address, String position) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.department = department;
        this.address = address;
        this.position = position;
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

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
