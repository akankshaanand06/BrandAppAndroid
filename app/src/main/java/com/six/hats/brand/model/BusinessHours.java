package com.six.hats.brand.model;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.ArrayList;

@Keep
public class BusinessHours implements Serializable {
    private String day;
    private String date;
    private ArrayList<SlotList> slotList = new ArrayList<>();

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDay() {
        return this.day;
    }

    public void setSlotList(ArrayList<SlotList> slotList) {
        this.slotList = slotList;
    }

    public ArrayList<SlotList> getSlotList() {
        return this.slotList;
    }
}
