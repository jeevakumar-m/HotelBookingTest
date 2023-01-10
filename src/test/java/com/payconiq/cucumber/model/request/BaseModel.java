package com.payconiq.cucumber.model.request;

import com.payconiq.cucumber.util.Logger.LoggerFile;

public class BaseModel {
    protected boolean isEqual(String value1, String value2, String validationName) {
        LoggerFile.log(" [" + validationName + "] : " + "Value1 : " + value1 + ", Value2 : " + value2);
        return value1.equals(value2);
    }
}
