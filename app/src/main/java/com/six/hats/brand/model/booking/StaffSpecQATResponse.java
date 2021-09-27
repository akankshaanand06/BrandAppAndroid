package com.six.hats.brand.model.booking;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.six.hats.brand.model.BasicResponse;

import java.util.ArrayList;
import java.util.List;

@Keep
public class StaffSpecQATResponse extends BasicResponse {
    @SerializedName("allBusy")
    @Expose
    private Boolean allBusy;
    @SerializedName("pleaseComeLater")
    @Expose
    private Boolean pleaseComeLater;
    @SerializedName("staffWaitList")
    @Expose
    private List<WaitTime> staffWaitList = new ArrayList<>();
    @SerializedName("staffQATList")
    @Expose
    private List<QATResponseList> staffQATList = new ArrayList<>();


    public Boolean getAllBusy() {
        return allBusy;
    }

    public void setAllBusy(Boolean allBusy) {
        this.allBusy = allBusy;
    }

    public Boolean getPleaseComeLater() {
        return pleaseComeLater;
    }

    public void setPleaseComeLater(Boolean pleaseComeLater) {
        this.pleaseComeLater = pleaseComeLater;
    }

    public List<WaitTime> getStaffWaitList() {
        return staffWaitList;
    }

    public void setStaffWaitList(List<WaitTime> staffWaitList) {
        this.staffWaitList = staffWaitList;
    }

    public List<QATResponseList> getStaffQATList() {
        return staffQATList;
    }

    public void setStaffQATList(List<QATResponseList> staffQATList) {
        this.staffQATList = staffQATList;
    }

    @Keep
    public class WaitTime {

        @SerializedName("staffId")
        @Expose
        private String staffId;

        @SerializedName("expectedWaitTime")
        @Expose
        private ExpectedWaitTime expectedWaitTime = new ExpectedWaitTime();

        public ExpectedWaitTime getExpectedWaitTime() {
            return expectedWaitTime;
        }

        public void setExpectedWaitTime(ExpectedWaitTime expectedWaitTime) {
            this.expectedWaitTime = expectedWaitTime;
        }

        public String getStaffId() {
            return staffId;
        }

        public void setStaffId(String staffId) {
            this.staffId = staffId;
        }

        @Keep
        public class ExpectedWaitTime {
            @SerializedName("staffIdExpectedWaitTime")
            @Expose
            private String staffIdWaitTime;

            @SerializedName("time")
            @Expose
            private int time;

            public int getTime() {
                return time;
            }

            public void setTime(int time) {
                this.time = time;
            }

            public String getStaffIdWaitTime() {
                return staffIdWaitTime;
            }

            public void setStaffIdWaitTime(String staffIdWaitTime) {
                this.staffIdWaitTime = staffIdWaitTime;
            }
        }
    }
}