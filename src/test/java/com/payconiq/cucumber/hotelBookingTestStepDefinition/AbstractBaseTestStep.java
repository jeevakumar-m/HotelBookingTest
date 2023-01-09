package com.payconiq.cucumber.hotelBookingTestStepDefinition;

import com.payconiq.cucumber.util.api.APIManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public abstract class AbstractBaseTestStep {

    public APIManager getService= new APIManager();
    public int actualStatusCode=0;
    public  HashMap<String,String> headers= new HashMap<>();
    public HashMap<String,String> queryParams=new HashMap<>();
    public HashMap<String,String> bookingInfo= new HashMap<>();
    public String timestamp;


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

    public boolean isDate1AfterDate2(String Date1,String Date2)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        boolean flag=false;
        try {
            Date date1=sdf.parse(Date1);
            Date date2=sdf.parse(Date2);
            if(date1.after(date2) || date1.equals(date2))
            {
                flag=true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return flag;


    }
}
