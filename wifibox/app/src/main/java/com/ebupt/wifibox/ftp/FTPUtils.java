package com.ebupt.wifibox.ftp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ebupt.wifibox.databases.DownVisitorMSG;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by Administrator on 2015/5/15.
 */
public class FTPUtils {
    private static final String TAG = "FTPUtils";
    public static void downloadFileFromFTP(final Context context, final String ServerPath,final String filename){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                   final String localpath = "/mnt/sdcard/"+context.getPackageName()+"/";
                    new FTP(context).downloadSingleFile(ServerPath, localpath, filename,
                            new FTP.DownLoadProgressListener() {
                                @Override
                                public void onDownLoadProgress(String currentStep, long downProcess, File file) {
                                    Log.e(TAG, currentStep);
                                    if (currentStep.equals(FTPLogConstants.FTP_DOWN_SUCCESS)) {
                                        Log.e(TAG, "successful");
                                    } else if (currentStep.equals(FTPLogConstants.FTP_DOWN_LOADING)) {
                                        Log.e(TAG, downProcess + "%");
                                    }
                                }
                            });
                    Intent intent = new Intent("downloadDBSuccess");
                    context.sendBroadcast(intent);
                } catch (Exception e) {
                    Log.e(TAG, "下载失败");
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public static void readDownVisitorMSG(Context context, String databaseFilename) {
        try {
            File file = new File(databaseFilename);
            if (file.exists()) {
                Log.e(TAG, "file exists");
                SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(
                        file, null);
                String sql = "select * from qiandao";
                Cursor c = database.rawQuery(sql, null);
                if (c.getCount() > 0) {
                    while (c.moveToNext()) {
                        DownVisitorMSG downVisitorMSG = new DownVisitorMSG();
                        downVisitorMSG.setName(c.getString(c.getColumnIndex("username")));
                        downVisitorMSG.setMac(c.getString(c.getColumnIndex("mac")));
                        downVisitorMSG.setPhone(c.getString(c.getColumnIndex("phone")));
                        downVisitorMSG.saveThrows();
                    }
                }
            }
            Intent intent = new Intent("readDBSuccess");
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
