package com.muggins.ebupt.wififunction.databases;

import org.litepal.crud.DataSupport;

/**
 * Created by qin on 2014/12/27.
 */
public class WiFiMessage extends DataSupport{
    private String SSID;
    private String password;

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getSSID() {
        return SSID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
