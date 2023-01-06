package com.payconiq.cucumber.RestUtil;

import com.google.gson.Gson;
import com.payconiq.cucumber.model.request.AuthTokenRequest;
import com.payconiq.cucumber.model.response.AuthTokenResponse;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Base64;
import java.util.HashMap;

public class RestServices
        {
                private RequestSpecification request;
                private Response response;
                private String token=null;

                public String getToken()
                {
                        HashMap<String,String> headers= new HashMap<>();
                        headers.put(APIConstants.header_ContentType,APIConstants.header_ApplicationJson);
                        if(token==null) {
                                AuthTokenRequest authTokenRequest = new AuthTokenRequest();
                                authTokenRequest.username = new String(Base64.getDecoder().decode(APIConstants.apiUserName));
                                authTokenRequest.password = new String (Base64.getDecoder().decode(APIConstants.apiPassWord));

                                execute(APIConstants.apiBaseUrl + APIConstants.apiAuthUrl,
                                        Method.POST,
                                        null,
                                        headers,
                                        new Gson().toJson(authTokenRequest)
                                );

                                AuthTokenResponse authTokenResponse = new Gson().fromJson(this.response.getBody().prettyPrint(), AuthTokenResponse.class);
                                token=authTokenResponse.token;
                        }
                        return APIConstants.token+token;
                }

                public void execute(String url,
                                        Method methodType,
                                        HashMap<String,String> queryParams,
                                        HashMap<String,String> headers,
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


                }

                public boolean isStatusCodeAsExpected(int expectedCode)
                {
                        return response.getStatusCode()==expectedCode;
                }

                public Response getResponse()
                {
                        return this.response;
                }

        }