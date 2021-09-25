package com.six.hats.brand.model;

import androidx.annotation.Keep;

@Keep
public class AppointmentsItems {
    public String cust_Name;
    public String cust_serv;
    public String cust_cost;
    public String serv_dur;
    public String serv_app_time;
    public String staff_Name;
    public String location;
    public String payment_mode;
    public String passcode;
    public String serv_img;
    public String live_queue_no;
    public String my_queue_no;
    public String expected_delay;

    public AppointmentsItems(String cust_Name, String cust_serv, String cust_cost, String serv_dur, String serv_app_time, String staff_Name,
                             String location,
                             String payment_mode,
                             String passcode,
                             String serv_img,
                             String live_queue_no,
                             String my_queue_no,
                             String expected_delay) {

        this.cust_Name = cust_Name;
        this.cust_serv = cust_serv;
        this.cust_cost = cust_cost;
        this.serv_dur = serv_dur;
        this.serv_app_time = serv_app_time;
        this.staff_Name = staff_Name;
        this.location = location;
        this.payment_mode = payment_mode;
        this.passcode = passcode;
        this.serv_img = serv_img;
        this.live_queue_no = live_queue_no;
        this.my_queue_no = my_queue_no;
        this.expected_delay = expected_delay;

    }

    public AppointmentsItems(String cust_Name, String cust_serv/*, String cust_cost, String serv_dur, String serv_app_time, String staff_Name,
                             String location,
                             String payment_mode,
                             String passcode,
                             String serv_img,
                             String live_queue_no,
                             String my_queue_no,
                             String expected_delay*/) {

        this.cust_Name = cust_Name;
        this.cust_serv = cust_serv;
        this.cust_cost = cust_cost;
        this.serv_dur = serv_dur;
        this.serv_app_time = serv_app_time;
        this.staff_Name = staff_Name;
        this.location = location;
        this.payment_mode = payment_mode;
        this.passcode = passcode;
        this.serv_img = serv_img;
        this.live_queue_no = live_queue_no;
        this.my_queue_no = my_queue_no;
        this.expected_delay = expected_delay;

    }


    public AppointmentsItems() {

    }

    public String getCust_Name() {
        return cust_Name;
    }

    public void setCust_Name(String cust_Name) {
        this.cust_Name = cust_Name;
    }

    public String getCust_serv() {
        return cust_serv;
    }

    public void setCust_serv(String cust_serv) {
        this.cust_serv = cust_serv;
    }

    public String getCust_cost() {
        return cust_cost;
    }

    public void setCust_cost(String cust_cost) {
        this.cust_cost = cust_cost;
    }

    public String getServ_dur() {
        return serv_dur;
    }

    public void setServ_dur(String serv_dur) {
        this.serv_dur = serv_dur;
    }

    public String getServ_app_time() {
        return serv_app_time;
    }

    public void setServ_app_time(String serv_app_time) {
        this.serv_app_time = serv_app_time;
    }

    public String getStaff_Name() {
        return staff_Name;
    }

    public void setStaff_Name(String staff_Name) {
        this.staff_Name = staff_Name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public String getServ_img() {
        return serv_img;
    }

    public void setServ_img(String serv_img) {
        this.serv_img = serv_img;
    }

    public String getLive_queue_no() {
        return live_queue_no;
    }

    public void setLive_queue_no(String live_queue_no) {
        this.live_queue_no = live_queue_no;
    }

    public String getMy_queue_no() {
        return my_queue_no;
    }

    public void setMy_queue_no(String my_queue_no) {
        this.my_queue_no = my_queue_no;
    }

    public String getExpected_delay() {
        return expected_delay;
    }

    public void setExpected_delay(String expected_delay) {
        this.expected_delay = expected_delay;
    }
}
