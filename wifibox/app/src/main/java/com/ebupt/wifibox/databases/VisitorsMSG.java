package com.ebupt.wifibox.databases;

import org.litepal.crud.DataSupport;

/**
 * Created by zhaoqin on 4/21/15.
 */
public class VisitorsMSG extends DataSupport{
    private String name;
    private String passports;
    private String groupid;

//    public VisitorsMSG(String name, String passports) {
//        super();
//        this.name = name;
//        this.passports = passports;
//    }
//
//    public VisitorsMSG() {
//
//    }

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
