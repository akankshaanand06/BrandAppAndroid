package com.six.hats.brand.model.booking;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.six.hats.brand.model.BasicResponse;
import com.six.hats.brand.model.TimeSpan;

import java.util.ArrayList;
import java.util.List;

@Keep
public class TimeSpecQATResponse extends BasicResponse {
    @SerializedName("anotherReqstInProgress")
    @Expose
    private Boolean anotherReqstInProgress;
    @SerializedName("noQATsAvailGoToSSBkng")
    @Expose
    private Boolean noQATsAvailGoToSSBkng;
    @SerializedName("expectedWaitTime")
    @Expose
    private List<WaitTime> expectedWaitTime = new ArrayList<>();
    @SerializedName("qatResponseList")
    @Expose
    private List<QATList> qatResponseList = new ArrayList<>();
    @SerializedName("numberOfPersonAhedInQue")
    @Expose
    private String numberOfPersonAhedInQue;

    public String getNumberOfPersonAhedInQue() {
        return numberOfPersonAhedInQue;
    }

    public void setNumberOfPersonAhedInQue(String numberOfPersonAhedInQue) {
        this.numberOfPersonAhedInQue = numberOfPersonAhedInQue;
    }

    public Boolean getNoQATsAvailGoToSSBkng() {
        return noQATsAvailGoToSSBkng;
    }

    public void setNoQATsAvailGoToSSBkng(Boolean noQATsAvailGoToSSBkng) {
        this.noQATsAvailGoToSSBkng = noQATsAvailGoToSSBkng;
    }

    public List<WaitTime> getExpectedWaitTime() {
        return expectedWaitTime;
    }

    public void setExpectedWaitTime(List<WaitTime> expectedWaitTime) {
        this.expectedWaitTime = expectedWaitTime;
    }

    public Boolean getAnotherReqstInProgress() {
        return anotherReqstInProgress;
    }

    public void setAnotherReqstInProgress(Boolean anotherReqstInProgress) {
        this.anotherReqstInProgress = anotherReqstInProgress;
    }

    /*public List<WaitTime> getExpectedWaitTime() {
        return expectedWaitTime;
    }

    public void setExpectedWaitTime(List<WaitTime> expectedWaitTime) {
        this.expectedWaitTime = expectedWaitTime;
    }*/

    public List<QATList> getQatResponseList() {
        return qatResponseList;
    }

    public void setQatResponseList(List<QATList> qatResponseList) {
        this.qatResponseList = qatResponseList;
    }
    @Keep
    public class WaitTime {

        @SerializedName("staffIdExpectedWaitTime")
        @Expose
        private String staffId;

        @SerializedName("time")
        @Expose
        private int time;

        public String getStaffId() {
            return staffId;
        }

        public void setStaffId(String staffId) {
            this.staffId = staffId;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }
    @Keep
    public class QATList {

        @SerializedName("staffIdQATResponse")
        @Expose
        private String staffId;

        @SerializedName("startSpan")
        @Expose
        private TimeSpan startSpan = new TimeSpan();

        @SerializedName("endSpan")
        @Expose
        private TimeSpan endSpan = new TimeSpan();

        @SerializedName("staffName")
        @Expose
        private String staffName;

        @SerializedName("staffPic")
        @Expose
        private String staffPic;

        @SerializedName("tempAppointmentId")
        @Expose
        private String tempAppointmentId;

        private Boolean selected = false;

        public String getStaffPic() {
            return staffPic;
        }

        public void setStaffPic(String staffPic) {
            this.staffPic = staffPic;
        }

        public Boolean getSelected() {
            return selected;
        }

        public void setSelected(Boolean selected) {
            this.selected = selected;
        }

        public String getStaffId() {
            return staffId;
        }

        public void setStaffId(String staffId) {
            this.staffId = staffId;
        }

        public TimeSpan getStartSpan() {
            return startSpan;
        }

        public void setStartSpan(TimeSpan startSpan) {
            this.startSpan = startSpan;
        }

        public TimeSpan getEndSpan() {
            return endSpan;
        }

        public void setEndSpan(TimeSpan endSpan) {
            this.endSpan = endSpan;
        }

        public String getStaffName() {
            return staffName;
        }

        public void setStaffName(String staffName) {
            this.staffName = staffName;
        }

        public String getTempAppointmentId() {
            return tempAppointmentId;
        }

        public void setTempAppointmentId(String tempAppointmentId) {
            this.tempAppointmentId = tempAppointmentId;
        }
    }


}