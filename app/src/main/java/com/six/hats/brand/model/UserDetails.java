package com.six.hats.brand.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Keep
public class UserDetails implements Serializable {


    @SerializedName("imeiNumber")
    @Expose
    private String imeiNumber;
    @SerializedName("otp")//
    @Expose
    private String otp;
    @SerializedName("name")//
    @Expose
    private String name;
    @SerializedName("surname")//
    @Expose
    private String surname;
    @SerializedName("username")//
    @Expose
    private String username;
    @SerializedName("userPhoneNo")//
    @Expose
    private String userPhoneNo;
    @SerializedName("age")//
    @Expose
    private String age;
    @SerializedName("gender")//
    @Expose
    private String gender;
    @SerializedName("mail")//
    @Expose
    private String mail;
    @SerializedName("profession")//
    @Expose
    private String profession;
    @SerializedName("country")//
    @Expose
    private String country;
    @SerializedName("city")//
    @Expose
    private String city;
    @SerializedName("photo")//
    @Expose
    private String photo;
    @SerializedName("area")//
    @Expose
    private String area;
    @SerializedName("education")//
    @Expose
    private String education;
    @SerializedName("income")//
    @Expose
    private String income;
    @SerializedName("bloodGroup")//
    @Expose
    private String bloodGroup;
    @SerializedName("otpverfied")
    @Expose
    private Boolean otpverfied;

    @SerializedName("favoriteBranchList")
    @Expose
    private List<FavoriteBranchList> favoriteBranchList = new ArrayList<>();

    public List<FavoriteBranchList> getFavoriteBranchList() {
        return favoriteBranchList;
    }

    public void setFavoriteBranchList(List<FavoriteBranchList> favoriteBranchList) {
        this.favoriteBranchList = favoriteBranchList;
    }

    public String getImeiNumber() {
        return imeiNumber;
    }

    public void setImeiNumber(String imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

    public String getUserPhoneNo() {
        return userPhoneNo;
    }

    public void setUserPhoneNo(String userPhoneNo) {
        this.userPhoneNo = userPhoneNo;
    }


    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public Boolean getOtpverfied() {
        return otpverfied;
    }

    public void setOtpverfied(Boolean otpverfied) {
        this.otpverfied = otpverfied;
    }

    @Keep
    public class FavoriteBranchList implements Serializable {
        @SerializedName("branchId")
        @Expose
        private String branchId;
        @SerializedName("subCategory")
        @Expose
        private String subCategory;

        public String getBranchId() {
            return branchId;
        }

        public void setBranchId(String branchId) {
            this.branchId = branchId;
        }

        public String getSubCategory() {
            return subCategory;
        }

        public void setSubCategory(String subCategory) {
            this.subCategory = subCategory;
        }
    }
}
