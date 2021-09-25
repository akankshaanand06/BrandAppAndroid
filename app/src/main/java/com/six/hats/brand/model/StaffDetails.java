package com.six.hats.brand.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@Keep
public class StaffDetails implements Serializable {

    @SerializedName("username")
    @Expose
    private String staffId;
    @SerializedName("salary")
    @Expose
    public String salary;
    @SerializedName("age")
    @Expose
    public String age;
    @SerializedName("education")
    @Expose
    public String education;
    @SerializedName("post")
    @Expose
    public String post;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("staffPhoneNumber")
    @Expose
    public String phone;
    @SerializedName("gender")
    @Expose
    public String gender;
    @SerializedName("working_hours")
    @Expose
    private ArrayList<BusinessHours> working_hours = new ArrayList<>();
    @SerializedName("lunch_hours")
    @Expose
    private ArrayList<BusinessHours> lunch_hours = new ArrayList<>();
    @SerializedName("qrPath")
    @Expose
    private String staffQrPath;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("staffRating")
    @Expose
    private String staffRating;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("visible")
    @Expose
    private Boolean visible;
    @SerializedName("staffEmergencyActive")
    @Expose
    private Boolean isStaffEmergencyActive;
    private String role = "staff";

    private Boolean isSelected = false;

    private Boolean isOnAlarm = false;

    public Boolean getOnAlarm() {
        return isOnAlarm;
    }

    public void setOnAlarm(Boolean onAlarm) {
        isOnAlarm = onAlarm;
    }

    public Boolean getStaffEmergencyActive() {
        return isStaffEmergencyActive;
    }

    public void setStaffEmergencyActive(Boolean staffEmergencyActive) {
        isStaffEmergencyActive = staffEmergencyActive;
    }

    public String getStaffRating() {
        return staffRating;
    }

    public void setStaffRating(String staffRating) {
        this.staffRating = staffRating;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public ArrayList<BusinessHours> getWorking_hours() {
        return working_hours;
    }

    public void setWorking_hours(ArrayList<BusinessHours> working_hours) {
        this.working_hours = working_hours;
    }

    public ArrayList<BusinessHours> getLunch_hours() {
        return lunch_hours;
    }

    public void setLunch_hours(ArrayList<BusinessHours> lunch_hours) {
        this.lunch_hours = lunch_hours;
    }

    public String getStaffQrPath() {
        return staffQrPath;
    }

    public void setStaffQrPath(String staffQrPath) {
        this.staffQrPath = staffQrPath;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
