package com.ebupt.wifibox.databases;

import org.litepal.crud.DataSupport;

/**
 * Created by zhaoqin on 4/21/15.
 */
public class GroupMSG extends DataSupport{
    private String group_name;
    private String group_date;
    private String group_count;
    private String group_id;
    private Boolean invalid;
    private String upload;
    private String download;

    public String getUpload() {
        return upload;
    }

    public void setUpload(String upload) {
        this.upload = upload;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public Boolean getInvalid() {
        return invalid;
    }

    public void setInvalid(Boolean invalid) {
        this.invalid = invalid;
    }

    public void setGroup_count(String group_count) {
        this.group_count = group_count;
    }

    public String getGroup_count() {
        return group_count;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_date(String group_date) {
        this.group_date = group_date;
    }

    public String getGroup_date() {
        return group_date;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_id() {
        return group_id;
    }
}
