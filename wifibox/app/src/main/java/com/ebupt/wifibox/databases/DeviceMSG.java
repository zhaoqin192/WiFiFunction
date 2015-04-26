package com.ebupt.wifibox.databases;

import org.litepal.crud.DataSupport;

/**
 * Created by zhaoqin on 4/24/15.
 */
public class DeviceMSG extends DataSupport{
    private String macAddress;
    private String passwd;
    private Boolean linkflag;

    public void setLinkflag(Boolean linkflag) {
        this.linkflag = linkflag;
    }

    public Boolean getLinkflag() {
        return linkflag;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getPasswd() {
        return passwd;
    }
}
