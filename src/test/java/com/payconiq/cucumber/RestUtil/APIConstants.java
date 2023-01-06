package com.payconiq.cucumber.RestUtil;

import com.payconiq.HotelBookingTest.configExtractor.ConfigParser;

public class APIConstants {
    public final static String apiUserName= ConfigParser.getValueAsString("encoded_name");
    public final static String apiPassWord=ConfigParser.getValueAsString("encoded_password");
    public final static String apiBaseUrl=ConfigParser.getValueAsString("baseurl");
    public final static String apiAuthUrl="auth";
    public final static String apiBookingUrl="booking";
    public final static String apiHealthCheckUrl="ping";
    public final static String header_Accept="Accept";
    public final static String header_ApplicationJson="application/json";
    public final static String header_ContentType="Content-Type";
    public final static String header_Cookie="Cookie";
    public final static String header_Authorization="Authorization";

    public final static String token="token=";
}
