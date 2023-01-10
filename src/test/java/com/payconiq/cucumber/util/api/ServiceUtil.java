package com.payconiq.cucumber.util.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.payconiq.cucumber.util.Logger.LoggerFile;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public class ServiceUtil {
    protected static RequestSpecification request;
    protected static Response response;
    protected static String token = null;

    protected static int execute(String url,
                                 Method methodType,
                                 Map<String, String> queryParams,
                                 Map<String, String> headers,
                                 String jsonPayload) {
        LoggerFile.log("################# Request Details ########################");
        LoggerFile.log("URL :" + url);
        LoggerFile.log("Method Type :" + methodType);
        LoggerFile.log("Query params : " + queryParams);
        LoggerFile.log("Headers : " + headers);
        if (jsonPayload != null) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement je = JsonParser.parseString(jsonPayload);
            String prettyJsonString = gson.toJson(je);
            LoggerFile.log("Payload : \n" + prettyJsonString);
        }
        LoggerFile.log("##########################################################");
        request = RestAssured.given();
        request.baseUri(Constants.apiBaseUrl);
        switch (methodType) {
            case GET:
                if (queryParams != null)
                    response = request.when().headers(headers).queryParams(queryParams).get(url);
                else
                    response = request.when().headers(headers).get(url);
                break;
            case POST:
                if (headers != null)
                    response = request.when().headers(headers).body(jsonPayload).post(url);
                else
                    response = request.when().body(jsonPayload).post(url);
                break;
            case PATCH:
                response = request.when().headers(headers).body(jsonPayload).patch(url);
                break;
            case PUT:
                response = request.when().headers(headers).body(jsonPayload).put(url);
                break;
            case DELETE:
                response = request.when().headers(headers).delete(url);
                break;
            default:
                throw new RuntimeException("Method :" + methodType + " is not found");
        }
        LoggerFile.log("########### Response Details #############################");
        LoggerFile.log("Status code :" + String.valueOf(response.getStatusCode()));
        LoggerFile.log("Status line :" + response.getStatusLine());
        LoggerFile.log("Response Headers : " + response.getHeaders());
        LoggerFile.log("Json Response body :" + response.getBody().prettyPrint().toString());
        LoggerFile.log("##########################################################");
        return response.getStatusCode();


    }


}