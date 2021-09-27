package com.six.hats.brand.model.booking;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.six.hats.brand.model.TimeSpan;

import java.util.ArrayList;
import java.util.List;

@Keep
public class QATResponseList {
    @SerializedName("qatResponseList")
    @Expose
    private QATList qatResponseList = new QATList();

    @SerializedName("serviceId")
    @Expose
    private List<String> serviceId = new ArrayList<>();

    private String idleDiff = "-1";

    public String getIdleDiff() {
        return idleDiff;
    }

    public void setIdleDiff(String idleDiff) {
        this.idleDiff = idleDiff;
    }

    public QATList getQatResponseList() {
        return qatResponseList;
    }

    public void setQatResponseList(QATList qatResponseList) {
        this.qatResponseList = qatResponseList;
    }

    public List<String> getServiceId() {
        return serviceId;
    }

    public void setServiceId(List<String> serviceId) {
        this.serviceId = serviceId;
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
