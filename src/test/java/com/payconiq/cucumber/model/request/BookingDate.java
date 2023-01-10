package com.payconiq.cucumber.model.request;

public class BookingDate extends BaseModel {
    private String checkin;
    private String checkout;

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public boolean equals(BookingDate bookingDate) {
        boolean flag = isEqual(this.checkin, bookingDate.checkin, "checkin");
        flag &= isEqual(this.checkout, bookingDate.checkout, "checkout");
        return flag;
    }
}