package com.muggins.ebupt.wififunction.settings;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.muggins.ebupt.wififunction.R;

/**
 * Created by qin on 2014/12/26.
 */
public class SettingsActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("X", "settingactivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
    }
}
