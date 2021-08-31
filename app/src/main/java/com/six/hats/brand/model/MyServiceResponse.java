package com.six.hats.brand.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Keep
public class MyServiceResponse {


    @SerializedName("branchServiceList")
    @Expose
    private List<ServicesData> data = new ArrayList<>();

    public List<ServicesData> getData() {
        return data;
    }

    public void setData(List<ServicesData> data) {
        this.data = data;
    }
}
