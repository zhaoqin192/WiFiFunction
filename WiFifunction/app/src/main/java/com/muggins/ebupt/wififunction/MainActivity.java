package com.muggins.ebupt.wififunction;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.muggins.ebupt.wififunction.settings.SettingsActivity;
import com.muggins.ebupt.wififunction.webview.ViewActivity;
import com.muggins.ebupt.wififunction.wifiscan.WifiScanActivity;


public class MainActivity extends TabActivity {
    private TabHost mtabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mtabHost = getTabHost();
        addwifiscan();
        addwebview();
        addsettings();
    }

    public void addwifiscan(){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, WifiScanActivity.class);

        TabSpec spec = mtabHost.newTabSpec("wifiscan");
        spec.setIndicator(getString(R.string.wifiscan), null);
        spec.setContent(intent);
        mtabHost.addTab(spec);
    }

    public void addwebview(){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, ViewActivity.class);

        TabSpec spec = mtabHost.newTabSpec("view");
        spec.setIndicator(getString(R.string.wifiview), null);
        spec.setContent(intent);
        mtabHost.addTab(spec);
    }

    public void addsettings(){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, SettingsActivity.class);

        TabSpec spec = mtabHost.newTabSpec("settings");
        spec.setIndicator(getString(R.string.wifiset), null);
        spec.setContent(intent);
        mtabHost.addTab(spec);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
