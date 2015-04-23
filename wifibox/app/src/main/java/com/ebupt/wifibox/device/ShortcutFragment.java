package com.ebupt.wifibox.device;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ebupt.wifibox.R;


/**
 * Created by zhaoqin on 4/23/15.
 */
public class ShortcutFragment extends Fragment{
    private View contactslayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactslayout = inflater.inflate(R.layout.shortcut_layout, container, false);

        return contactslayout;
    }
}
