package com.payconiq.cucumber.Service;

import com.payconiq.cucumber.util.api.APIConstants;
import com.payconiq.cucumber.util.api.APIUtils;
import io.restassured.http.Method;

import java.util.HashMap;

public class Ping extends APIUtils {
    private static String pingURL= APIConstants.apiHealthCheckUrl;
    public int doHealthCheck(HashMap<String,String> headers)
    {
        return execute(pingURL, Method.GET,null,
                headers,null);
    }
}
