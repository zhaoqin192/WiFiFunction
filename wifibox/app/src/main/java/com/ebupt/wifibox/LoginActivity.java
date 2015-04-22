package com.ebupt.wifibox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.ebupt.wifibox.databases.UserMSG;
import com.ebupt.wifibox.networks.Networks;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by zhaoqin on 4/21/15.
 */
public class LoginActivity extends Activity{
    private BootstrapButton login_button;
    private BootstrapEditText login_name;
    private BootstrapEditText login_passwd;
    private CheckBox login_memory;
    private CheckBox login_auto;
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
                login_memory.setChecked(true);
                login_name.setText(userMSG.getPhone());
                login_passwd.setText(userMSG.getPasswd());
            }
            if (userMSG.getAuto()) {
                login_auto.setChecked(true);
                login_name.setText(userMSG.getPhone());
                login_passwd.setText(userMSG.getPasswd());
                Networks.login(this, userMSG.getPhone(), userMSG.getPasswd());
            }
        }
    }

    private void initViews() {

        login_memory = (CheckBox) findViewById(R.id.login_memory);

        login_auto = (CheckBox) findViewById(R.id.login_auto);


        login_button = (BootstrapButton) findViewById(R.id.login_button);
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
                if (login_memory.isChecked()) {
                    userMSG.setMemory(true);
                } else {
                    userMSG.setMemory(false);
                    DataSupport.deleteAll(UserMSG.class);
                }
                if (login_auto.isChecked()) {
                    userMSG.setAuto(true);
                } else {
                    userMSG.setAuto(false);
                }
                userMSG.saveThrows();
                myApp.phone = login_name.getText().toString();
                Networks.login(LoginActivity.this, userMSG.getPhone(), userMSG.getPasswd());
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        login_name = (BootstrapEditText) findViewById(R.id.login_edit_name);

        login_passwd = (BootstrapEditText) findViewById(R.id.login_edit_passwd);
    }
}
