package com.six.hats.brand.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.LinkedHashMap;

@Keep
public class FeedBackResponse {
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("branchId")
    @Expose
    private String branchId;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("customerFeedBackQuestionStringHashMap")
    @Expose
    private LinkedHashMap<String, String> customerFeedBack = new LinkedHashMap<>();

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LinkedHashMap<String, String> getCustomerFeedBack() {
        return customerFeedBack;
    }

    public void setCustomerFeedBack(LinkedHashMap<String, String> customerFeedBack) {
        this.customerFeedBack = customerFeedBack;
    }
}
