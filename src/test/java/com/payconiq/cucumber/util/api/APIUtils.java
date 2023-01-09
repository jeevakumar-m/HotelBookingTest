package com.payconiq.cucumber.util.api;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;

public class APIUtils
        {
                protected static RequestSpecification request;
                protected static Response response;
                protected static String token=null;

                protected static int execute(String url,
                                           Method methodType,
                                           HashMap<String, String> queryParams,
                                           HashMap<String, String> headers,
                                           String jsonPayload)
                {

                      request= RestAssured.given();
                      request.baseUri(APIConstants.apiBaseUrl);
                      switch(methodType)
                      {
                              case GET:
                                      if(queryParams!=null)
                                      response=request.when().headers(headers).queryParams(queryParams).get(url);
                                      else
                                              response=request.when().headers(headers).get(url);
                                      break;
                              case POST:
                                      if(headers!=null)
                                      response=request.when().headers(headers).body(jsonPayload).post(url);
                                      else
                                      response=request.when().body(jsonPayload).post(url);
                                      break;
                              case PATCH:
                                      response=request.when().headers(headers).body(jsonPayload).patch(url);
                                      break;
                              case PUT:
                                      response=request.when().headers(headers).body(jsonPayload).put(url);
                                      break;
                              case DELETE:
                                      response=request.when().headers(headers).delete(url);
                                      break;
                              default:
                                      throw new RuntimeException("Method :" + methodType + " is not found");
                      }
                        return response.getStatusCode();

                }


        }