package com.payconiq.cucumber.model.request;

public class Booking extends BaseModel {
    private String firstname;
    private String lastname;
    private String totalprice;
    private String depositpaid;
    private BookingDate bookingdates;
    private String additionalneeds;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(String totalprice) {
        this.totalprice = totalprice;
    }

    public String getDepositpaid() {
        return depositpaid;
    }

    public void setDepositpaid(String depositpaid) {
        this.depositpaid = depositpaid;
    }

    public BookingDate getBookingdates() {
        return bookingdates;
    }

    public void setBookingdates(BookingDate bookingdates) {
        this.bookingdates = bookingdates;
    }

    public String getAdditionalneeds() {
        return additionalneeds;
    }

    public void setAdditionalneeds(String additionalneeds) {
        this.additionalneeds = additionalneeds;
    }

    public boolean equals(Booking bookingRequest) {
        boolean flag = true;
        flag &= isEqual(this.firstname, bookingRequest.firstname, "firstname");
        flag &= isEqual(this.lastname, bookingRequest.lastname, "lastname");
        flag &= isEqual(this.totalprice, bookingRequest.totalprice, "totalprice");
        flag &= isEqual(this.depositpaid, bookingRequest.depositpaid, "depositpaid");
        flag &= this.bookingdates.equals(bookingRequest.bookingdates);
        flag &= isEqual(this.additionalneeds, bookingRequest.additionalneeds, "additionalneeds");

        return flag;
    }
}