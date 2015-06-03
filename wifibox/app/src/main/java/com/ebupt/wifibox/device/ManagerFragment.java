package com.ebupt.wifibox.device;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ebupt.wifibox.R;

/**
 * Created by zhaoqin on 4/23/15.
 */
public class ManagerFragment extends Fragment{
    private View contactslayout;
    private WebView webView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactslayout = inflater.inflate(R.layout.manager_layout, container, false);


        String url = "http://a.miniap.cn";
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
            }
        });


        return contactslayout;
    }
}
