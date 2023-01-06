package com.payconiq.cucumber.model.request;

public class BaseModel {
    protected boolean isEqual(String value1, String value2, String validationName)
    {
        System.out.println("Value1 : " + value1 + ", Value2 : " + value2 + " [" + validationName + "]");
        return value1.equals(value2);
    }
}
