package com.example.quanglong.model;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String Name;
    private Integer Age;
    private Integer Phone;
    public String image;
    public String status;

    public String role;


    public User() {
    }

    public User(String name, Integer age, Integer phone, String status,String image) {
        Name = name;
        Age = age;
        Phone = phone;
        this.status = status;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getAge() {
        return Age;
    }

    public void setAge(Integer age) {
        Age = age;
    }

    public Integer getPhone() {
        return Phone;
    }

    public void setPhone(Integer phone) {
        Phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", Name='" + Name + '\'' +
                ", Age=" + Age +
                ", Phone=" + Phone +
                ", Status='" + status + '\'' +
                '}';
    }
}
