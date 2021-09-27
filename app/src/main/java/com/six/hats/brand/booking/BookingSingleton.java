package com.six.hats.brand.booking;

import androidx.annotation.Keep;


import com.six.hats.brand.model.ServicesData;
import com.six.hats.brand.util.CommonUtility;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Keep
public class BookingSingleton {
    private static BookingSingleton sSoleInstance;
    private static LinkedHashMap<String, ServicesData> serviceResponses = new LinkedHashMap<>();


    //private constructor.
    private BookingSingleton() {

        //Prevent form the reflection api.
        if (sSoleInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static BookingSingleton getInstance() {
        if (sSoleInstance == null) { //if there is no instance available... create new one
            sSoleInstance = new BookingSingleton();
        }

        return sSoleInstance;
    }


    public static ServicesData getServicesData(String serviceID) {

        ServicesData servicesData = new ServicesData();
        try {
            if (CommonUtility.chkString(serviceID)) {
                for (int i = 0; i < BookingSingleton.getServiceResponses().size(); i++) {
                    String key = (new ArrayList<String>(BookingSingleton.getServiceResponses().keySet())).get(i);
                    if (serviceID.equalsIgnoreCase(key)) {
                        servicesData = BookingSingleton.getServiceResponses().get(serviceID);
                        servicesData.setSelected(false);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return servicesData;
    }


    public static LinkedHashMap<String, ServicesData> getServiceResponses() {
        return serviceResponses;
    }

    public static void setServiceResponses(List<ServicesData> serviceResponses) {
        for (int i = 0; i < serviceResponses.size(); i++) {

            if (!BookingSingleton.serviceResponses.containsKey(serviceResponses.get(i).getId())) {
                BookingSingleton.serviceResponses.put(serviceResponses.get(i).getId(), serviceResponses.get(i));
            }

        }

    }
}
