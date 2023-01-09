package com.payconiq.cucumber.Service;

import com.google.gson.Gson;
import com.payconiq.cucumber.model.request.AuthTokenRequest;
import com.payconiq.cucumber.model.response.AuthTokenResponse;
import com.payconiq.cucumber.util.api.APIConstants;
import com.payconiq.cucumber.util.api.APIUtils;
import io.restassured.http.Method;

import java.util.Base64;
import java.util.HashMap;

public class Auth extends APIUtils {
    private static String authURL= APIConstants.apiAuthUrl;
    public String createToken()
    {
        HashMap<String,String> headers= new HashMap<>();
        headers.put(APIConstants.header_ContentType,APIConstants.header_ApplicationJson);
        if(token==null) {
            AuthTokenRequest authTokenRequest = new AuthTokenRequest();
            authTokenRequest.username = new String(Base64.getDecoder().decode(APIConstants.apiUserName));
            authTokenRequest.password = new String (Base64.getDecoder().decode(APIConstants.apiPassWord));

            execute(authURL,
                    Method.POST,
                    null,
                    headers,
                    new Gson().toJson(authTokenRequest)
            );

            AuthTokenResponse authTokenResponse = new Gson().fromJson(response.getBody().prettyPrint(), AuthTokenResponse.class);
            token=authTokenResponse.token;
        }
        return APIConstants.token+token;
    }
}
