package com.six.hats.brand.model.booking;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Keep
public class StaffSpecificResponse {

    @SerializedName("success")
    @Expose
    private String status;
    @SerializedName("staffServiceResponses")
    @Expose
    private List<Data> staffServiceResponses = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Data> getStaffServiceResponses() {
        return staffServiceResponses;
    }

    public void setStaffServiceResponses(List<Data> staffServiceResponses) {
        this.staffServiceResponses = staffServiceResponses;
    }
    @Keep
    public class Data {

        @SerializedName("serviceId")
        @Expose
        private String serviceId;

        @SerializedName("staffResponseList")
        @Expose
        private List<StaffList> staffList = new ArrayList<>();

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public List<StaffList> getStaffList() {
            return staffList;
        }

        public void setStaffList(List<StaffList> staffList) {
            this.staffList = staffList;
        }
    }
}
