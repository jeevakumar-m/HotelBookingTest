package com.payconiq.cucumber.hotelBookingTestStepDefinition;

import com.google.gson.Gson;
import com.payconiq.cucumber.RestUtil.APIConstants;
import com.payconiq.cucumber.RestUtil.RestServices;
import com.payconiq.cucumber.model.request.BookingDate;
import com.payconiq.cucumber.model.request.CreateBookingRequest;
import com.payconiq.cucumber.model.request.PartialUpdateBookingRequest;
import com.payconiq.cucumber.model.response.CreateBookingResponse;
import com.payconiq.cucumber.model.response.GetBookingResponse;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.Method;
import org.junit.Assert;


import java.lang.reflect.Parameter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

public class HotelBookingTestApplicationTests
{
    RestServices apiService= new RestServices();
    HashMap<String,String> headers= new HashMap<>();
    HashMap<String,String> queryParams=new HashMap<>();
    HashMap<String,String> bookingInfo= new HashMap<>();
    public String bookingId;
    CreateBookingRequest bookingRequest;
    PartialUpdateBookingRequest partialUpdateBookingRequest;
    String timestamp;

    @Before
    public void init()
    {
        headers.clear();
        queryParams.clear();
        headers.put(APIConstants.header_ContentType,APIConstants.header_ApplicationJson);
        timestamp=Instant.now().toString();
    }

    @After
    public void deleteCreatedBookingsForTest()
    {
        System.out.println("Deleting existing Bookings created during Test");
        for(Map.Entry<String,String> createdBookingId: bookingInfo.entrySet())
        {
            user_deletes_the_created_booking_idById(createdBookingId.getKey());
        }
    }

    public String getDate(String dateToBeCalculated)
    {
        final SimpleDateFormat sdf=new SimpleDateFormat( "yyyy-MM-dd" );
        String thisDate=dateToBeCalculated.toLowerCase().contains("today")? Instant.now().toString():dateToBeCalculated.split("#")[1];
        final java.util.Calendar cal = GregorianCalendar.getInstance();
        try {

            cal.setTime( sdf.parse(thisDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.add( GregorianCalendar.DATE,Integer.valueOf(dateToBeCalculated.split("#")[1]));
        return sdf.format( cal.getTime() );
    }



    @When("user pings api end point")
    public void user_pings_api_end_point() {

          apiService.execute(APIConstants.apiHealthCheckUrl,Method.GET,null,
                            headers,null);
    }
    @Then("verify status code {int} is received")
    public void verify_status_code_is_received(Integer expectedStatusCode) {
        Assert.assertEquals(apiService.isStatusCodeAsExpected(expectedStatusCode),true);
    }


    @Given("user is authorised to access api")
    public void user_is_authorised_to_access_api() {
        headers.put(APIConstants.header_Cookie,apiService.getToken());
    }

    @Given("user creates booking request with valid information")
    public void user_creates_booking_request_with_valid_information() {

        userPostsRequestWith("Payconiq_First_" + new Random().nextInt(1000,10000),
                "Payconiq_Last_TestLast" + new Random().nextInt(1000,10000),
                String.valueOf(new Random().nextInt(1000,10000)),
                "true",
                    "2022-01-01",
                "2022-05-01",
                "Lunch");

    }



    @When("user deletes the created booking id")
    public void user_deletes_the_created_booking_id() {
         apiService.execute(APIConstants.apiBaseUrl+APIConstants.apiBookingUrl+"/"+ bookingId,
                 Method.DELETE,null,headers,null);

    }

    public void user_deletes_the_created_booking_idById(String myBookingId) {
        apiService.execute(APIConstants.apiBaseUrl+APIConstants.apiBookingUrl+"/"+ myBookingId,
                Method.DELETE,null,headers,null);


    }

    @Then("the created booking does not exist")
    public void the_created_booking_does_not_exist() {


    }


    @When("user gets the booking information")
    public void user_gets_the_booking_information() {
        apiService.execute(APIConstants.apiBaseUrl+APIConstants.apiBookingUrl+"/"+bookingId,
                Method.GET,null,headers,null);

    }

    @Then("verify retrieved booking information is as expected")
    public void verify_retrieved_booking_information_is_as_expected() {
        // Write code here that turns the phrase above into concrete actions

    }

    @Given("user has created {string} hotel bookings")
    public void user_has_created_multiple_hotel_bookings(String count) {
        if(count.equals("multiple"))
        {
            for(int i=0;i<5;i++)
            userPostsRequestWith(
                    "Payconiq_user_firstname_"+timestamp +"_"+i,
                    "Payconiq_user_lastname_"+timestamp +"_"+i,
                    String.valueOf(12250+i),
                    String.valueOf(true),
                    getDate("Today#"+i),
                    getDate("Today#"+(i+1)),
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
        apiService.execute(APIConstants.apiBaseUrl+APIConstants.apiBookingUrl,
                Method.GET,null,headers,null);

    }

    @When("user gets multiple booking information filtered by {string}")
    public void user_gets_multiple_booking_information_byFilter(String filterCriteria)
    {
        HashMap<String,String> queryParam = new HashMap<>();
        for(String fieldValue:filterCriteria.split(";")) {
            queryParam.put(fieldValue.split("=")[0],fieldValue.split("=")[1].replace("<timestamp>",timestamp));
        }
        apiService.execute(APIConstants.apiBaseUrl+APIConstants.apiBookingUrl,
                Method.GET,queryParam,headers,null);
    }

    @Then("verify {string} booking ids are retrieved")
    public void verify_multiple_booking_ids_are_retrieved(String records) {
        List<GetBookingResponse> getBookingResponse= Arrays.asList(apiService.getResponse().getBody().as(GetBookingResponse[].class));
        if(records.equals("multiple"))
        Assert.assertTrue(getBookingResponse.size()>0);
        else
        Assert.assertTrue(getBookingResponse.size()==Integer.valueOf(records));
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
        bookingRequest.bookingdates.checkin=checkInDates;
        bookingRequest.bookingdates.checkout=checkOutDates;
        bookingRequest.additionalneeds=addon;

        apiService.execute(APIConstants.apiBookingUrl,
                Method.POST,null,headers,new Gson().toJson(bookingRequest));
        bookingId=new Gson().fromJson(apiService.getResponse().getBody().prettyPrint(), CreateBookingResponse.class).bookingid;
        bookingInfo.put(bookingId,apiService.getResponse().getBody().prettyPrint());
        System.out.println(bookingInfo);
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
        bookingRequest.bookingdates.checkin=checkInDates;
        bookingRequest.bookingdates.checkout=checkOutDates;
        bookingRequest.additionalneeds=addon;

        apiService.execute(APIConstants.apiBookingUrl+"/"+ bookingId,
                Method.PUT,null,headers,new Gson().toJson(bookingRequest));
    }

    @Then("verify the create booking response is as expected")
    public void verifyCreateBookingResponseIsAsExpected() {
        CreateBookingResponse bookingResponse= apiService.getResponse().getBody().as(CreateBookingResponse.class); //new Gson().fromJson(apiService.getResponse().getBody().prettyPrint(), CreateBookingResponse.class);
        Assert.assertTrue(bookingRequest.equals(bookingResponse.booking));
    }

    @Then("verify the update booking response is as expected")
    public void  verifyUpdateBookingResponseIsAsExpected() {
        CreateBookingRequest bookingResponse= apiService.getResponse().getBody().as(CreateBookingRequest.class);
        Assert.assertTrue(bookingRequest.equals(bookingResponse));
    }

    @When("user partially updates request with {string},{string}")
    public void userPartiallyUpdatesRequestWithFirstNameLastName(String firstName,String lastName) {
        partialUpdateBookingRequest= new PartialUpdateBookingRequest();
        partialUpdateBookingRequest.firstname=firstName;
        partialUpdateBookingRequest.lastname=lastName;

        apiService.execute(APIConstants.apiBaseUrl+APIConstants.apiBookingUrl+"/"+ bookingId,
                Method.PATCH,null,headers,new Gson().toJson(partialUpdateBookingRequest));
    }

    @And("verify the partial update booking response is as expected")
    public void verifyThePartialUpdateBookingResponseIsAsExpected() {
        CreateBookingRequest bookingResponse= apiService.getResponse().getBody().as(CreateBookingRequest.class);
        Assert.assertTrue(partialUpdateBookingRequest.firstname.equals(bookingResponse.firstname));
        Assert.assertTrue(partialUpdateBookingRequest.lastname.equals(bookingResponse.lastname));
    }

    @And("verify the Get booking response is as expected")
    public void verifyTheGetBookingResponseIsAsExpected() {
        CreateBookingRequest GetbookingResponse=apiService.getResponse().getBody().as(CreateBookingRequest.class);
        Assert.assertTrue(bookingRequest.equals(GetbookingResponse));
    }
}
