package com.payconiq.cucumber.Service;

import com.google.gson.Gson;
import com.payconiq.cucumber.model.request.CreateBookingRequest;
import com.payconiq.cucumber.model.request.PartialUpdateBookingRequest;
import com.payconiq.cucumber.model.response.CreateBookingResponse;
import com.payconiq.cucumber.model.response.GetBookingResponse;
import com.payconiq.cucumber.util.api.APIConstants;
import com.payconiq.cucumber.util.api.APIUtils;
import io.restassured.http.Method;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Booking extends APIUtils {

    private  String bookingURL= APIConstants.apiBookingUrl;
    private  String bookingURLWithId=APIConstants.apiBookingUrl+"/%s";

    public int getBooking(HashMap<String,String> headers,String bookingId)
    {
        return execute(String.format(bookingURLWithId,bookingId),
                Method.GET,null,headers,null);
    }

    public CreateBookingRequest getBookingResponse()
    {
       return new Gson().fromJson(response.getBody().prettyPrint(), CreateBookingRequest.class);
    }



    public int getBookings(HashMap<String,String> headers,
                            HashMap<String,String> queryParams)
    {
        return execute(bookingURL,
                Method.GET,queryParams,headers,null);
    }

    public List<GetBookingResponse> getBookingsResponse()
    {
        return Arrays.asList(response.getBody().as(GetBookingResponse[].class));
    }

    public int createBooking(HashMap<String,String> headers,
                                     CreateBookingRequest createBookingPayload)
    {

        return execute(bookingURL,
                Method.POST,null,headers,new Gson().toJson(createBookingPayload));
    }

    public CreateBookingResponse getCreateBookingResponse()
    {
        return new Gson().fromJson(response.getBody().prettyPrint(), CreateBookingResponse.class);
    }

    public String getCreateBookingResponsePayload()
    {
        return response.getBody().prettyPrint().toString();
    }

    public int updateBooking(HashMap<String,String> headers,
                              CreateBookingRequest updateBookingRequestPayload,
                              String bookingId)
    {
        return execute(String.format(bookingURLWithId,bookingId),
                Method.PUT,null,headers,
                new Gson().toJson(updateBookingRequestPayload));
    }

    public CreateBookingRequest getUpdateBookingResponse()
    {
        return new Gson().fromJson(response.getBody().prettyPrint(), CreateBookingRequest.class);
    }

    public int partialUpdateBooking(HashMap<String,String> headers,
                                     PartialUpdateBookingRequest partialUpdateBookingRequest,
                                     String bookingId)
    {
        return execute(String.format(bookingURLWithId,bookingId),
                Method.PATCH,null,
                headers,new Gson().toJson(partialUpdateBookingRequest));
    }

    public CreateBookingRequest partialUpdateBookingResponse()
    {
        return new Gson().fromJson(response.getBody().prettyPrint(), CreateBookingRequest.class);
    }

    public int deleteBooking(HashMap<String,String> headers,String bookingId)
    {
        return execute(String.format(bookingURLWithId,bookingId),
                Method.DELETE,null,headers,null);
    }
}
