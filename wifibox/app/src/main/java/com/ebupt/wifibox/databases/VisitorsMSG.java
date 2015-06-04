package com.ebupt.wifibox.databases;

import org.litepal.crud.DataSupport;

/**
 * Created by zhaoqin on 4/21/15.
 */
public class VisitorsMSG extends DataSupport{
    private String name;
    private String passports;
    private String groupid;
    private String brokerage;
    private String passports_id;

    public String getPassports_id() {
        return passports_id;
    }

    public void setPassports_id(String passports_id) {
        this.passports_id = passports_id;
    }

    public void setBrokerage(String brokerage) {
        this.brokerage = brokerage;
    }

    public String getBrokerage() {
        return brokerage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPassports(String passports) {
        this.passports = passports;
    }

    public String getPassports() {
        return passports;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupid() {
        return groupid;
    }
}
