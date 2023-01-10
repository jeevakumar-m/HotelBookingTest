package com.payconiq.cucumber.Service;

import com.payconiq.cucumber.util.api.Constants;
import com.payconiq.cucumber.util.api.ServiceUtil;
import io.restassured.http.Method;

import java.util.Map;

public class PingService extends ServiceUtil {
    private static String pingURL = Constants.apiHealthCheckUrl;

    public int doHealthCheck(Map<String, String> headers) {
        return execute(pingURL, Method.GET, null,
                headers, null);
    }
}
