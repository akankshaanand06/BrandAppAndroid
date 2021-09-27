package com.six.hats.brand.model.booking;

import androidx.annotation.Keep;


import java.util.ArrayList;
import java.util.List;

@Keep
public class StaffSpecQATRequest {

    private MultiRequest multiRequest = new MultiRequest();
    private List<SearchQatRequest> searchQATForStaffRequests = new ArrayList<>();
    private Boolean newBooking;
    private String bookingMedium;
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

    public String getBookingMedium() {
        return bookingMedium;
    }

    public void setBookingMedium(String bookingMedium) {
        this.bookingMedium = bookingMedium;
    }

    public CustomerDetails getCustomerRequest() {
        return customerRequest;
    }

    public void setCustomerRequest(CustomerDetails customerRequest) {
        this.customerRequest = customerRequest;
    }

    public Boolean getNewBooking() {
        return newBooking;
    }

    public void setNewBooking(Boolean newBooking) {
        this.newBooking = newBooking;
    }

    public MultiRequest getMultiRequest() {
        return multiRequest;
    }

    public void setMultiRequest(MultiRequest multiRequest) {
        this.multiRequest = multiRequest;
    }

    public List<SearchQatRequest> getSearchQATForStaffRequests() {
        return searchQATForStaffRequests;
    }

    public void setSearchQATForStaffRequests(List<SearchQatRequest> searchQATForStaffRequests) {
        this.searchQATForStaffRequests = searchQATForStaffRequests;
    }


}
