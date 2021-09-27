package com.six.hats.brand.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.six.hats.brand.model.booking.Categories;

import java.util.ArrayList;
import java.util.List;

@Keep
public class CndDataCategoryItem {

    @SerializedName("businessStaticData")
    @Expose
    private List<Categories> businessStaticData = new ArrayList<>();

    public List<Categories> getBusinessStaticData() {
        return businessStaticData;
    }

    public void setBusinessStaticData(List<Categories> businessStaticData) {
        this.businessStaticData = businessStaticData;
    }
}
