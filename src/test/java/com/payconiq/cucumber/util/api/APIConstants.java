package com.payconiq.cucumber.util.api;

import com.payconiq.cucumber.util.parser.ConfigParser;

public class APIConstants {
    public final static String apiUserName= ConfigParser.getValueAsString("encoded_name");
    public final static String apiPassWord=ConfigParser.getValueAsString("encoded_password");
    public final static String apiBaseUrl=ConfigParser.getValueAsString("baseurl");
    public final static String apiAuthUrl="/auth";
    public final static String apiBookingUrl="/booking";
    public final static String apiHealthCheckUrl="/ping";
    public final static String header_Accept="Accept";
    public final static String header_ApplicationJson="application/json";
    public final static String header_ContentType="Content-Type";
    public final static String header_Cookie="Cookie";
    public final static String header_Authorization="Authorization";
    public final static String field_firstname="firstname";
    public final static String field_lastname="lastname";
    public final static String field_checkindate="checkin";
    public final static String field_checkoutdate="checkout";
    public final static String records_multiple="multiple";

    public final static String token="token=";
}
