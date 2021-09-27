package com.six.hats.brand.model.booking;

import androidx.annotation.Keep;

@Keep
public class ServicesItems {
    public String serviceName;
    public String serviceETS;
    public String serviceRate;
    public Boolean isSelected = false;


    public ServicesItems(String service_name, String service_dur, String service_rate, Boolean isSelected) {

        this.serviceName = service_name;
        this.serviceETS = service_dur;
        this.serviceRate = service_rate;
        this.isSelected = isSelected;

    }

    public ServicesItems() {

    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getServiceETS() {
        return serviceETS;
    }

    public void setServiceETS(String serviceETS) {
        this.serviceETS = serviceETS;
    }

    public String getServiceRate() {
        return serviceRate;
    }

    public void setServiceRate(String serviceRate) {
        this.serviceRate = serviceRate;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
