package com.example.kursova_work;

public class CreateUser {

    public  CreateUser(){

    }

    public CreateUser(String name, String email, String password, String code, String isSharing, String lat, String lng, String imageUrl) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.code = code;
        this.isSharing = isSharing;
        this.lat = lat;
        this.lng = lng;
        this.imageUrl = imageUrl;
    }

    public String name;
    public String email;
    public String password;
    public String code;
    public String isSharing;
    public String lat;
    public String lng;
    public String imageUrl;
}
