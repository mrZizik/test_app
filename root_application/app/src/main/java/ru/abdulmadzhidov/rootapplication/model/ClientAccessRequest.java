package ru.abdulmadzhidov.rootapplication.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientAccessRequest {
    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("client_id")
    @Expose
    private String client_id;

    @SerializedName("client_secret")
    @Expose
    private String client_secret;

    @SerializedName("scope")
    @Expose
    private String scope;


    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getClient_secret() {
        return client_secret;
    }

    public void setClient_secret(String client_secret) {
        this.client_secret = client_secret;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
