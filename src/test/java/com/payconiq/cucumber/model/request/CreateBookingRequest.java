package com.payconiq.cucumber.model.request;

public class CreateBookingRequest extends BaseModel
        {
            public String firstname;
            public String lastname;
            public String totalprice;
            public String depositpaid;
            public BookingDate bookingdates;
            public String additionalneeds;

            public boolean equals(CreateBookingRequest bookingRequest)
            {
                boolean flag=true;
                flag &=isEqual(this.firstname,bookingRequest.firstname,"firstname");
                flag &=isEqual(this.lastname,bookingRequest.lastname,"lastname");
                flag &=isEqual(this.totalprice,bookingRequest.totalprice,"totalprice");
                flag &=isEqual(this.depositpaid,bookingRequest.depositpaid,"depositpaid");
                flag &=this.bookingdates.equals(bookingRequest.bookingdates);
                flag &=isEqual(this.additionalneeds,bookingRequest.additionalneeds,"additionalneeds");

                return flag;
            }
        }