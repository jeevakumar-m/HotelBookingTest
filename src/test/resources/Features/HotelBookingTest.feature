Feature: HotelBooking

  @HealthCheck
  Scenario: User checks application is up and running successfully
    When user pings api end point
    Then status code 201 is received

  @CreateBooking
  Scenario Outline: User creates a new hotel booking "<firstName>","<lastName>"
    When user posts request with "<firstName>","<lastName>","<totalPrice>","<depositPaid>","<checkInDates>","<checkOutDates>","<addon>"
    Then status code 200 is received
    And create booking response is as expected
    Examples:
      | firstName | lastName | totalPrice | depositPaid | checkInDates | checkOutDates | addon     |
      | TestFN    | TestLN   | 1000       | true        | Today#5      | Today#10      | Breakfast |

  @UpdateBooking
  Scenario Outline: User updates an existing hotel booking "<firstName>","<lastName>"
    Given user creates booking request with valid information
    And user is authorised to access api
    When user updates request with "<firstName>","<lastName>","<totalPrice>","<depositPaid>","<checkInDates>","<checkOutDates>","<addon>"
    Then status code 200 is received
    And update booking response is as expected
    Examples:
      | firstName | lastName | totalPrice | depositPaid | checkInDates | checkOutDates | addon     |
      | TestFN    | TestLN   | 1000       | true        | Today#30     | Today#35      | Breakfast |

  @PartialUpdateBooking @ProjectRequirement
  Scenario Outline: User partially update an existing hotel booking with "<fieldValue>"
    Given user creates booking request with valid information
    And user is authorised to access api
    When user partially updates request with "<fieldValue>"
    Then status code <expectedCode> is received
    And partial update booking response is as expected for "<fieldValue>"
    Examples:
      | fieldValue                                                   | expectedCode |
      | firstname=TestFN;lastname=space                              | 200          |
      | additionalneeds=lunch                                        | 200          |
      | firstname=中国人;lastname=नीदरलैंड                              | 200          |
      | bookingdates.checkin=Today#19;bookingdates.checkout=Today#20 | 200          |
      | firstname=Empty;lastname=Empty                               | 200          |
      | bookingdates.checkin=Today#20;bookingdates.checkout=Today#19 | 400          |

  @PartialUpdateBookingNegative @ProjectRequirement
  Scenario: User cannot partially update an existing hotel booking without credentials
    Given user creates booking request with valid information
    When user partially updates request with "firstname=TestFN;lastname=se"
    Then status code 403 is received

  @DeleteBooking @ProjectRequirement
  Scenario: User deletes booking with delete api
    Given user creates booking request with valid information
    And user is authorised to access api
    When user deletes the created booking id
    Then status code 201 is received
    And created booking does not exist

  @DeleteBooking @ProjectRequirement
  Scenario: User cannot delete booking with invalid credentials
    Given user creates booking request with valid information
    When user deletes the created booking id
    Then status code 403 is received

  @GetBookingInformation @ProjectRequirement
  Scenario: User retrieves single booking information with GetBooking service
    Given user creates booking request with valid information
    When user gets the booking information
    Then status code 200 is received
    And Get booking response is as expected

  @GetBookingsInformationWithoutFilter @ProjectRequirement
  Scenario: User retrieves multiple booking information with GetBookings service without filter
    Given user creates "20" hotel bookings
    When user gets multiple booking information
    Then status code 200 is received
    And "multiple" booking ids are retrieved

  @GetBookingsInformationWithFilterFirstNameAndLastName @ProjectRequirement
  Scenario Outline: User retrieves multiple booking information with GetBookings service "<filterCriteria>"
    Given user creates "multiple" hotel bookings
    When user gets multiple booking information filtered by "<filterCriteria>"
    Then status code 200 is received
    And "<recordCount>" booking ids are retrieved
    Examples:
      | filterCriteria                                  | recordCount |
      | firstname=Payconiq_user_firstname_<timestamp>_1 | 1           |
      | lastname=Payconiq_user_lastname_<timestamp>_3   | 1           |

  @GetBookingsInformationWithCheckInAndCheckOutDate @ProjectRequirement
  Scenario Outline: User retrieves multiple booking information with GetBookings service for checkin and checkout "<filterCriteria>"
    Given user creates "multiple" hotel bookings
    When user gets multiple booking information filtered by "<filterCriteria>"
    Then status code 200 is received
    And records that are greater than or equal to "<filterCriteria>" are retrieved
    Examples:
      | filterCriteria   |
      | checkin=Today#1  |
      | checkout=Today#2 |