package com.six.hats.brand.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Keep
public class CampaignItems {

    @SerializedName("branchId")
    @Expose
    private String branchId;
    @SerializedName("branchCampaginResponse")
    @Expose
    private CampaignResponse branchCampaginResponse = new CampaignResponse();

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public CampaignResponse getBranchCampaginResponse() {
        return branchCampaginResponse;
    }

    public void setBranchCampaginResponse(CampaignResponse branchCampaginResponse) {
        this.branchCampaginResponse = branchCampaginResponse;
    }

    @Keep
    public class CampaignResponse {
        @SerializedName("branchCampaginList")
        @Expose
        private List<Campaign> campaignArrayList = new ArrayList<>();

        public List<Campaign> getCampaignArrayList() {
            return campaignArrayList;
        }

        public void setCampaignArrayList(List<Campaign> campaignArrayList) {
            this.campaignArrayList = campaignArrayList;
        }
    }

    @Keep
    public class Campaign {
        @SerializedName("branchId")
        @Expose
        private String branchId;
        @SerializedName("campaginType")
        @Expose
        private String campaignType;
        @SerializedName("endDate")
        @Expose
        private String endDate;
        @SerializedName("branchName")
        @Expose
        private String branchName;
        @SerializedName("fontStyle")
        @Expose
        private String fontStyle;
        @SerializedName("heading")
        @Expose
        private String heading;
        @SerializedName("imageURLLink")
        @Expose
        private String imageURLLink;
        @SerializedName("language")
        @Expose
        private String language;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("startDate")
        @Expose
        private String startDate;

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public String getBranchId() {
            return branchId;
        }

        public void setBranchId(String branchId) {
            this.branchId = branchId;
        }

        public String getCampaignType() {
            return campaignType;
        }

        public void setCampaignType(String campaignType) {
            this.campaignType = campaignType;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getFontStyle() {
            return fontStyle;
        }

        public void setFontStyle(String fontStyle) {
            this.fontStyle = fontStyle;
        }

        public String getHeading() {
            return heading;
        }

        public void setHeading(String heading) {
            this.heading = heading;
        }

        public String getImageURLLink() {
            return imageURLLink;
        }

        public void setImageURLLink(String imageURLLink) {
            this.imageURLLink = imageURLLink;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }
    }
}
