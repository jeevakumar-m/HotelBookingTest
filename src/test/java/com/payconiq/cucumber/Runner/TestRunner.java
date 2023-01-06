package com.payconiq.cucumber.Runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/"
        ,glue={"com.payconiq.cucumber.hotelBookingTestStepDefinition"},tags="@CreateBooking"
)

public class TestRunner {

}