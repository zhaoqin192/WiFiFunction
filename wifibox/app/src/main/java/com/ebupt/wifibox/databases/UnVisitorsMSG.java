package com.ebupt.wifibox.databases;

import org.litepal.crud.DataSupport;

/**
 * Created by zhaoqin on 5/29/15.
 */
public class UnVisitorsMSG extends DataSupport{
    private String name;
    private String passports;
    private String groupid;
    private String brokerage;
    private String passportsid;

    public String getPassportsid() {
        return passportsid;
    }

    public void setPassportsid(String passportsid) {
        this.passportsid = passportsid;
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
