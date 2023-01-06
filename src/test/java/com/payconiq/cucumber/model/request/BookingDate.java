package com.payconiq.cucumber.model.request;

public class BookingDate extends BaseModel
        {
            public String checkin;
            public String checkout;

            public boolean equals(BookingDate bookingDate)
            {
                boolean flag=isEqual(this.checkin,bookingDate.checkin,"checkin");
                flag&=isEqual(this.checkout,bookingDate.checkout,"checkout");

                return flag;
            }
        }