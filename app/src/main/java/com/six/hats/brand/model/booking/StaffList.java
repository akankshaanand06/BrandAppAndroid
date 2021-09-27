package com.six.hats.brand.model.booking;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class StaffList {
    @SerializedName("staffId")
    @Expose
    private String staffId;
    @SerializedName("staffName")
    @Expose
    private String staffName;
    @SerializedName("staffPic")
    @Expose
    private String staffPic;
    private Boolean isselected = false;

    public String getStaffPic() {
        return staffPic;
    }

    public void setStaffPic(String staffPic) {
        this.staffPic = staffPic;
    }

    public Boolean getIsselected() {
        return isselected;
    }

    public void setIsselected(Boolean isselected) {
        this.isselected = isselected;
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
}