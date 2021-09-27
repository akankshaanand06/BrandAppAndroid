package com.six.hats.brand.model.booking;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Keep
public class SubCategoryItems {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private List<Categories> data = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getMessage() {
        return success;
    }

    public void setMessage(Boolean message) {
        this.success = message;
    }

    public List<Categories> getData() {
        return data;
    }

    public void setData(List<Categories> data) {
        this.data = data;
    }
}
