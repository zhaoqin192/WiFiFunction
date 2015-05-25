package com.ebupt.wifibox.networks;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.ebupt.wifibox.MyApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by zhaoqin on 4/20/15.
 */
public class Networks {
    private static RequestQueue requestQueue;
    private static MyApp myApp;


    public static void login(final Context context, String phone, String passwd) {
        final String TAG = "login";
        requestQueue = Volley.newRequestQueue(context);
        StringBuffer url = new StringBuffer("http://10.1.29.254:28080/AppInterface/login");

        HashMap<String, String> params = new HashMap<>();
        params.put("phoneNum", phone);
        params.put("password", passwd);
//        JSONObject params = new JSONObject();
//        params.put("phoneNum", phone);
//        params.put("password", passwd);

//        try {
//            JSONArray params2 = new JSONArray();
//            JSONObject param3 = new JSONObject();
//            param3.put("phone", "13411111111");
//            param3.put("passport", "123231x");
//            params2.add(param3);
//            param3.put("phone", "13411111112");
//            param3.put("passport", "123232x");
//            params2.add(param3);
//            params.put("passports", params2);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }



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

    public static void passports(Context context, String phone, String mac, String tourid, String gs, String array) {
        final String TAG = "passports";
        requestQueue = Volley.newRequestQueue(context);
        StringBuffer url = new StringBuffer("http://10.1.29.254:28080/AppInterface/passports");


        HashMap<String, String> params = new HashMap<>();
        params.put("guide", phone);
        params.put("mac", mac);
        params.put("tourid", tourid);
//        params.put("gs", gs);
//        params.put("passport", array);

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 4; i++) {
            JSONObject object = new JSONObject();
            try {
                object.put("phone", "123");
                object.put("passport", "456");
                jsonArray.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        //[{name:”张三”，passport:“123231”},{name:”李四”,passport”12312312”}]


        JSONObject jsstring = new JSONObject(params);
        try {
            jsstring.put("passports", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url.toString(), jsstring,
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

    public static void userInfos(Context context, String phone, String mac, String tourid, String gs, String array) {
        final String TAG = "userInfos";
        requestQueue = Volley.newRequestQueue(context);
        StringBuffer url = new StringBuffer("http://10.1.29.254:28080/AppInterface/userInfos");


        HashMap<String, String> params = new HashMap<>();
        params.put("guide", phone);
        params.put("mac", mac);
        params.put("tourid", tourid);
//        params.put("gs", gs);//201504177080808
//        params.put("userInfos", array);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 4; i++) {
            JSONObject object = new JSONObject();
            try {
                object.put("name", "123");
                object.put("phone", "123");
                object.put("mac", "789");
                jsonArray.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //[{names:“张三”,phone:”13411111111”,mac:”123321”},{names:“张三”,phone:”13411111111”,mac:”123321”}]

        JSONObject jsonObject = new JSONObject(params);
        try {
            jsonObject.put("userInfos", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public static void getrebates(Context context, String tourid) {
        final String TAG = "getrebates";
        requestQueue = Volley.newRequestQueue(context);
        StringBuffer url = new StringBuffer("http://10.1.29.254:28080/AppInterface/getRebates");

        HashMap<String, String> params = new HashMap<>();
        params.put("tourid", tourid);

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

    public static void getTours(Context context) {
        final String TAG = "getTours";
        requestQueue = Volley.newRequestQueue(context);
        myApp = (MyApp) context.getApplicationContext();
        String url = "http://10.1.29.254:28080/AppInterface/getTours";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("guide", myApp.phone);
            JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void addTour(Context context, String tourid, String tourname) {
        final String TAG = "addTour";
        requestQueue = Volley.newRequestQueue(context);
        myApp = (MyApp) context.getApplicationContext();
        String url = "http://10.1.29.254:28080/AppInterface/addTour";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tourid", tourid);
            jsonObject.put("tourname", tourname);
            jsonObject.put("guide", myApp.phone);
            JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void delTour(Context context, String tourid) {
        final String TAG = "delTour";
        requestQueue = Volley.newRequestQueue(context);
        myApp = (MyApp) context.getApplicationContext();
        String url = "http://10.1.29.254:28080/AppInterface/delTour";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tourid", tourid);
            JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
