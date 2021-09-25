package com.six.hats.brand.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CentreSingleton {

    private boolean isEmergencyActive;
    private String branchId;

    private String businessName;

    private String businessId;

    private String adminId;

    private String managerId;

    private List<String> staffId = new ArrayList<>();

    private String subCategory;

    private String category;

    private String location;

    private String cordinates;

    private String address;

    private String pinCode;

    private String branchRating;

    private String experience;

    private int numberOfStaff;

    private int maxAppointment;

    private int noOfUploadedImages;

    private List<ImageItem> imageList = new ArrayList<>();

    private String qrPath;

    private String branchQrId;

    private String reasutrantTables;

    private List<BusinessHours> businessHours;

    private boolean completed;

    private boolean homeService;

    private Boolean isFav = false;


    private static CentreSingleton sInstance = null;

    public synchronized static CentreSingleton getInstance() {
        if (sInstance == null) {
            sInstance = new CentreSingleton();
        }
        return sInstance;
    }


    private CentreSingleton() {

    }


    public boolean isEmergencyActive() {
        return isEmergencyActive;
    }

    public void setEmergencyActive(boolean emergencyActive) {
        isEmergencyActive = emergencyActive;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public List<String> getStaffId() {
        return staffId;
    }

    public void setStaffId(List<String> staffId) {
        this.staffId = staffId;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCordinates() {
        return cordinates;
    }

    public void setCordinates(String cordinates) {
        this.cordinates = cordinates;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getBranchRating() {
        return branchRating;
    }

    public void setBranchRating(String branchRating) {
        this.branchRating = branchRating;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public int getNumberOfStaff() {
        return numberOfStaff;
    }

    public void setNumberOfStaff(int numberOfStaff) {
        this.numberOfStaff = numberOfStaff;
    }

    public int getMaxAppointment() {
        return maxAppointment;
    }

    public void setMaxAppointment(int maxAppointment) {
        this.maxAppointment = maxAppointment;
    }

    public int getNoOfUploadedImages() {
        return noOfUploadedImages;
    }

    public void setNoOfUploadedImages(int noOfUploadedImages) {
        this.noOfUploadedImages = noOfUploadedImages;
    }

    public List<ImageItem> getImageList() {
        return imageList;
    }

    public void setImageList(List<ImageItem> imageList) {
        this.imageList = imageList;
    }

    public String getQrPath() {
        return qrPath;
    }

    public void setQrPath(String qrPath) {
        this.qrPath = qrPath;
    }

    public String getBranchQrId() {
        return branchQrId;
    }

    public void setBranchQrId(String branchQrId) {
        this.branchQrId = branchQrId;
    }

    public String getReasutrantTables() {
        return reasutrantTables;
    }

    public void setReasutrantTables(String reasutrantTables) {
        this.reasutrantTables = reasutrantTables;
    }

    public List<BusinessHours> getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(List<BusinessHours> businessHours) {
        this.businessHours = businessHours;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isHomeService() {
        return homeService;
    }

    public void setHomeService(boolean homeService) {
        this.homeService = homeService;
    }

    public Boolean getFav() {
        return isFav;
    }

    public void setFav(Boolean fav) {
        isFav = fav;
    }


}
