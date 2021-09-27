package com.six.hats.brand.model.booking;


import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Keep
public class ServiceCategoryItem implements Serializable {

    @SerializedName("serviceMainCategories")
    @Expose
    private List<ServicesStaticData> businessStaticData = new ArrayList<>();

    public List<ServicesStaticData> getBusinessStaticData() {
        return businessStaticData;
    }

    public void setBusinessStaticData(List<ServicesStaticData> businessStaticData) {
        this.businessStaticData = businessStaticData;
    }

    @Keep
    public class ServicesStaticData implements Serializable {

        String serviceId;
        String serviceName;
        String servicePriority;

        public ServicesStaticData(String serviceId, String serviceName, String servicePriority) {
            this.serviceId = serviceId;
            this.serviceName = serviceName;
            this.servicePriority = servicePriority;
        }

        public ServicesStaticData() {
        }


        public String getServicePriority() {
            return servicePriority;
        }

        public void setServicePriority(String servicePriority) {
            this.servicePriority = servicePriority;
        }

        public String getServiceId() {
            return serviceId;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }

        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }
    }

}
