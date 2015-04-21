package com.ebupt.wifibox.networks;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.ebupt.wifibox.databases.UserMSG;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.HashMap;

/**
 * Created by zhaoqin on 4/20/15.
 */
public class Networks {
    private static RequestQueue requestQueue;


    public static void login(final Context context, String phone, String passwd) {
        final String TAG = "login";
        requestQueue = Volley.newRequestQueue(context);
        StringBuffer url = new StringBuffer("");

        HashMap<String, String> params = new HashMap<>();
        params.put("phoneNum", phone);
        params.put("password", passwd);

        JSONObject jsonObject = new JSONObject(params);
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url.toString(), jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e(TAG, jsonObject.toString());
                        try {
                            if (jsonObject.getBoolean("status")) {
                                Intent intent = new Intent("login_success");
                                context.sendBroadcast(intent);
                            } else {
                                Intent intent = new Intent("login_error");
                                context.sendBroadcast(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.toString());
                Intent intent = new Intent("login_error");
                context.sendBroadcast(intent);
            }
        });
        requestQueue.add(jsonRequest);
    }

    public static void passports(Context context) {
        final String TAG = "passports";
        requestQueue = Volley.newRequestQueue(context);
        StringBuffer url = new StringBuffer("");

        UserMSG userMSG = DataSupport.findFirst(UserMSG.class);
        if (userMSG == null) {
            Toast.makeText(context, "未注册用户", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("guide", userMSG.getPhone());
        params.put("mac", "");
        params.put("tourid", "");
        params.put("gs", "");
        params.put("passports", "");

        //[{name:”张三”，passport:“123231”},{name:”李四”,passport”12312312”}]


        JSONObject jsonObject = new JSONObject(params);
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url.toString(), jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e(TAG, jsonObject.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.toString());
            }
        });
        requestQueue.add(jsonRequest);
    }

    public static void userInfos(Context context) {
        final String TAG = "userInfos";
        requestQueue = Volley.newRequestQueue(context);
        StringBuffer url = new StringBuffer("");

        UserMSG userMSG = DataSupport.findFirst(UserMSG.class);
        if (userMSG == null) {
            Toast.makeText(context, "未注册用户", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("guide", userMSG.getPhone());
        params.put("mac", "");
        params.put("tourid", "");
        params.put("gs", "");//201504177080808
        params.put("userInfos", "");

        //[{names:“张三”,phone:”13411111111”,mac:”123321”},{names:“张三”,phone:”13411111111”,mac:”123321”}]

        JSONObject jsonObject = new JSONObject(params);
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url.toString(), jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e(TAG, jsonObject.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.toString());
            }
        });
        requestQueue.add(jsonRequest);
    }
}
