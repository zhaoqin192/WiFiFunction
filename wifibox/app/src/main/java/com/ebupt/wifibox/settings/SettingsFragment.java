package com.ebupt.wifibox.settings;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ebupt.wifibox.R;
import com.ebupt.wifibox.settings.tessocr.OCRActivity;
import com.ebupt.wifibox.settings.wifi.WifiAdmin;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhaoqin on 4/15/15.
 */
public class SettingsFragment extends Fragment{
    private View contactslayout;
//    private Button passport;
    private WifiAdmin wifiAdmin;
    private WifiManager wifiManager;
    private WifiInfo wifiInfo;
    private String wifi_name;
    private String wifi_mac;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactslayout = inflater.inflate(R.layout.settings_layout, container, false);
//        passport = (Button) contactslayout.findViewById(R.id.settings_passport);
//        passport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), OCRActivity.class);
//                startActivity(intent);
//            }
//        });

        wifiAdmin = new WifiAdmin(getActivity());
        wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                wifiInfo = wifiManager.getConnectionInfo();
                wifi_name = wifiInfo.getSSID();
                wifi_mac = wifiInfo.getBSSID();
                Log.e("xxx", wifi_name);
                if (wifi_mac != null) {
                    Log.e("yyy", wifi_mac);
                }
            }
        }, 1000, 10000);

        return contactslayout;
    }
}
