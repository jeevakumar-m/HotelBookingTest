package com.payconiq.cucumber.Service;

import com.google.gson.Gson;
import com.payconiq.cucumber.model.request.Booking;
import com.payconiq.cucumber.model.response.BookingResponse;
import com.payconiq.cucumber.model.response.BookingsResponse;
import com.payconiq.cucumber.util.Logger.LoggerFile;
import com.payconiq.cucumber.util.api.Constants;
import com.payconiq.cucumber.util.api.ServiceUtil;
import io.restassured.http.Method;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BookingService extends ServiceUtil {

    private String bookingURL = Constants.apiBookingUrl;
    private String bookingURLWithId = Constants.apiBookingUrl + "/%s";

    public int getBooking(Map<String, String> headers, String bookingId) {
        LoggerFile.log("--------------------Get Booking Request--------------------");
        return execute(String.format(bookingURLWithId, bookingId),
                Method.GET, null, headers, null);
    }

    public Booking getBookingResponse() {
        return new Gson().fromJson(response.getBody().prettyPrint(), Booking.class);
    }


    public int getBookings(Map<String, String> headers,
                           Map<String, String> queryParams) {
        LoggerFile.log("--------------------Get Bookings Request--------------------");
        return execute(bookingURL,
                Method.GET, queryParams, headers, null);
    }

    public List<BookingsResponse> getBookingsResponse() {
        return Arrays.asList(response.getBody().as(BookingsResponse[].class));
    }

    public int createBookingRequest(Map<String, String> headers,
                                    Booking createBookingPayload) {
        LoggerFile.log("--------------------Create Booking Request--------------------");
        return execute(bookingURL,
                Method.POST, null, headers, new Gson().toJson(createBookingPayload));
    }

    public BookingResponse createBookingResponse() {
        return new Gson().fromJson(response.getBody().prettyPrint(), BookingResponse.class);
    }

    public String createBookingResponsePayload() {
        return response.getBody().prettyPrint().toString();
    }

    public int updateBooking(Map<String, String> headers,
                             Booking updateBookingRequestPayload,
                             String bookingId) {
        LoggerFile.log("--------------------Update Booking Request--------------------");
        return execute(String.format(bookingURLWithId, bookingId),
                Method.PUT, null, headers,
                new Gson().toJson(updateBookingRequestPayload));
    }

    public Booking updateBookingResponse() {
        return new Gson().fromJson(response.getBody().prettyPrint(), Booking.class);
    }

    public int partialUpdateBooking(Map<String, String> headers,
                                    Booking partialUpdateBookingRequest,
                                    String bookingId) {
        LoggerFile.log("--------------------Partial Update Booking Request--------------------");
        return execute(String.format(bookingURLWithId, bookingId),
                Method.PATCH, null,
                headers, new Gson().toJson(partialUpdateBookingRequest));
    }

    public Booking partialUpdateBookingResponse() {
        return new Gson().fromJson(response.getBody().prettyPrint(), Booking.class);
    }

    public int deleteBooking(Map<String, String> headers, String bookingId) {
        LoggerFile.log("--------------------Delete Booking Request--------------------");
        return execute(String.format(bookingURLWithId, bookingId),
                Method.DELETE, null, headers, null);
    }
}
