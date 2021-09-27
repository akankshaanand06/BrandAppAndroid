package com.six.hats.brand.model.booking;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.six.hats.brand.model.BookingSlot;

import java.io.Serializable;

@Keep
public class LiveBookingResponse implements Serializable {
    @SerializedName("delayOnIt")
    @Expose
    private String delayOnIt;
    @SerializedName("advance")
    @Expose
    private Boolean advance;
    @SerializedName("peopleAheadCount")
    @Expose
    private String peopleAheadCount;
    @SerializedName("appointment")
    @Expose
    private Appointment appointment;
    @SerializedName("currentSlot")
    @Expose
    private BookingSlot currentSlot = new BookingSlot();

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Boolean getAdvance() {
        return advance;
    }

    public void setAdvance(Boolean advance) {
        this.advance = advance;
    }

    public String getDelayOnIt() {
        return delayOnIt;
    }

    public void setDelayOnIt(String delayOnIt) {
        this.delayOnIt = delayOnIt;
    }

    public String getPeopleAheadCount() {
        return peopleAheadCount;
    }

    public void setPeopleAheadCount(String peopleAheadCount) {
        this.peopleAheadCount = peopleAheadCount;
    }

    public BookingSlot getCurrentSlot() {
        return currentSlot;
    }

    public void setCurrentSlot(BookingSlot currentSlot) {
        this.currentSlot = currentSlot;
    }
}
