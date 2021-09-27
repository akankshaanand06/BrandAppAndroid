package com.six.hats.brand.model.booking;

import androidx.annotation.Keep;

import java.util.ArrayList;
import java.util.List;

@Keep
public class Services {
    private List<String> serviceId = new ArrayList<>();
    private String servicePrice;

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        this.servicePrice = servicePrice;
    }

    public List<String> getServices() {
        return serviceId;
    }

    public void setServices(List<String> services) {
        this.serviceId = services;
    }
}
