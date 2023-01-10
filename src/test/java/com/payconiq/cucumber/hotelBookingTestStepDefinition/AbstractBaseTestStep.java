package com.payconiq.cucumber.hotelBookingTestStepDefinition;

import com.payconiq.cucumber.Service.BookingService;
import com.payconiq.cucumber.model.request.Booking;
import com.payconiq.cucumber.model.request.BookingDate;
import com.payconiq.cucumber.util.api.ServiceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBaseTestStep {

    public ServiceManager apiService = new ServiceManager();
    public int actualStatusCode = 0;
    public Map<String, String> headers = new HashMap<>();
    public Map<String, String> queryParams = new HashMap<>();
    public Map<String, String> bookingInfo = new HashMap<>();
    public String timestamp;
    public String bookingId;
    public Booking bookingRequest;
    public Booking partialUpdateBookingRequest;


    public String getDate(String dateToBeCalculated) {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String thisDate = dateToBeCalculated.toLowerCase().contains("today") ? Instant.now().toString() : dateToBeCalculated.split("#")[1];
        final java.util.Calendar cal = GregorianCalendar.getInstance();
        try {

            cal.setTime(sdf.parse(thisDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.add(GregorianCalendar.DATE, Integer.valueOf(dateToBeCalculated.split("#")[1]));
        return sdf.format(cal.getTime());
    }

    public boolean isDate1AfterDate2(String Date1, String Date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        boolean flag = false;
        try {
            Date date1 = sdf.parse(Date1);
            Date date2 = sdf.parse(Date2);
            if (date1.after(date2) || date1.equals(date2)) {
                flag = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return flag;


    }

    protected void createBookingPayload(String firstName, String lastName,
                                        String totalPrice, String depositPaid, String checkInDates,
                                        String checkOutDates, String addon) {
        bookingRequest = new Booking();

        bookingRequest.setFirstname(firstName);
        bookingRequest.setLastname(lastName);
        bookingRequest.setTotalprice(totalPrice);
        bookingRequest.setDepositpaid(depositPaid);
        bookingRequest.setAdditionalneeds(addon);
        BookingDate bookingDate = new BookingDate();
        bookingDate.setCheckin(getDate(checkInDates));
        bookingDate.setCheckout(getDate(checkOutDates));
        bookingRequest.setBookingdates(bookingDate);


    }
}
