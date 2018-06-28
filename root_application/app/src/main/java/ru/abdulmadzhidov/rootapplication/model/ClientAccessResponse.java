package ru.abdulmadzhidov.rootapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientAccessResponse {
    @SerializedName("token")
    @Expose
    private String client_token;

    public String getClient_token() {
        return client_token;
    }

    public void setClient_token(String client_token) {
        this.client_token = client_token;
    }
}
