package com.payconiq.cucumber.model.response;

import com.payconiq.cucumber.model.request.BaseModel;
import com.payconiq.cucumber.model.request.Booking;


public class BookingResponse extends BaseModel {
    private String bookingid;
    private Booking booking;

    public String getBookingid() {
        return bookingid;
    }

    public void setBookingid(String bookingid) {
        this.bookingid = bookingid;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}