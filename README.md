# HotelBookingTest
API Test Automation

Tech Stack :
===============================

1. Programming Language - Java<br />
2. Test Framework - Junit with BDD Cucumber (Gherkin) flavored with RestAssured<br />
3. Build management - Maven<br />
4. Reporting - Extent Report and File Logging<br />
5. Repo Management and VC - Github<br />

Test Objective :
=================

You have a working “createBooking” and “getBookingById” endpoints, make sure that
“partialUpdateBooking , “deleteBooking” and “getBookingIds”(test get all bookingIds
without filter, and add test for 1-2 parameters) are working. (Write automated tests
that can be used for regression)

Scope and Out of Scope :
=========================

1. APIs to be tested inclusive as part of http://restful-booker.herokuapp.com/apidoc/index.html#api-Booking<br />
2. Any third party validation, db validation currently not exposed are out of scope, Support services like auth and ping.<br />

Test Approach
==============

1.Feature to be validated - Booking<br />
2.Support services - Auth , Ping<br />
3.Service to be validated as part of Booking Feature - PartialUpdateBooking , DeleteBooking , GetBookingIds (With and WithoutFilter)<br />

BDD Cucumber approach Test scenarios have been written for the entire Booking feature focussing primarily on point 3.<br />
Following are the Project requirement tests that can be found in the feature file section with tag as "@ProjectRequirement"<br />

Automation Test Framework has been created to serve the test automtion for regression / functional in a better way with POJO Implementation with Restassured.<br />

Deserialiazation and serialization of classes is done using Google Gson library and restassured library as and when required<br />

Dynamic payload creation for PATCH Api is done using Reflections of class.<br />

Before Test Execution begins hooks is configured to clear the headers and set the headers with basic headers<br/>

After Test execution is completed hooks is configured to delete created booking ids to control the junk data overhead<br/>

Test data management is effectively done in such a way that at any point of time the test suite is independent of test data and can run effectively with auto data creation and deletion<br/>

Framework Folder structure :
================================

1.hotelBookingTestStepDefinitions :
====================================

  a.Test-contains the stepdefinition logic for hotelbooking feature for different services.<br />
  b.hotelBookingTestStepDefinitions inherits from AbstractTestSteps that holds the simplified view of the basic step that are required by all stepdefinition files.<br />

2.model :
====================================

  a.request - contains all the required POJO for requests<br />
  b.response - contains all the required POJO for response<br />

3.TestRunner
====================================

drives the complete test execution which can be controlled using different tags that are used for test execution. this is a junit based testrunner<br />

4.Service Layer
====================================

  This contains the Classes for different services offered by the swagger<br />
    a.Auth service - class responsible for generating token <br />
    b.Booking service - responsible for all booking related operations<br />
    c.Ping service - responsible for all health check functionality<br />
5. API Util holds the constants ,wrapper for all the basic feature functionalities and then Util to create a resuable restassured component that can be used across different tests<br />
6. LoggerFile to log the results<br />
7. parser file to read the config file<br />
8. Properties categorized as application and extent used for app related credentilals and urls and the later for reporting.<br />


Total Test created for the @ProjectRequirement mentioned is 14 Tests as mentioned below ,

Execution Details 
======================

Execution can be started by pulling down the repo and configuring the required tags in the testrunner section , executing maven goals would trigger test execution.<br />

Results :
====================================

Pass - User partially update an existing hotel booking with "firstname=TestFN;lastname=space"<br />
Pass - User partially update an existing hotel booking with "additionalneeds=lunch"<br />
Pass - User partially update an existing hotel booking with "firstname=中国人;lastname=नीदरलैंड"<br />
Pass - User partially update an existing hotel booking with "bookingdates.checkin=Today#19;bookingdates.checkout=Today#20"<br />
Pass - User partially update an existing hotel booking with "firstname=Empty;lastname=Empty"<br />
Pass - User cannot partially update an existing hotel booking without credentials<br />
Pass - User deletes booking with delete api<br />
Pass - User cannot delete booking with invalid credentials<br />
Pass - User retrieves single booking information with GetBooking service<br />
Pass - User retrieves multiple booking information with GetBookings service without filter<br />
Pass - User retrieves multiple booking information with GetBookings service "firstname=Payconiq_user_firstname__1"<br />
Pass - User retrieves multiple booking information with GetBookings service "lastname=Payconiq_user_lastname__3"<br />
Pass - User retrieves multiple booking information with GetBookings service for checkin and checkout "checkin=Today#1"<br />
Fail - User retrieves multiple booking information with GetBookings service for checkin and checkout "checkout=Today#2"<br />

Observations
====================================
1.Checkout scenario retrieves irrelvant data eventhought it has been mentioned that only ids having checkout date greater or equal to filter criteria should be displayed.<br />
2.Eventhough patch api works for partial payload upload it violates the patch rule of payload operation like given below
{

  "op": "type of operation",

  "path": "path of resource",

  "value": "field value"

}<br />
3. For Delete API , status code returned is 201 (Created) , as per the standards one of the following adds meaning to operation (200,202,204) instead of 201.<br />
4. Get Bookings api returns nothing when multiple parameters are passed as query parameter. Though there are valid data with combination it returns null.<br />
5. when invalid query param is passed for getbookings api it returns all valid result which is incorrect.<br />



