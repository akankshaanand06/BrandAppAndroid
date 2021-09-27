package com.six.hats.brand.model.booking;



import com.six.hats.brand.model.ServicesData;
import com.six.hats.brand.model.StaffDetails;

import java.io.Serializable;
import java.util.List;

public class StaffDisplayResponse implements Serializable {
    private StaffDetails branchStaff;
    private List<ServicesData> branchServiceList;
    private String apptCount;


    public StaffDisplayResponse() {
    }

    public StaffDetails getBranchStaff() {
        return branchStaff;
    }

    public void setBranchStaff(StaffDetails branchStaff) {
        this.branchStaff = branchStaff;
    }

    public List<ServicesData> getBranchServiceList() {
        return branchServiceList;
    }

    public void setBranchServiceList(List<ServicesData> branchServiceList) {
        this.branchServiceList = branchServiceList;
    }

    public String getApptCount() {
        return apptCount;
    }

    public void setApptCount(String apptCount) {
        this.apptCount = apptCount;
    }
}
