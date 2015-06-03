package com.ebupt.wifibox.device;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ebupt.wifibox.R;
import com.ebupt.wifibox.networks.Networks;


/**
 * Created by zhaoqin on 4/23/15.
 */
public class ShortcutFragment extends Fragment{
    private final String TAG = "ShortcutFragment";
    private View contactslayout;
    private WebView webView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactslayout = inflater.inflate(R.layout.shortcut_layout, container, false);

        IntentFilter getURL = new IntentFilter("getURL");
        getActivity().registerReceiver(broadcastReceiver, getURL);
        return contactslayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        Networks.getSettingUrl(getActivity(), "none");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    private void present(String url) {
        webView = (WebView) contactslayout.findViewById(R.id.shortcut_webview);
        webView.getSettings().setAppCacheEnabled(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
            }

            @Override
            public void onPageFinished(WebView view, String url) {
            }
        });
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("getURL")) {
                present(intent.getStringExtra("url"));
            }
        }
    };
}
