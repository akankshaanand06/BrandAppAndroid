package com.six.hats.brand.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Keep
public class AppontmentEnitities implements Serializable {
    @SerializedName("appointmentBaseEntityId")
    @Expose
    private String appointmentBaseEntityId;
    @SerializedName("appointmentStatus")
    @Expose
    private String appointmentStatus;
    @SerializedName("bookingSlot")
    @Expose
    private BookingSlot bookingSlot = new BookingSlot();
    @SerializedName("bookingTime")
    @Expose
    private String bookingTime;
    @SerializedName("branchId")
    @Expose
    private String branchId;
    @SerializedName("branchName")
    @Expose
    private String branchName;
    @SerializedName("serviceAmount")
    @Expose
    private String serviceAmount;
    @SerializedName("serviceIdList")
    @Expose
    private List<String> serviceIdList = new ArrayList<>();
    @SerializedName("serviceModelList")
    @Expose
    private List<ServicesData> serviceModelList = new ArrayList<>();
    @SerializedName("staffId")
    @Expose
    private String staffId;
    @SerializedName("staffName")
    @Expose
    private String staffName;
    @SerializedName("totalServiceTime")
    @Expose
    private String totalServiceTime;

    public String getAppointmentBaseEntityId() {
        return appointmentBaseEntityId;
    }

    public void setAppointmentBaseEntityId(String appointmentBaseEntityId) {
        this.appointmentBaseEntityId = appointmentBaseEntityId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public BookingSlot getBookingSlot() {
        return bookingSlot;
    }

    public void setBookingSlot(BookingSlot bookingSlot) {
        this.bookingSlot = bookingSlot;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getServiceAmount() {
        return serviceAmount;
    }

    public void setServiceAmount(String serviceAmount) {
        this.serviceAmount = serviceAmount;
    }

    public List<String> getServiceIdList() {
        return serviceIdList;
    }

    public void setServiceIdList(List<String> serviceIdList) {
        this.serviceIdList = serviceIdList;
    }

    public List<ServicesData> getServiceModelList() {
        return serviceModelList;
    }

    public void setServiceModelList(List<ServicesData> serviceModelList) {
        this.serviceModelList = serviceModelList;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getTotalServiceTime() {
        return totalServiceTime;
    }

    public void setTotalServiceTime(String totalServiceTime) {
        this.totalServiceTime = totalServiceTime;
    }

}
