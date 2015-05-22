package com.ebupt.wifibox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.ebupt.wifibox.databases.DeviceMSG;
import com.ebupt.wifibox.databases.UserMSG;
import com.ebupt.wifibox.networks.Networks;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by zhaoqin on 4/21/15.
 */
public class LoginActivity extends Activity{
    private Button login_button;
    private BootstrapEditText login_name;
    private BootstrapEditText login_passwd;
    private ImageView login_memory;
    private ImageView login_auto;
    private UserMSG userMSG;
    private MyApp myApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_layout);

        myApp = (MyApp) getApplicationContext();

        initViews();

        List<UserMSG> list = DataSupport.findAll(UserMSG.class);
        if (list.size() != 0) {
            userMSG = list.get(0);
            if (userMSG.getMemory()) {
                login_name.setText(userMSG.getPhone());
                login_passwd.setText(userMSG.getPasswd());
            }
            if (userMSG.getAuto()) {
                login_name.setText(userMSG.getPhone());
                login_passwd.setText(userMSG.getPasswd());
                Networks.login(this, userMSG.getPhone(), userMSG.getPasswd());
            }
        } else {
            userMSG = new UserMSG();
            userMSG.setMemory(false);
            userMSG.setAuto(false);
        }

        login_memory = (ImageView) findViewById(R.id.login_memory);
        if (userMSG.getMemory()) {
            login_memory.setImageResource(R.drawable.selected);
        } else {
            login_memory.setImageResource(R.drawable.unselect);
        }
        login_memory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userMSG.getMemory()) {
                    userMSG.setMemory(false);
                    login_memory.setImageResource(R.drawable.unselect);
                    DataSupport.deleteAll(UserMSG.class);
                } else {
                    userMSG.setMemory(true);
                    login_memory.setImageResource(R.drawable.selected);
                }
                userMSG.saveThrows();
            }
        });

        login_auto = (ImageView) findViewById(R.id.login_auto);
        if (userMSG.getAuto()) {
            login_auto.setImageResource(R.drawable.selected);
        } else {
            login_auto.setImageResource(R.drawable.unselect);
        }
        login_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userMSG.getAuto()) {
                    userMSG.setAuto(false);
                    login_auto.setImageResource(R.drawable.unselect);
                } else {
                    userMSG.setAuto(true);
                    userMSG.setMemory(true);
                    login_memory.setImageResource(R.drawable.selected);
                    login_auto.setImageResource(R.drawable.selected);
                }
                userMSG.saveThrows();
            }
        });


        DeviceMSG deviceMSG = DataSupport.findFirst(DeviceMSG.class);
        if (deviceMSG == null) {
            deviceMSG = new DeviceMSG();
        }
//            deviceMSG.setMacAddress("64:51:7e:3a:e5:14");
//            deviceMSG.setMacAddress("66:51:7e:38:e9:80");
//            deviceMSG.setMacAddress("00:1f:64:ec:4e:6a");//butp3---16
//            deviceMSG.setMacAddress("6a:43:7c:0a:e8:40");//Zhao的iPhone---17
//        deviceMSG.setMacAddress("00:1f:64:eb:4d:4d");//Cert_Download---18
        deviceMSG.setMacAddress("66:51:7e:38:e9:80");//EBUPT-INNER-WIFI
        deviceMSG.setPasswd("1082325588");
            deviceMSG.setLinkflag(false);
            deviceMSG.saveThrows();
    }

    private void initViews() {

        login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userMSG == null) {
                    userMSG = new UserMSG();
                }
                if (login_name.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (login_passwd.getText().toString().equals("")) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                userMSG.setPhone(login_name.getText().toString());
                userMSG.setPasswd(login_passwd.getText().toString());
//                if (userMSG.getMemory()) {
//                    userMSG.setMemory(true);
//                } else {
//                    userMSG.setMemory(false);
//                    DataSupport.deleteAll(UserMSG.class);
//                }
//                if (userMSG.getAuto()) {
//                    userMSG.setAuto(true);
//                } else {
//                    userMSG.setAuto(false);
//                }
                userMSG.saveThrows();
                myApp.phone = login_name.getText().toString();
                Networks.login(LoginActivity.this, userMSG.getPhone(), userMSG.getPasswd());
                uploadpassports();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        login_name = (BootstrapEditText) findViewById(R.id.login_edit_name);

        login_passwd = (BootstrapEditText) findViewById(R.id.login_edit_passwd);
    }

    public void uploadpassports() {
        Log.e("passports", "uploadpassports");
        StringBuffer str2 = new StringBuffer("[");
        for (int i = 0; i < 4; i++) {
            str2.append("{\"names\":");
            str2.append("\"张三\",");
            str2.append("\"phone\":");
            str2.append("\"123456\",");
            str2.append("\"mac\":");

            if (i != 3) {
                str2.append("\"123456\"},");
            } else {
                str2.append("\"123456\"}");
            }
        }
        str2.append("]");
        Networks.userInfos(LoginActivity.this, myApp.phone, "mac", "tourid", "gs", str2.toString());
        Networks.passports(LoginActivity.this, myApp.phone, "mac", "tourid", "gs", str2.toString());
        Networks.getrebates(LoginActivity.this, "tourid");
    }
}
