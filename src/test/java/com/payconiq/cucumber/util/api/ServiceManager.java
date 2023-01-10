package com.payconiq.cucumber.util.api;

import com.payconiq.cucumber.Service.AuthService;
import com.payconiq.cucumber.Service.BookingService;
import com.payconiq.cucumber.Service.PingService;

public class ServiceManager {

    private AuthService authService;
    private BookingService bookingService;
    private PingService pingService;

    public AuthService getAuthService() {
        return (authService == null) ? authService = new AuthService() : authService;
    }

    public PingService getPingService() {
        return (pingService == null) ? pingService = new PingService() : pingService;
    }

    public BookingService getBookingService() {
        return (bookingService == null) ? bookingService = new BookingService() : bookingService;
    }


}
