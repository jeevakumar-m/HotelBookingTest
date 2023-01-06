package com.payconiq.cucumber.hotelBookingTestStepDefinition;

import com.google.gson.Gson;
import com.payconiq.cucumber.RestUtil.APIConstants;
import com.payconiq.cucumber.RestUtil.RestServices;
import com.payconiq.cucumber.model.request.BookingDate;
import com.payconiq.cucumber.model.request.CreateBookingRequest;
import com.payconiq.cucumber.model.request.PartialUpdateBookingRequest;
import com.payconiq.cucumber.model.response.CreateBookingResponse;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.Method;
import org.junit.Assert;


import java.lang.reflect.Parameter;
import java.util.HashMap;

public class HotelBookingTestApplicationTests
{
    RestServices apiService= new RestServices();
    HashMap<String,String> headers= new HashMap<>();
    HashMap<String,String> queryParams=new HashMap<>();
    HashMap<String,String> bookingInfo= new HashMap<>();
    public String bookingId;
    CreateBookingRequest bookingRequest;
    PartialUpdateBookingRequest partialUpdateBookingRequest;

    @Before
    public void init()
    {
        headers.clear();
        queryParams.clear();
        headers.put(APIConstants.header_ContentType,APIConstants.header_ApplicationJson);
    }

    @After
    public void destruct()
    {

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
    public void user_posts_request_with_valid_information() {
        userPostsRequestWith("TestFirst","TestLast","250",
                "false","2022-01-01","2022-05-01","Lunch");

    }
    @When("user deletes the created booking id")
    public void user_deletes_the_created_booking_id() {
         apiService.execute(APIConstants.apiBaseUrl+APIConstants.apiBookingUrl+"/"+ bookingId,
                 Method.DELETE,null,headers,null);

    }

    @Then("the created booking does not exist")
    public void the_created_booking_does_not_exist() {


    }


    @Given("user has created a new hotel booking")
    public void user_has_created_a_new_hotel_booking() {
        // Write code here that turns the phrase above into concrete actions

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

    @Given("user has created multiple hotel bookings")
    public void user_has_created_multiple_hotel_bookings() {
        // Write code here that turns the phrase above into concrete actions

    }
    @When("user gets multiple booking information")
    public void user_gets_multiple_booking_information() {
        // Write code here that turns the phrase above into concrete actions

    }

    @Then("verify multiple booking ids are retrieved")
    public void verify_multiple_booking_ids_are_retrieved() {
        // Write code here that turns the phrase above into concrete actions

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
