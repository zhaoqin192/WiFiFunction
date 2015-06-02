package com.ebupt.wifibox;

import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoqin on 4/21/15.
 */
public class MyApp extends LitePalApplication{
    public String phone;
    public PollService pollService;
    public int viewCount = 0;
    public List<String> fileList = new ArrayList<>();
}
