package com.muggins.ebupt.wififunction;

import android.app.Application;

/**
 * Created by qin on 2014/12/26.
 */
public class MyApp extends Application{
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
