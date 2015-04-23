package com.ebupt.wifibox.device;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ebupt.wifibox.R;

/**
 * Created by zhaoqin on 4/21/15.
 */
public class DeviceFragment extends Fragment {
    private View contactslayout;
    private ManagerFragment managerFragment;
    private ShortcutFragment shortcutFragment;
    private FragmentManager fragmentManager;
    private ImageView manager_button;
    private ImageView shortcut_button;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactslayout = inflater.inflate(R.layout.device_layout, container, false);


        fragmentManager = getFragmentManager();
        initViews(contactslayout);


        return contactslayout;
    }

    private void initViews(View view) {
        manager_button = (ImageView) view.findViewById(R.id.device_manager);
        shortcut_button = (ImageView) view.findViewById(R.id.device_shortcut);

        manager_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelection(0);
            }
        });
        shortcut_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTabSelection(1);
            }
        });
    }

    private void clearSelection() {
        manager_button.setImageResource(R.drawable.tab_device_manager);
        shortcut_button.setImageResource(R.drawable.tab_shortcut);
    }

    private void setTabSelection(int index) {
        clearSelection();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                manager_button.setImageResource(R.drawable.tab_device_manager_hot);
                if (managerFragment == null) {
                    managerFragment = new ManagerFragment();
                    transaction.add(R.id.device_frame, managerFragment);
                } else {
                    transaction.show(managerFragment);
                }
                break;
            case 1:
            default:
                shortcut_button.setImageResource(R.drawable.tab_shortcut_hot);
                if (shortcutFragment == null) {
                    shortcutFragment = new ShortcutFragment();
                    transaction.add(R.id.device_frame, shortcutFragment);
                } else {
                    transaction.show(shortcutFragment);
                }
                break;
        }
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (managerFragment != null) {
            transaction.hide(managerFragment);
        }
        if (shortcutFragment != null) {
            transaction.hide(shortcutFragment);
        }

    }
}
