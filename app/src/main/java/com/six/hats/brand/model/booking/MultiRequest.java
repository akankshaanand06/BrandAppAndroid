package com.six.hats.brand.model.booking;

import androidx.annotation.Keep;

@Keep
public class MultiRequest {

    private int currentPersonNumber;
    private int totalPerson;
    private Boolean isMultiRequest;


    public int getCurrentPersonNumber() {
        return currentPersonNumber;
    }

    public void setCurrentPersonNumber(int currentPersonNumber) {
        this.currentPersonNumber = currentPersonNumber;
    }

    public int getTotalPerson() {
        return totalPerson;
    }

    public void setTotalPerson(int totalPerson) {
        this.totalPerson = totalPerson;
    }

    public Boolean getIsMultiRequest() {
        return isMultiRequest;
    }

    public void setIsMultiRequest(Boolean isMultiRequest) {
        this.isMultiRequest = isMultiRequest;
    }
}
