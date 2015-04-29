package com.ebupt.wifibox.databases;

import org.litepal.crud.DataSupport;

/**
 * Created by zhaoqin on 4/28/15.
 */
public class RecordMSG extends DataSupport{
    private String group_id;
    private int upload_passports;
    private int no_upload_passports;
    private int upload_sign;
    private int download_sign;

    public int getDownload_sign() {
        return download_sign;
    }

    public void setDownload_sign(int download_sign) {
        this.download_sign = download_sign;
    }

    public int getUpload_passports() {
        return upload_passports;
    }

    public void setUpload_passports(int upload_passports) {
        this.upload_passports = upload_passports;
    }

    public int getNo_upload_passports() {
        return no_upload_passports;
    }

    public void setNo_upload_passports(int no_upload_passports) {
        this.no_upload_passports = no_upload_passports;
    }

    public int getUpload_sign() {
        return upload_sign;
    }

    public void setUpload_sign(int upload_sign)
    {
        this.upload_sign = upload_sign;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_id() {
        return group_id;
    }
}
