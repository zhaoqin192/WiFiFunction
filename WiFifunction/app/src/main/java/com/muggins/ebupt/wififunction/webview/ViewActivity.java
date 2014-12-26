package com.muggins.ebupt.wififunction.webview;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.muggins.ebupt.wififunction.MyApp;
import com.muggins.ebupt.wififunction.R;

/**
 * Created by qin on 2014/12/26.
 */
public class ViewActivity extends Activity{
    private WebView webView;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("X", "viewactivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifiview);
        webView = (WebView) findViewById(R.id.webview);

        getweb();
    }

    public void getweb(){
        MyApp myApp = (MyApp) getApplicationContext();
        Toast.makeText(ViewActivity.this, myApp.getKey(), Toast.LENGTH_SHORT).show();

        url = "http://www.baidu.com";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);

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
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        getweb();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
