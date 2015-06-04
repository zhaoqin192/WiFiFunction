package com.ebupt.wifibox.device;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ebupt.wifibox.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhaoqin on 4/23/15.
 */
public class ManagerFragment extends Fragment{
    private View contactslayout;
    private WebView webView;

    @InjectView(R.id.manager_refresh)
    SwipeRefreshLayout refreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactslayout = inflater.inflate(R.layout.manager_layout, container, false);
        ButterKnife.inject(this, contactslayout);
        final String url = "http://a.miniap.cn";


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.loadUrl(url);
            }
        });
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        webView = (WebView) contactslayout.findViewById(R.id.manager_webview);

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
                if (refreshLayout != null) {
                    refreshLayout.setRefreshing(false);
                }
            }
        });


        return contactslayout;
    }
}
