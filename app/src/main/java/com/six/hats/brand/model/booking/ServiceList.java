package com.six.hats.brand.model.booking;

import androidx.annotation.Keep;

@Keep
public class ServiceList {
    private Services serviceList = new Services();

    public Services getServiceList() {
        return serviceList;
    }

    public void setServiceList(Services serviceList) {
        this.serviceList = serviceList;
    }


}
