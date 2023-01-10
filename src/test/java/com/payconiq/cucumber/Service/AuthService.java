package com.payconiq.cucumber.Service;

import com.google.gson.Gson;
import com.payconiq.cucumber.model.request.AuthToken;
import com.payconiq.cucumber.model.response.AuthTokenResponse;
import com.payconiq.cucumber.util.api.Constants;
import com.payconiq.cucumber.util.api.ServiceUtil;
import io.restassured.http.Method;

import java.util.Base64;
import java.util.HashMap;

public class AuthService extends ServiceUtil {
    private static String authURL = Constants.apiAuthUrl;

    public String createToken() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(Constants.header_ContentType, Constants.header_ApplicationJson);
        if (token == null) {
            AuthToken authTokenRequest = new AuthToken();
            authTokenRequest.setUsername(new String(Base64.getDecoder().decode(Constants.apiUserName)));
            authTokenRequest.setPassword(new String(Base64.getDecoder().decode(Constants.apiPassWord)));

            execute(authURL,
                    Method.POST,
                    null,
                    headers,
                    new Gson().toJson(authTokenRequest)
            );

            AuthTokenResponse authTokenResponse = new Gson().fromJson(response.getBody().prettyPrint(), AuthTokenResponse.class);
            token = authTokenResponse.token;
        }
        return Constants.token + token;
    }
}
