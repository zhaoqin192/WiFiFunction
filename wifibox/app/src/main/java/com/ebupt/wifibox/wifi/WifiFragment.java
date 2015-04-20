package com.ebupt.wifibox.wifi;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ebupt.wifibox.R;
import com.ebupt.wifibox.networks.AESencryption;
import com.ebupt.wifibox.networks.Hex;

import java.security.Key;


/**
 * Created by zhaoqin on 4/15/15.
 */
public class WifiFragment extends Fragment {

    private View contactsLayout;
    private static final String TAG = "wififragment";



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactsLayout = inflater.inflate(R.layout.wifi_layout, container, false);




        return contactsLayout;
    }
}
