// src/main/java/com/ureca/user/dto/User.java
package com.ureca.user.dto;

public class User {
    private Integer user_id;
    private String name;
    private int class_id;
    private String email;
    private String password;
    private String profile_picture; // 필드 정의

    // Getters and Setters
    public Integer getUser_id() {
        return user_id;
    }
    
    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // 정확한 getter와 setter 추가
    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", class_id='" + class_id + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", profile_picture='" + profile_picture + '\'' +
                '}';
    }
}