package com.six.hats.brand.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Keep
public class Appointment implements Serializable {

    @SerializedName("advance")
    @Expose
    private Boolean advance;
    @SerializedName("appointmentId")
    @Expose
    private String appointmentId;
    @SerializedName("bookingMode")
    @Expose
    private String bookingMode;
    @SerializedName("bookingType")
    @Expose
    private String bookingType;
    @SerializedName("passCode")
    @Expose
    private String passCode;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("userType")
    @Expose
    private String userType;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("userPhoneNumber")
    @Expose
    private String userPhoneNumber;

    @SerializedName("subCategory")
    @Expose
    private String subCategory;
    @SerializedName("mainCategory")
    @Expose
    private String mainCategory;
    @SerializedName("appontmentBaseEnitities")
    @Expose
    private List<AppontmentEnitities> appontmentEnitities = new ArrayList<>();

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getAdvance() {
        return advance;
    }

    public void setAdvance(Boolean advance) {
        this.advance = advance;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getBookingMode() {
        return bookingMode;
    }

    public void setBookingMode(String bookingMode) {
        this.bookingMode = bookingMode;
    }

    public String getPassCode() {
        return passCode;
    }

    public void setPassCode(String passCode) {
        this.passCode = passCode;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public List<AppontmentEnitities> getAppontmentEnitities() {
        return appontmentEnitities;
    }

    public void setAppontmentEnitities(List<AppontmentEnitities> appontmentEnitities) {
        this.appontmentEnitities = appontmentEnitities;
    }


}
