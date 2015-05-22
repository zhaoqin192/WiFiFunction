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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
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

//    public static JSONObject doPost(String url,JSONObject json){
//        DefaultHttpClient client = new DefaultHttpClient();
//        HttpPost post = new HttpPost(url);
//        JSONObject response = null;
//        try {
//            //º”√‹
//            //StringEntity s = new StringEntity(encode(json.toString()));
//            StringEntity s = new StringEntity(json.toString());
//            System.out.println("************************º”√‹«∞µƒrequest£∫"+json.toString());
//            System.out.println("************************º”√‹∫Ûµƒrequest£∫"+s);
//            s.setContentEncoding("UTF-8");
//            s.setContentType("application/json");//∑¢ÀÕjson ˝æ›–Ë“™…Ë÷√contentType
//            post.setEntity(s);
//            HttpResponse res = client.execute(post);
//            if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
//                HttpEntity entity = res.getEntity();
//                //String result = EntityUtils.toString(res.getEntity());// ∑µªÿjson∏Ò Ω£∫
//                //String result = decode(EntityUtils.toString(res.getEntity()));//
//                String result = EntityUtils.toString(res.getEntity());//
//                //Ω‚√‹
//                System.out.println("************************Ω‚√‹«∞µƒresponse£∫"+res.getEntity());
//                System.out.println("************************Ω‚√‹∫Ûµƒresponse£∫"+result);
//                response = JSONObject.fromObject(result);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        return response;
//    }

//    public static String encode(String str){
//        System.out.println("************************º”√‹∫Ûµƒstr£∫"+ConvertUtil.getEncodeKey(str));
//        return ConvertUtil.getEncodeKey(str);
//    }

//    public static String decode(String str){
//        return ConvertUtil.getDecodeKey(str);
//    }

//    public static JSONObject login(){
//        String url = targetIp + "/login";
//        String phoneNum = "13661022851";
//        String password = "12345678";
//        JSONObject params = new JSONObject();
//        params.put("phoneNum", phoneNum);
//        params.put("password", password);
////		    params.put("TENANT_ID", "123");
////		    params.put("NM", "’≈»˝");
////		    params.put("BRTH_DT", "1983-01-20");
////		    params.put("GND_CODE", "1");
////		    JSONArray params2 = new JSONArray();
////		    JSONObject param3 = new JSONObject();
////		    param3.put("DOC_TP_CODE", "10100");
////		    param3.put("DOC_NBR", "100200198301202210");
////		    param3.put("DOC_CUST_NM", "test");
////		    params2.add(param3);
////		    params.put("DOCS", params2);
//
//        System.out.println(params);
//        JSONObject result = doPost(url, params);
//        return result;
//    }
//
//    public static JSONObject userInfos(){
//        String url = targetIp + "/userInfos";
//
//        JSONObject params = new JSONObject();
//        params.put("guide", "13111111111");
//        params.put("mac", "MACSXF");
//        params.put("tourid", "1377");
//
//        JSONArray params2 = new JSONArray();
//        JSONObject param3 = new JSONObject();
//        param3.put("name", "’≈»˝");
//        param3.put("phone", "13411111111");
//        param3.put("mac", "123321");
//        params2.add(param3);
//        param3.put("name", "¿ÓÀƒ");
//        param3.put("phone", "13411111112");
//        param3.put("mac", "123322");
//        params2.add(param3);
//        params.put("userInfos", params2);
//
//        System.out.println(params);
//        JSONObject result = doPost(url, params);
//        return result;
//    }
//
//    public static JSONObject passports(){
//        String url = targetIp + "/passports";
//
//        JSONObject params = new JSONObject();
//        params.put("guide", "13111111111");
//        params.put("mac", "MACSXF");
//        params.put("tourid", "1377");
//
//        JSONArray params2 = new JSONArray();
//        JSONObject param3 = new JSONObject();
//        param3.put("phone", "13411111111");
//        param3.put("passport", "123231x");
//
//        params2.add(param3);
//        param3.put("phone", "13411111112");
//        param3.put("passport", "123232x");
//        params2.add(param3);
//        params.put("passports", params2);
//
//        System.out.println(params);
//        JSONObject result = doPost(url, params);
//        return result;
//    }
//
//    public static JSONObject getRebates(){
//        String url = targetIp + "/getRebates";
//
//        JSONObject params = new JSONObject();
//        params.put("tourid", "1377");
//        JSONObject result = doPost(url, params);
//        return result;
//    }
}
