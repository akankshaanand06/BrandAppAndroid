package com.six.hats.brand.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Keep
public class StaffApiResponse implements Serializable {


    @SerializedName("branchStaffList")
    @Expose
    private List<StaffDetails> branchStaffList = new ArrayList<>();

    public List<StaffDetails> getBranchStaffList() {
        return branchStaffList;
    }

    public void setBranchStaffList(List<StaffDetails> branchStaffList) {
        this.branchStaffList = branchStaffList;
    }
}

