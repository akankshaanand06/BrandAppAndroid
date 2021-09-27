package com.six.hats.brand.model.booking;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class Categories {
  /*  @SerializedName("id")
    @Expose
    private String catID;
    @SerializedName("name")
    @Expose
    private String catName;
    @SerializedName("cndType")
    @Expose
    private String cndType;
    @SerializedName("priority")
    @Expose
    private String priority;
    @SerializedName("parent")
    @Expose
    private String parent;*/

    @SerializedName("businessId")
    @Expose
    private String cndId;
    @SerializedName("businessName")
    @Expose
    private String cndName;
    @SerializedName("businessIconUrl")
    @Expose
    private String businessIconUrl;

    private Boolean isSelected = false;

    public String getBusinessIconUrl() {
        return businessIconUrl;
    }

    public void setBusinessIconUrl(String businessIconUrl) {
        this.businessIconUrl = businessIconUrl;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getCndId() {
        return cndId;
    }

    public void setCndId(String cndId) {
        this.cndId = cndId;
    }

    public String getCndName() {
        return cndName;
    }

    public void setCndName(String cndName) {
        this.cndName = cndName;
    }
}

