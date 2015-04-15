package com.ebupt.wifibox;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ebupt.wifibox.settings.SettingsFragment;
import com.ebupt.wifibox.web.WebFragment;
import com.ebupt.wifibox.wifi.WifiFragment;



public class MainActivity extends Activity implements View.OnClickListener{

    private WifiFragment wifiFragment;
    private WebFragment webFragment;
    private SettingsFragment settingsFragment;

    private View wifilayout;
    private View weblayout;
    private View settingslayout;

    private TextView wifitext;
    private TextView webtext;
    private TextView settingstext;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        fragmentManager = getFragmentManager();
        setTabSelection(0);
    }

    private void initViews() {
        wifilayout = findViewById(R.id.wifi_layout);
        weblayout = findViewById(R.id.web_layout);
        settingslayout = findViewById(R.id.settings_layout);

        wifitext = (TextView) findViewById(R.id.wifi_text);
        webtext = (TextView) findViewById(R.id.web_text);
        settingstext = (TextView) findViewById(R.id.settings_text);

        wifilayout.setOnClickListener(this);
        weblayout.setOnClickListener(this);
        settingslayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wifi_layout:
                setTabSelection(0);
                break;
            case R.id.web_layout:
                setTabSelection(1);
                break;
            case R.id.settings_layout:
                setTabSelection(2);
                break;
            default:
                break;
        }
    }

    private void clearSelection() {
        wifitext.setTextColor(Color.parseColor("#000000"));
        webtext.setTextColor(Color.parseColor("#000000"));
        settingstext.setTextColor(Color.parseColor("#000000"));
    }

    private void setTabSelection(int index) {
        clearSelection();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragment(fragmentTransaction);
        switch (index) {
            case 0:
                wifitext.setTextColor(Color.parseColor("#50B1DB"));
                if (wifiFragment == null) {
                    wifiFragment = new WifiFragment();
                    fragmentTransaction.add(R.id.content, wifiFragment);
                } else {
                    fragmentTransaction.show(wifiFragment);
                }
                break;
            case 1:
                webtext.setTextColor(Color.parseColor("#50B1DB"));
                if (webFragment == null) {
                    webFragment = new WebFragment();
                    fragmentTransaction.add(R.id.content, webFragment);
                } else {
                    fragmentTransaction.show(webFragment);
                }
                break;
            case 2:
            default:
                settingstext.setTextColor(Color.parseColor("#50B1DB"));
                if (settingsFragment == null) {
                    settingsFragment = new SettingsFragment();
                    fragmentTransaction.add(R.id.content, settingsFragment);
                } else {
                    fragmentTransaction.show(settingsFragment);
                }
                break;
        }
        fragmentTransaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (wifiFragment != null) {
            transaction.hide(wifiFragment);
        }
        if (webFragment != null) {
            transaction.hide(webFragment);
        }
        if (settingsFragment != null) {
            transaction.hide(settingsFragment);
        }
    }

}
