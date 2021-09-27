package com.six.hats.brand.model.booking;

import androidx.annotation.Keep;

@Keep
public class MultiRequest {

    private int currentPersonNumber;
    private int totalPerson;
    private Boolean multiRequest;


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

    public Boolean getMultiRequest() {
        return multiRequest;
    }

    public void setMultiRequest(Boolean multiRequest) {
        this.multiRequest = multiRequest;
    }
}
