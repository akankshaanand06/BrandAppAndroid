package com.six.hats.brand.model;

import androidx.annotation.Keep;

@Keep
public class LoginRequest {

    private String userName;
    private String password;
    private String imeiNumber;

    public String getMobile() {
        return userName;
    }

    public void setMobile(String mobile) {
        this.userName = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImeiNumber() {
        return imeiNumber;
    }

    public void setImeiNumber(String imeiNumber) {
        this.imeiNumber = imeiNumber;
    }
}
