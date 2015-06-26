package com.ebupt.wifibox.device;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ebupt.wifibox.MyApp;
import com.ebupt.wifibox.R;
import com.ebupt.wifibox.networks.Networks;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by zhaoqin on 4/23/15.
 */
public class ShortcutFragment extends Fragment{
    private final String TAG = "ShortcutFragment";
    private View contactslayout;
    private WebView webView;
    private String url = null;
    private ProgressDialog dialog;
    private MyApp myApp;

    @InjectView(R.id.shortcut_refresh)
    SwipeRefreshLayout refreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactslayout = inflater.inflate(R.layout.shortcut_layout, container, false);
        ButterKnife.inject(this, contactslayout);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (url != null) {
                    webView.loadUrl(url);
                }
            }
        });
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        Networks.getSettingUrl(getActivity(), "none");


        IntentFilter getURL = new IntentFilter("getURL");
        getActivity().registerReceiver(broadcastReceiver, getURL);

        myApp = (MyApp) getActivity().getApplicationContext();
        return contactslayout;
    }

    @Override
    public void onResume() {
        super.onResume();

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
                dialog = ProgressDialog.show(getActivity(), null, "页面加载中，请稍后...");
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
                if (refreshLayout != null) {
                    refreshLayout.setRefreshing(false);
                }
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("getURL")) {
                url = intent.getStringExtra("url");
                if (myApp.wifiConnectFlag) {
                    present(url);
                } else {
                    Toast.makeText(getActivity(), "当前没有管控路由器", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}
