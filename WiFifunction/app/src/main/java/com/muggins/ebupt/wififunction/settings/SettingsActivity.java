package com.muggins.ebupt.wififunction.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.muggins.ebupt.wififunction.R;

/**
 * Created by qin on 2014/12/26.
 */
public class SettingsActivity extends Activity{
    private TextView statetext;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        statetext = (TextView) findViewById(R.id.settings_state);

        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.settings_dialog, null);
        dialog = new AlertDialog.Builder(SettingsActivity.this).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        //打开dialog中的软键盘
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    public void dialog(View view){
        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.settings_dialog, null);
        dialog = new AlertDialog.Builder(SettingsActivity.this).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        //打开dialog中的软键盘
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }
}
