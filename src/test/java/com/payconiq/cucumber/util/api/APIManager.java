package com.payconiq.cucumber.util.api;

import com.payconiq.cucumber.Service.Auth;
import com.payconiq.cucumber.Service.Booking;
import com.payconiq.cucumber.Service.Ping;

public class APIManager {
    Auth auth;
    Booking booking;
    Ping ping;

    public Auth getAuthService()
    {
        return (auth==null)?auth= new Auth():auth;
    }

    public Booking getBookingService()
    {
        return (booking==null)?booking= new Booking():booking;
    }

    public Ping getPingService()
    {
        return (ping==null)?ping= new Ping():ping;
    }




}
