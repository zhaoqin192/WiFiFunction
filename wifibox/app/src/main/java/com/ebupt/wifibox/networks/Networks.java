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
import com.ebupt.wifibox.databases.GroupMSG;
import com.ebupt.wifibox.databases.UserMSG;
import com.ebupt.wifibox.databases.VisitorsMSG;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;

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

    public static void getTours(final Context context) {
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
                            try {
                                JSONArray array = jsonObject.getJSONArray("tours");
                                int size = array.length();
                                for (int i = 0; i < size; i++) {
                                    JSONObject object = (JSONObject) array.get(i);
                                    GroupMSG groupMSG = new GroupMSG();
                                    groupMSG.setGroup_name(object.getString("tourname"));
                                    groupMSG.setGroup_id(object.getString("tourid"));
                                    groupMSG.setGroup_date("none");
                                    groupMSG.setGroup_count("none");
                                    List<GroupMSG> list = DataSupport.where("group_id = ?", object.getString("tourid")).find(GroupMSG.class);
                                    if (list.size() == 0) {
                                        groupMSG.saveThrows();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e(TAG, volleyError.toString());
                }
            });
            requestQueue.add(jsonRequest);
            Intent intent = new Intent("updateGroup");
            context.sendBroadcast(intent);
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

    public static void pollGroup(Context context) {
        final String TAG = "pollGroup";
        requestQueue = Volley.newRequestQueue(context);
        myApp = (MyApp) context.getApplicationContext();
        String url = "http://10.1.29.254:28080/AppInterface/getTours";

        try {
            UserMSG userMSG = DataSupport.findFirst(UserMSG.class);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("guide", userMSG.getPhone());
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

    public static void getPassports(Context context, final String tourid) {
        final String TAG = "getPassports";
        requestQueue = Volley.newRequestQueue(context);
        String url = "http://10.1.29.254:28080/AppInterface/getPassports";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tourid", tourid);
            JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.e(TAG, jsonObject.toString());
                            try {
                                JSONArray array = jsonObject.getJSONArray("passports");
                                int size = array.length();
                                for (int i = 0; i < size; i++) {
                                    JSONObject object = (JSONObject) array.get(i);
                                    VisitorsMSG visitor = new VisitorsMSG();
                                    visitor.setGroupid(tourid);
                                    visitor.setName(object.getString("name"));
                                    visitor.setPassports(object.getString("passport"));
                                    visitor.setBrokerage(object.getString("rebate"));
                                    List<VisitorsMSG> list = DataSupport.where("gourid = ?", tourid).where("passports = ?", object.getString("passport"))
                                            .find(VisitorsMSG.class);
                                    if (list.size() == 0) {
                                        visitor.saveThrows();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.e(TAG, volleyError.toString());
                }
            });
            requestQueue.add(jsonRequest);
            Intent intent = new Intent("getVisitors");
            context.sendBroadcast(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
