package ru.abdulmadzhidov.client_application.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    public static User instance = new User();

    @SerializedName("email")
    @Expose
    private String email;
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

