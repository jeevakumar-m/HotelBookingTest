Feature: HotelBooking

  @HealthCheck
  Scenario: verify application is up and running successfully
    When user pings api end point
    Then verify status code 201 is received

  @CreateBooking
  Scenario Outline: verify user can create a new hotel booking
    When user posts request with "<firstName>","<lastName>","<totalPrice>","<depositPaid>","<checkInDates>","<checkOutDates>","<addon>"
    Then verify status code 200 is received
    And verify the received content is as expected
    Examples:
      | firstName | lastName | totalPrice | depositPaid | checkInDates | checkOutDates | addon     |
      | TestFN    | TestLN   | 1000       | true        | 2018-01-01   | 2018-01-01    | Breakfast |

  @UpdateBooking
  Scenario Outline: verify user can edit an existing hotel booking
    Given user is authorised to access api
    And user posts request with valid information
    When user edits request with "<firstName>","<lastName>","<totalPrice>","<depositPaid>","<checkInDates>","<checkOutDates>","<addon>"
    Then verify status code 200 is received
    And verify the received content is as expected
    Examples:
      | firstName | lastName | totalPrice | depositPaid | checkInDates | checkOutDates | addon     |
      | TestFN    | TestLN   | 1000       | 500         | 2018-01-01   | 2018-01-01    | Breakfast |

  @PartialUpdateBooking
  Scenario Outline: verify user can edit partial information for hotel booking
    Given user is authorised to access api
    And user posts request with valid information
    When user patches request with "<firstName>","<lastName>"
    Then verify status code 200 is received
    And verify the received content is as expected
    Examples:
      | firstName | lastName |
      | TestFN    | TestLN   |

  @DeleteBooking
  Scenario: verify Booking can be created with delete api
    Given user is authorised to access api
    And user posts request with valid information
    When user deletes the created booking id
    Then verify status code 200 is received
    And the created booking does not exist

  @GetBookingInformation
  Scenario: verify Booking information can be retrieved with Get Api
    Given user has created a new hotel booking
    When user gets the booking information
    Then verify status code 200 is received
    And verify retrieved booking information is as expected

  @GetBookingsInformation
  Scenario: verify Booking ids are retreived with Get Api
    Given user has created multiple hotel bookings
    When user gets multiple booking information
    Then verify status code 200 is received
    And verify multiple booking ids are retrieved

