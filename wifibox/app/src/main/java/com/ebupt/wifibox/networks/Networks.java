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
import com.ebupt.wifibox.R;
import com.ebupt.wifibox.databases.DownVisitorMSG;
import com.ebupt.wifibox.databases.GroupMSG;
import com.ebupt.wifibox.databases.MessageTable;
import com.ebupt.wifibox.databases.UnVisitorsMSG;
import com.ebupt.wifibox.databases.UserMSG;
import com.ebupt.wifibox.databases.VisitorsMSG;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        myApp = (MyApp) context.getApplicationContext();
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

    public static void userInfos(Context context, String phone, String mac, String tourid) {
        final String TAG = "userInfos";
        requestQueue = Volley.newRequestQueue(context);
        StringBuffer url = new StringBuffer("http://10.1.29.254:28080/AppInterface/userInfos");

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("guide", phone);
            jsonObject.put("mac", mac);
            jsonObject.put("tourid", tourid);

            JSONArray array = new JSONArray();
            List<DownVisitorMSG> list = DataSupport.findAll(DownVisitorMSG.class);
            int size = list.size();
            if (size != 0) {
                for (int i = 0; i < size; i++) {
                    DownVisitorMSG downVisitorMSG = list.get(i);
                    JSONObject object = new JSONObject();
                    object.put("name", downVisitorMSG.getName());
                    object.put("phone", downVisitorMSG.getPhone());
                    object.put("mac", downVisitorMSG.getMac());
                    array.put(object);
                }
            }
            jsonObject.put("userInfos", array);
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                                DataSupport.deleteAll(GroupMSG.class);
                                JSONArray array = jsonObject.getJSONArray("tours");
                                int size = array.length();
                                for (int i = 0; i < size; i++) {
                                    JSONObject object = (JSONObject) array.get(i);
                                    GroupMSG groupMSG = new GroupMSG();
                                    groupMSG.setGroup_name(object.getString("tourname"));
                                    groupMSG.setGroup_id(object.getString("tourid"));
                                    groupMSG.setGroup_date(object.getString("createtime"));
                                    groupMSG.setGroup_count(object.getString("count"));
                                    if (object.getInt("overdue") == 0) {
                                        groupMSG.setInvalid(false);
                                    } else {
                                        groupMSG.setInvalid(true);
                                    }
                                    List<GroupMSG> list = DataSupport.where("group_id = ?", object.getString("tourid")).find(GroupMSG.class);
                                    if (list.size() == 0) {
                                        groupMSG.saveThrows();
                                        Intent intent = new Intent("updateGroup");
                                        context.sendBroadcast(intent);
                                    }
                                }
                                JSONArray jsonArray = jsonObject.getJSONArray("changetourids");
                                size = jsonArray.length();
                                if (size != 0) {
                                    for (int i = 0; i < size; i++) {
                                        MessageTable message = new MessageTable();
                                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                        Date curDate = new Date(System.currentTimeMillis());
                                        String time = formatter.format(curDate);
                                        message.setTime(time);
                                        message.setContent(context.getResources().getString(R.string.brokerage));
                                        message.setStatus(true);
                                        message.setGroupid(jsonArray.get(i).toString());
                                        message.saveThrows();
                                    }
                                    Intent intent = new Intent("updateBadge");
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

    public static void delTour(final Context context, String tourid) {
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
                            Intent intent = new Intent("delTour");
                            context.sendBroadcast(intent);
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

    public static void pollGroup(final Context context) {
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
                            try {
                                JSONArray array = jsonObject.getJSONArray("changetourids");
                                int size = array.length();
                                if (size != 0) {
                                    myApp.viewCount = size;
                                    for (int i = 0; i < size; i++) {
                                        MessageTable message = new MessageTable();
                                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                        Date curDate = new Date(System.currentTimeMillis());
                                        String time = formatter.format(curDate);
                                        message.setTime(time);
                                        message.setContent(context.getResources().getString(R.string.brokerage));
                                        message.setStatus(true);
                                        message.saveThrows();
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getPassports(final Context context, final String tourid) {
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
                                        Intent intent = new Intent("getVisitors");
                                        context.sendBroadcast(intent);
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void uploadPassports(final Context context, final String groupid) {
        final String TAG = "uploadPassports";
        requestQueue = Volley.newRequestQueue(context);
        myApp = (MyApp) context.getApplicationContext();
        String url = "http://10.1.29.254:28080/AppInterface/uploadPassports";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("guide", myApp.phone);
            jsonObject.put("tourid", groupid);
            jsonObject.put("mac", "12345678");
            final List<UnVisitorsMSG> list = DataSupport.findAll(UnVisitorsMSG.class);
            final int size = list.size();
            JSONArray array = new JSONArray();
            if (size != 0) {
                for (int i = 0; i < size; i++) {
                    JSONObject object = new JSONObject();
                    UnVisitorsMSG unVisitorsMSG = list.get(i);
                    object.put("name", unVisitorsMSG.getName());
                    object.put("passport", unVisitorsMSG.getPassports());
                    array.put(object);
                }
            }
            jsonObject.put("passports", array);

            final JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.e(TAG, jsonObject.toString());
                            try {
                                if (jsonObject.getBoolean("status")) {
                                    if (size != 0) {
                                        for (int i = 0; i < size; i++) {
                                            VisitorsMSG visitorsMSG = new VisitorsMSG();
                                            UnVisitorsMSG unVisitorsMSG = list.get(i);
                                            visitorsMSG.setBrokerage(unVisitorsMSG.getBrokerage());
                                            visitorsMSG.setGroupid(unVisitorsMSG.getGroupid());
                                            visitorsMSG.setPassports(unVisitorsMSG.getPassports());
                                            visitorsMSG.setName(unVisitorsMSG.getName());
                                            visitorsMSG.saveThrows();
                                        }
                                        DataSupport.deleteAll(UnVisitorsMSG.class, "groupid = ?", groupid);
                                    }
                                    Intent intent = new Intent("updateList");
                                    context.sendBroadcast(intent);
                                } else {
                                    Intent intent = new Intent("error");
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
                }
            });
            requestQueue.add(jsonRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void getSettingUrl(final Context context, String groupid) {
        final String TAG = "getSettingUrl";
        requestQueue = Volley.newRequestQueue(context);
        String url = "http://10.1.29.254:28080/AppInterface/getFastSetUrl";

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("tourid", groupid);
            jsonObject.put("mac", "mac");

            JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.e(TAG, jsonObject.toString());
                            try {
                                Intent intent = new Intent("getURL");
                                intent.putExtra("url", jsonObject.getString("url"));
                                context.sendBroadcast(intent);
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
