package com.ebupt.wifibox.databases;

import org.litepal.crud.DataSupport;

/**
 * Created by zhaoqin on 4/20/15.
 */
public class UserMSG extends DataSupport{
    private String phone;
    private String passwd;
    private Boolean memory;
    private Boolean auto;
    private String noticetime;

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getMemory() {
        return memory;
    }

    public void setMemory(Boolean memory) {
        this.memory = memory;
    }

    public Boolean getAuto() {
        return auto;
    }

    public void setAuto(Boolean auto) {
        this.auto = auto;
    }

    public void setNoticetime(String noticetime) {
        this.noticetime = noticetime;
    }

    public String getNoticetime() {
        return noticetime;
    }
}
