package com.ebupt.wifibox.device;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ebupt.wifibox.MyApp;
import com.ebupt.wifibox.R;


import org.apache.http.util.EncodingUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhaoqin on 4/23/15.
 */
public class ManagerFragment extends Fragment{
    private View contactslayout;
    private WebView webView;
    private ProgressDialog dialog;
    private String url;
    private String postData = "password=he123456";
    private MyApp myApp;

    @InjectView(R.id.manager_refresh)
    SwipeRefreshLayout refreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactslayout = inflater.inflate(R.layout.manager_layout, container, false);
        ButterKnife.inject(this, contactslayout);
        url = "http://a.miniap.cn/actionAuth.cgi";


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.postUrl(url, EncodingUtils.getBytes(postData, "base64"));
            }
        });
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        webView = (WebView) contactslayout.findViewById(R.id.manager_webview);
        myApp = (MyApp) getActivity().getApplicationContext();

        return contactslayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (myApp.wifiConnectFlag) {
            webView.getSettings().setAppCacheEnabled(true);
            webView.loadUrl("sdf");
            webView.postUrl(url, EncodingUtils.getBytes(postData, "base64"));
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    dialog = ProgressDialog.show(getActivity(), null, "页面加载中,请稍后...");

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
        } else {
            Toast.makeText(getActivity(), "当前没有管控路由器", Toast.LENGTH_SHORT).show();
        }
    }

}
