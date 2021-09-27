package com.six.hats.brand.model.booking;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Keep
public class SearchQatRequest implements Serializable {

    private Advance advanceBookingRequest = new Advance();
    private Boolean last;
    private List<String> serviceIdList=new ArrayList<>();
    private int servicePriority;
    private int serviceTime;
    private String staffId;
    private String servicePrice;

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public Boolean getLast() {
        return last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

    public List<String> getServiceId() {
        return serviceIdList;
    }

    public void setServiceId(List<String> serviceId) {
        this.serviceIdList = serviceId;
    }

    public int getServicePriority() {
        return servicePriority;
    }

    public void setServicePriority(int servicePriority) {
        this.servicePriority = servicePriority;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public Advance getAdvanceBookingRequest() {
        return advanceBookingRequest;
    }

    public void setAdvanceBookingRequest(Advance advanceBookingRequest) {
        this.advanceBookingRequest = advanceBookingRequest;
    }


}
