package com.payconiq.cucumber.hotelBookingTestStepDefinition;


import com.payconiq.cucumber.model.request.Booking;
import com.payconiq.cucumber.model.request.BookingDate;
import com.payconiq.cucumber.model.response.BookingsResponse;
import com.payconiq.cucumber.util.Logger.LoggerFile;
import com.payconiq.cucumber.util.api.Constants;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.*;

public class HotelBookingTestApplicationTests extends AbstractBaseTestStep {


    @Before
    public void init(Scenario scenario) {
        headers.clear();
        queryParams.clear();
        headers.put(Constants.header_ContentType, Constants.header_ApplicationJson);
        timestamp = Instant.now().toString();
        actualStatusCode = 0;
        LoggerFile.log("Scenario Id :" + scenario.getId());
        LoggerFile.log("Scenario name :" + scenario.getName());
        LoggerFile.log("Precondition : Init restassured parameters and headers");

    }

    @After
    public void deleteCreatedBookingsForTest() {
        LoggerFile.log("Postcondition : Deleting existing Bookings created during Test");
        headers.clear();
        headers.put(Constants.header_ContentType, Constants.header_ApplicationJson);
        headers.put(Constants.header_Cookie, apiService.getAuthService().createToken());
        for (Map.Entry<String, String> createdBookingId : bookingInfo.entrySet()) {
            apiService.getBookingService().deleteBooking(headers, createdBookingId.getKey());
            LoggerFile.log("Deleted Booking Id : " + createdBookingId.getKey());
        }
    }

    @When("user pings api end point")
    public void user_pings_api_end_point() {
        actualStatusCode = apiService.getPingService().doHealthCheck(headers);
        LoggerFile.log("Pinged Health Check Url , Status = " + actualStatusCode);
    }

    @Then("status code {int} is received")
    public void verify_status_code_is_received(Integer expectedStatusCode) {
        Assert.assertEquals(actualStatusCode == expectedStatusCode, true);
        LoggerFile.log("Assertion of actual status code and expected status code" +
                "actual status code=" + actualStatusCode + " , expected status code" + expectedStatusCode);
    }

    @Given("user is authorised to access api")
    public void user_is_authorised_to_access_api() {
        headers.put(Constants.header_Cookie, apiService.getAuthService().createToken());
    }

    @Given("user creates booking request with valid information")
    public void user_creates_booking_request_with_valid_information() {
        userPostsRequestWith("Payconiq_First_" + new Random().nextInt(1000),
                "Payconiq_Last_TestLast" + new Random().nextInt(1000),
                String.valueOf(new Random().nextInt(1000)),
                "true",
                "Today#30",
                "Today#45",
                "Lunch");

    }

    @When("user deletes the created booking id")
    public void user_deletes_the_created_booking_id() {

        actualStatusCode = apiService.getBookingService().deleteBooking(headers, bookingId);
    }

    public void user_deletes_the_created_booking_idById(String myBookingId) {
        actualStatusCode = apiService.getBookingService().deleteBooking(headers, myBookingId);
    }

    @Then("created booking does not exist")
    public void the_created_booking_does_not_exist() {
        Assert.assertTrue(404 == apiService.getBookingService().getBooking(headers, bookingId));
    }

    @When("user gets the booking information")
    public void user_gets_the_booking_information() {
        actualStatusCode = apiService.getBookingService().getBooking(headers, bookingId);

    }


    @Given("user creates {string} hotel bookings")
    public void user_has_created_multiple_hotel_bookings(String count) {
        if (count.equals("multiple")) {
            for (int i = 1; i <= 5; i++)
                userPostsRequestWith(
                        "Payconiq_user_firstname_" + timestamp + "_" + i,
                        "Payconiq_user_lastname_" + timestamp + "_" + i,
                        String.valueOf(12250 + i),
                        String.valueOf(true),
                        "Today#" + i,
                        "Today#" + (i + 1),
                        "breakfast");
        } else {
            for (int i = 0; i < Integer.valueOf(count); i++) {
                user_creates_booking_request_with_valid_information();
            }
        }
    }

    @When("user gets multiple booking information")
    public void user_gets_multiple_booking_information() {
        actualStatusCode = apiService.getBookingService().getBookings(headers, null);
    }

    @When("user gets multiple booking information filtered by {string}")
    public void user_gets_multiple_booking_information_byFilter(String filterCriteria) {
        HashMap<String, String> queryParams = new HashMap<>();
        for (String fieldValue : filterCriteria.split(";")) {
            String[] fieldText = fieldValue.split("=");
            if (fieldText[0].contains(Constants.field_firstname) || fieldText[0].contains(Constants.field_lastname)) {
                queryParams.put(fieldText[0], fieldText[1].replace("<timestamp>", timestamp));
            }
            if (fieldText[0].contains(Constants.field_checkindate) || fieldText[0].contains(Constants.field_checkoutdate)) {
                queryParams.put(fieldText[0], getDate(fieldText[1]));
            }
        }
        actualStatusCode = apiService.getBookingService().getBookings(headers, queryParams);
    }

    @Then("{string} booking ids are retrieved")
    public void verify_multiple_booking_ids_are_retrieved(String records) {
        List<BookingsResponse> getBookingResponse = apiService.getBookingService().getBookingsResponse();
        if (records.equals(Constants.records_multiple)) {
            Assert.assertTrue(getBookingResponse.size() > 0);
        } else {
            Assert.assertEquals(getBookingResponse.size(), (int) Integer.parseInt(records));
            Assert.assertTrue(bookingInfo.containsKey(getBookingResponse.get(0).getBookingid()));
        }
    }


    @When("user posts request with {string},{string},{string},{string},{string},{string},{string}")
    public void userPostsRequestWith(String firstName, String lastName,
                                     String totalPrice, String depositPaid, String checkInDates,
                                     String checkOutDates, String addon) {

        createBookingPayload(firstName, lastName, totalPrice, depositPaid, checkInDates, checkOutDates, addon);

        actualStatusCode = apiService.getBookingService().createBookingRequest(headers, bookingRequest);
        bookingId = apiService.getBookingService().createBookingResponse().getBookingid();
        bookingInfo.put(bookingId, apiService.getBookingService().createBookingResponsePayload());
    }

    @When("user updates request with {string},{string},{string},{string},{string},{string},{string}")
    public void userUpdatesRequestWith(String firstName, String lastName,
                                       String totalPrice, String depositPaid, String checkInDates,
                                       String checkOutDates, String addon) {
        createBookingPayload(firstName, lastName, totalPrice, depositPaid, checkInDates, checkOutDates, addon);

        actualStatusCode = apiService.getBookingService().updateBooking(headers,
                bookingRequest,
                bookingId);
    }

    @Then("create booking response is as expected")
    public void verifyCreateBookingResponseIsAsExpected() {
        Assert.assertTrue(bookingRequest.equals(apiService.getBookingService().createBookingResponse().getBooking()));
    }

    @Then("update booking response is as expected")
    public void verifyUpdateBookingResponseIsAsExpected() {
        Assert.assertTrue(bookingRequest.equals(apiService.getBookingService().updateBookingResponse()));
    }

    @When("user partially updates request with {string}")
    public void userPartiallyUpdatesRequestWithFirstNameLastName(String fieldValue) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        partialUpdateBookingRequest = new Booking();
        BookingDate bookingDates = new BookingDate();
        Class<?> concreteClass1 = Class.forName(Booking.class.getName());
        Class<?> concreteClass2 = Class.forName(Booking.class.getName());
        Field field1 = null;
        Field field2 = null;
        for (String eachField : fieldValue.split(";")) {
            String key = eachField.split("=")[0];
            String value = eachField.split("=")[1];
            if (!key.contains(Constants.field_bookginDatesCheck)) {
                field1 = concreteClass1.getDeclaredField(key);
                field2 = concreteClass2.getDeclaredField(key);
                field1.setAccessible(true);
                field2.setAccessible(true);
            }
            if (value.contains(Constants.value_empty)) {
                field1.set(partialUpdateBookingRequest, "");
                field2.set(bookingRequest, "");
            } else if (value.contains(Constants.value_space)) {
                field1.set(partialUpdateBookingRequest, " ");
                field2.set(bookingRequest, " ");
            } else if (key.contains(Constants.field_checkindate)) {
                bookingDates.setCheckin(getDate(value));
                partialUpdateBookingRequest.setBookingdates(bookingDates);
                bookingRequest.setBookingdates(bookingDates);
            } else if (key.contains(Constants.field_checkoutdate)) {
                bookingDates.setCheckout(getDate(value));
                partialUpdateBookingRequest.setBookingdates(bookingDates);
                bookingRequest.setBookingdates(bookingDates);
            } else {
                field1.set(partialUpdateBookingRequest, value);
                field2.set(bookingRequest, value);
            }
        }
        actualStatusCode = apiService.getBookingService().partialUpdateBooking
                (headers,partialUpdateBookingRequest,bookingId);
    }

    @And("partial update booking response is as expected for {string}")
    public void verifyThePartialUpdateBookingResponseIsAsExpected(String fieldValue) {
        Assert.assertTrue(bookingRequest.equals(apiService.getBookingService().partialUpdateBookingResponse()));

    }

    @And("Get booking response is as expected")
    public void verifyTheGetBookingResponseIsAsExpected() {
        Booking getBookingResponse = apiService.getBookingService().getBookingResponse();
        Assert.assertTrue(bookingRequest.equals(getBookingResponse));
    }

    @And("records that are greater than or equal to {string} are retrieved")
    public void verifyRecordsThatAreGreaterThanOrEqualToAreRetrieved(String filterCriteria) {
        List<BookingsResponse> getBookingResponse = apiService.getBookingService().getBookingsResponse();
        System.out.println(getBookingResponse.size() + " records found");
        for (BookingsResponse gbr : getBookingResponse) {
            Assert.assertEquals(200, apiService.getBookingService().getBooking(headers, gbr.getBookingid()));
            if (filterCriteria.contains(Constants.field_checkindate)) {
                String checkinDate = apiService.getBookingService().getBookingResponse().getBookingdates().getCheckin();
                Assert.assertTrue(
                        "Checkin date value not greater , Response checkin date : " + checkinDate +
                                "Input checkin date" + getDate(filterCriteria.split("=")[1]),
                        isDate1AfterDate2(checkinDate, getDate(filterCriteria.split("=")[1]))
                        );
            } else if (filterCriteria.contains(Constants.field_checkoutdate)) {
                String checkoutDate = apiService.getBookingService().getBookingResponse().getBookingdates().getCheckout();
                Assert.assertTrue("Checkout date value not greater , Response checkin date : " + checkoutDate +
                        "Input checkin date" + getDate(filterCriteria.split("=")[1]),
                        isDate1AfterDate2(checkoutDate, getDate(filterCriteria.split("=")[1])));
            }
        }


    }
}
