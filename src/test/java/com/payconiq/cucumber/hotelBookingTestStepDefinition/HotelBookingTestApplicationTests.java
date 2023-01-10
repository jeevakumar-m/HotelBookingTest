package com.payconiq.cucumber.hotelBookingTestStepDefinition;


import com.payconiq.cucumber.Runner.TestRunner;
import com.payconiq.cucumber.model.request.BookingDate;
import com.payconiq.cucumber.model.request.CreateBookingRequest;
import com.payconiq.cucumber.model.request.PartialUpdateBookingRequest;
import com.payconiq.cucumber.model.response.GetBookingResponse;
import com.payconiq.cucumber.util.Logger.LoggerFile;
import com.payconiq.cucumber.util.api.APIConstants;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.*;

public class HotelBookingTestApplicationTests extends AbstractBaseTestStep
{
    public String bookingId;
    public CreateBookingRequest bookingRequest;
    public CreateBookingRequest partialUpdateBookingRequest;

    @Before
    public void init(Scenario scenario)
    {
        headers.clear();
        queryParams.clear();
        headers.put(APIConstants.header_ContentType,APIConstants.header_ApplicationJson);
        timestamp= Instant.now().toString();
        actualStatusCode=0;
        LoggerFile.log("Scenario Id :" + scenario.getId());
        LoggerFile.log("Scenario name :" + scenario.getName());
        LoggerFile.log("Precondition : Init restassured parameters and headers");

    }
    @After
    public void deleteCreatedBookingsForTest()
    {
        LoggerFile.log("Postcondition : Deleting existing Bookings created during Test");
        headers.clear();
        headers.put(APIConstants.header_ContentType,APIConstants.header_ApplicationJson);
        headers.put(APIConstants.header_Cookie, getService.getAuthService().createToken());
        for(Map.Entry<String,String> createdBookingId: bookingInfo.entrySet())
        {
            getService.getBookingService().deleteBooking(headers,createdBookingId.getKey());
            LoggerFile.log("Deleted Booking Id : " + createdBookingId.getKey());
        }
    }
    @When("user pings api end point")
    public void user_pings_api_end_point() {
        actualStatusCode=getService.getPingService().doHealthCheck(headers);
        LoggerFile.log("Pinged Health Check Url , Status = " + actualStatusCode);
    }
    @Then("verify status code {int} is received")
    public void verify_status_code_is_received(Integer expectedStatusCode) {
        Assert.assertEquals(actualStatusCode==expectedStatusCode,true);
        LoggerFile.log("Assertion of actual status code and expected status code" +
                "actual status code=" + actualStatusCode + " , expected status code" + expectedStatusCode);
    }
    @Given("user is authorised to access api")
    public void user_is_authorised_to_access_api() {
        headers.put(APIConstants.header_Cookie, getService.getAuthService().createToken());
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
        actualStatusCode=getService.getBookingService().deleteBooking(headers,bookingId);
    }
    public void user_deletes_the_created_booking_idById(String myBookingId) {
        actualStatusCode=getService.getBookingService().deleteBooking(headers,myBookingId);
    }
    @Then("the created booking does not exist")
    public void the_created_booking_does_not_exist() {
        Assert.assertTrue(404==getService.getBookingService().getBooking(headers,bookingId));
    }
    @When("user gets the booking information")
    public void user_gets_the_booking_information() {
        actualStatusCode=getService.getBookingService().getBooking(headers,bookingId);

    }
    @Then("verify retrieved booking information is as expected")
    public void verify_retrieved_booking_information_is_as_expected() {
        // Write code here that turns the phrase above into concrete actions

    }
    @Given("user has created {string} hotel bookings")
    public void user_has_created_multiple_hotel_bookings(String count) {
        if(count.equals("multiple"))
        {
            for(int i=1;i<=5;i++)
            userPostsRequestWith(
                    "Payconiq_user_firstname_"+timestamp +"_"+i,
                    "Payconiq_user_lastname_"+timestamp +"_"+i,
                    String.valueOf(12250+i),
                    String.valueOf(true),
                  "Today#"+i,
                    "Today#"+(i+1),
                    "breakfast");
        }
        else {
            for (int i = 0; i < Integer.valueOf(count); i++) {
                user_creates_booking_request_with_valid_information();
            }
        }
    }
    @When("user gets multiple booking information")
    public void user_gets_multiple_booking_information() {
        actualStatusCode=getService.getBookingService().getBookings(headers,null);
    }
    @When("user gets multiple booking information filtered by {string}")
    public void user_gets_multiple_booking_information_byFilter(String filterCriteria)
    {
        HashMap<String,String> queryParams = new HashMap<>();
        for(String fieldValue:filterCriteria.split(";")) {
            String[] fieldText = fieldValue.split("=");
            if (fieldText[0].contains(APIConstants.field_firstname) || fieldText[0].contains(APIConstants.field_lastname)) {
                queryParams.put(fieldText[0], fieldText[1].replace("<timestamp>", timestamp));
            }
            if(fieldText[0].contains(APIConstants.field_checkindate) || fieldText[0].contains(APIConstants.field_checkoutdate))
            {
                queryParams.put(fieldText[0],getDate(fieldText[1]));
            }
        }
        actualStatusCode=getService.getBookingService().getBookings(headers,queryParams);
    }
    @Then("verify {string} booking ids are retrieved")
    public void verify_multiple_booking_ids_are_retrieved(String records) {
        List<GetBookingResponse> getBookingResponse= getService.getBookingService().getBookingsResponse();
        if(records.equals(APIConstants.records_multiple))
        {
            Assert.assertTrue(getBookingResponse.size() > 0);
        }
        else
        {
            Assert.assertEquals(getBookingResponse.size(), (int) Integer.parseInt(records));
            Assert.assertTrue(bookingInfo.containsKey(getBookingResponse.get(0).bookingid));
        }
    }
    @When("user posts request with {string},{string},{string},{string},{string},{string},{string}")
    public void userPostsRequestWith(String firstName, String lastName,
                                     String totalPrice, String depositPaid, String checkInDates,
                                     String checkOutDates, String addon) {
        bookingRequest= new CreateBookingRequest();
        bookingRequest.firstname=firstName;
        bookingRequest.lastname=lastName;
        bookingRequest.totalprice=totalPrice;
        bookingRequest.depositpaid=depositPaid;
        bookingRequest.bookingdates= new BookingDate();
        bookingRequest.bookingdates.checkin=getDate(checkInDates);
        bookingRequest.bookingdates.checkout=getDate(checkOutDates);
        bookingRequest.additionalneeds=addon;
        actualStatusCode=getService.getBookingService().createBooking(headers,bookingRequest);
        bookingId=getService.getBookingService().getCreateBookingResponse().bookingid;
        bookingInfo.put(bookingId,getService.getBookingService().getCreateBookingResponsePayload());
    }
    @When("user updates request with {string},{string},{string},{string},{string},{string},{string}")
    public void userUpdatesRequestWith(String firstName, String lastName,
                                     String totalPrice, String depositPaid, String checkInDates,
                                     String checkOutDates, String addon) {
        bookingRequest= new CreateBookingRequest();
        bookingRequest.firstname=firstName;
        bookingRequest.lastname=lastName;
        bookingRequest.totalprice=totalPrice;
        bookingRequest.depositpaid=depositPaid;
        bookingRequest.bookingdates= new BookingDate();
        bookingRequest.bookingdates.checkin=getDate(checkInDates);
        bookingRequest.bookingdates.checkout=getDate(checkOutDates);
        bookingRequest.additionalneeds=addon;

        actualStatusCode=getService.getBookingService().updateBooking(headers,
                             bookingRequest,
                             bookingId);
    }
    @Then("verify the create booking response is as expected")
    public void verifyCreateBookingResponseIsAsExpected() {
       Assert.assertTrue(bookingRequest.equals(getService.getBookingService().getCreateBookingResponse().booking));
    }
    @Then("verify the update booking response is as expected")
    public void  verifyUpdateBookingResponseIsAsExpected() {
        Assert.assertTrue(bookingRequest.equals(getService.getBookingService().getUpdateBookingResponse()));
    }
    @When("user partially updates request with {string}")
    public void userPartiallyUpdatesRequestWithFirstNameLastName(String fieldValue) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        partialUpdateBookingRequest= new CreateBookingRequest();

        Class<?> concreteClass1 = Class.forName(partialUpdateBookingRequest.getClass().getName());
        Class<?> concreteClass2 = Class.forName(bookingRequest.getClass().getName());
        Field field1=null;
        Field field2=null;
        for(String eachField:fieldValue.split(";")) {
            if(!eachField.split("=")[0].contains("bookingdates.check")) {
                field1 = concreteClass1.getField(eachField.split("=")[0]);
                field2 = concreteClass2.getField(eachField.split("=")[0]);
            }

            if(eachField.split("=")[1].contains("Empty")) {
                field1.set(partialUpdateBookingRequest,"" );
                field2.set(bookingRequest,"" );
            }
            else if(eachField.split("=")[1].contains("space")) {
                field1.set(partialUpdateBookingRequest," " );
                field2.set(bookingRequest," " );
            }
            else if(eachField.split("=")[0].contains("bookingdates.checkin")){
                partialUpdateBookingRequest.bookingdates=partialUpdateBookingRequest.bookingdates==null?new BookingDate():partialUpdateBookingRequest.bookingdates;
                partialUpdateBookingRequest.bookingdates.checkin=getDate(eachField.split("=")[1]);
                bookingRequest.bookingdates.checkin=partialUpdateBookingRequest.bookingdates.checkin;
            }
            else if(eachField.split("=")[0].contains("bookingdates.checkout")){
                partialUpdateBookingRequest.bookingdates=partialUpdateBookingRequest.bookingdates==null?new BookingDate():partialUpdateBookingRequest.bookingdates;
                partialUpdateBookingRequest.bookingdates.checkout=getDate(eachField.split("=")[1]);
                bookingRequest.bookingdates.checkout=partialUpdateBookingRequest.bookingdates.checkout;
            }
            else
            {
                field1.set(partialUpdateBookingRequest,eachField.split("=")[1]);
                field2.set(bookingRequest,eachField.split("=")[1]);
            }
        }
        actualStatusCode=getService.getBookingService().partialUpdateBooking(headers,
                partialUpdateBookingRequest,
                bookingId);
    }
    @And("verify the partial update booking response is as expected for {string}")
    public void verifyThePartialUpdateBookingResponseIsAsExpected(String fieldValue) {
        Assert.assertTrue(bookingRequest.equals(getService.getBookingService().partialUpdateBookingResponse()));

    }
    @And("verify the Get booking response is as expected")
    public void verifyTheGetBookingResponseIsAsExpected() {
        CreateBookingRequest GetbookingResponse= getService.getBookingService().getBookingResponse();
        Assert.assertTrue(bookingRequest.equals(GetbookingResponse));
    }
    @And("verify records that are greater than or equal to {string} are retrieved")
    public void verifyRecordsThatAreGreaterThanOrEqualToAreRetrieved(String filterCriteria) {
        List<GetBookingResponse> getBookingResponse=getService.getBookingService().getBookingsResponse();
        System.out.println(getBookingResponse.size() + " records found");
        for(GetBookingResponse gbr:getBookingResponse) {
            Assert.assertEquals(200, getService.getBookingService().getBooking(headers, gbr.bookingid));
            if (filterCriteria.contains(APIConstants.field_checkindate)) {
              String checkinDate=getService.getBookingService().getBookingResponse().bookingdates.checkin;
                Assert.assertTrue(isDate1AfterDate2(checkinDate,getDate(filterCriteria.split("=")[1])));
            } else if (filterCriteria.contains(APIConstants.field_checkoutdate)) {
                String checkoutDate=getService.getBookingService().getBookingResponse().bookingdates.checkout;
                Assert.assertTrue(isDate1AfterDate2(checkoutDate,getDate(filterCriteria.split("=")[1])));
            }
        }





    }
}
