package com.example.kursova_work;

/**
 * СreateUser - a class for storing user data,
 * for interacting with the server: authorization, location, etc.
 * @version 1.0
 */
public class CreateUser {

    public  CreateUser(){}

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

    private String name;
    private String email;
    private String password;
    private String code;
    private String isSharing;
    private String lat;
    private String lng;
    private String imageUrl;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getCode() {
        return code;
    }

    public String getIsSharing() {
        return isSharing;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setIsSharing(String isSharing) {
        this.isSharing = isSharing;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
