package com.six.hats.brand.model.booking;

import androidx.annotation.Keep;


import java.util.ArrayList;
import java.util.List;

@Keep
public class StaffSpecQATRequest {

    private MultiRequest multiPersonRequestCounter = new MultiRequest();
    private List<SearchQatRequest> staffWiseQATReqList = new ArrayList<>();
    private Boolean isNewBooking;
    /**
     * Contains Values ONSITE and ONLINE
     */
    private String bkngMode;
    private CustomerDetails customerRequest = new CustomerDetails();
    private String mainCategory;
    private String subCategory;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getBkngMode() {
        return bkngMode;
    }

    public void setBkngMode(String bkngMode) {
        this.bkngMode = bkngMode;
    }

    public CustomerDetails getCustomerRequest() {
        return customerRequest;
    }

    public void setCustomerRequest(CustomerDetails customerRequest) {
        this.customerRequest = customerRequest;
    }

    public Boolean getIsNewBooking() {
        return isNewBooking;
    }

    public void setIsNewBooking(Boolean isNewBooking) {
        this.isNewBooking = isNewBooking;
    }

    public MultiRequest getMultiPersonRequestCounter() {
        return multiPersonRequestCounter;
    }

    public void setMultiPersonRequestCounter(MultiRequest multiPersonRequestCounter) {
        this.multiPersonRequestCounter = multiPersonRequestCounter;
    }

    public List<SearchQatRequest> getStaffWiseQATReqList() {
        return staffWiseQATReqList;
    }

    public void setStaffWiseQATReqList(List<SearchQatRequest> staffWiseQATReqList) {
        this.staffWiseQATReqList = staffWiseQATReqList;
    }


}
