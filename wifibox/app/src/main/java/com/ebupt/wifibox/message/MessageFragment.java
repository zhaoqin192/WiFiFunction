package com.ebupt.wifibox.message;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ebupt.wifibox.R;

/**
 * Created by zhaoqin on 5/22/15.
 */
public class MessageFragment extends Fragment {
    private View contactsLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactsLayout = inflater.inflate(R.layout.message_layout, container, false);


        return contactsLayout;
    }
}
