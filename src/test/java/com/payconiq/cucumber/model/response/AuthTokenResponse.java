package com.payconiq.cucumber.model.response;

public class AuthTokenResponse {
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;
}