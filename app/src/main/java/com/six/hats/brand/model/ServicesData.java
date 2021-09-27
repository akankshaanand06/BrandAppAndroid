package com.six.hats.brand.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Keep
public class ServicesData implements Serializable {

    @SerializedName("serviceId")
    @Expose
    private String id;
    @SerializedName("serviceName")
    @Expose
    private String serviceName;
    @SerializedName("branchId")
    @Expose
    private String fkCentreId;
    @SerializedName("serviceCost")
    @Expose
    private String cost;
    @SerializedName("serviceDuration")
    @Expose
    private String duration;
    @SerializedName("servicePriority")
    @Expose
    private String priority;
    @SerializedName("mappedStaff")
    @Expose
    private List<String> serviceStaffs = new ArrayList<>();
    @SerializedName("last")
    @Expose
    private Boolean isLast;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("shortDesp")
    @Expose
    private String shortDesp;
    @SerializedName("servImgUrl")
    @Expose
    private String servImgUrl;

    /*  @SerializedName("status")
      @Expose
      private String status;

      @SerializedName("fkServiceSubCatId")
      @Expose
      private String fkServiceSubCatId;
  */

    public String getServImgUrl() {
        return servImgUrl;
    }

    public void setServImgUrl(String servImgUrl) {
        this.servImgUrl = servImgUrl;
    }

    private Boolean isSelected = false;

    public String getShortDesp() {
        return shortDesp;
    }

    public void setShortDesp(String shortDesp) {
        this.shortDesp = shortDesp;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getFkCentreId() {
        return fkCentreId;
    }

    public void setFkCentreId(String fkCentreId) {
        this.fkCentreId = fkCentreId;
    }

    public List<String> getServiceStaffs() {
        return serviceStaffs;
    }

    public void setServiceStaffs(List<String> serviceStaffs) {
        this.serviceStaffs = serviceStaffs;
    }

    public Boolean getLast() {
        return isLast;
    }

    public void setLast(Boolean last) {
        isLast = last;
    }
}
