package com.six.hats.brand.model.booking;

import androidx.annotation.Keep;

@Keep
public class SimpleStringItem {

    private String name;
    private boolean selected = false;
    private boolean isStaffSelected = false;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isStaffSelected() {
        return isStaffSelected;
    }

    public void setStaffSelected(boolean staffSelected) {
        isStaffSelected = staffSelected;
    }
}
