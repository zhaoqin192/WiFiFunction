package com.ebupt.wifibox.group.list;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.ebupt.wifibox.R;
import com.ebupt.wifibox.databases.DownVisitorMSG;
import com.ebupt.wifibox.ftp.FTPUtils;


import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoqin on 4/23/15.
 */
public class SignFragment extends Fragment{
    private View contactsLayout;
    private ExpandableListView listView;
    private List<DownVisitorMSG> datalist;
    private SignAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactsLayout = inflater.inflate(R.layout.sign_fragment_layout, container, false);

        listView = (ExpandableListView) contactsLayout.findViewById(R.id.sign_listview);
        listView.setGroupIndicator(null);


        datalist = new ArrayList<>();
        List<DownVisitorMSG> list = DataSupport.findAll(DownVisitorMSG.class);
        int size = list.size();
        if (size != 0) {
            for (int i = 0; i < size; i++) {
                datalist.add(list.get(i));
            }
        }

        adapter = new SignAdapter(getActivity(), datalist);
        listView.setAdapter(adapter);


        IntentFilter deleteVisitor = new IntentFilter("deleteDownVisitor");
        IntentFilter updateVisitor = new IntentFilter("updateDownVisitor");
        IntentFilter downDB = new IntentFilter("downloadDBSuccess");
        IntentFilter readDB = new IntentFilter("readDBSuccess");
        getActivity().registerReceiver(broadcastReceiver, readDB);
        getActivity().registerReceiver(broadcastReceiver, downDB);
        getActivity().registerReceiver(broadcastReceiver, deleteVisitor);
        getActivity().registerReceiver(broadcastReceiver, updateVisitor);

        return contactsLayout;
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("deleteDownVisitor")) {
                Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
            }
            if (intent.getAction().equals("updateDownVisitor")) {
                Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
            }
            if (intent.getAction().equals("downloadDBSuccess")) {
                Log.e("FTPUtils", "downloadDBSuccess");
                DataSupport.deleteAll(DownVisitorMSG.class);
                String dataPath = "/mnt/sdcard/" + getActivity().getPackageName() + "/qiandao.db";
                FTPUtils.readDownVisitorMSG(getActivity(), dataPath);
            }
            if (intent.getAction().equals("readDBSuccess")) {
                datalist.clear();
                List<DownVisitorMSG> list = DataSupport.findAll(DownVisitorMSG.class);
                int size = list.size();
                if (size != 0) {
                    for (int i = 0; i < size; i++) {
                        datalist.add(list.get(i));
                    }
                }

            }
            adapter.notifyDataSetChanged();
            int count = adapter.getGroupCount();
            for (int i = 0; i < count; i++) {
                if (listView.isGroupExpanded(i)) {
                    listView.collapseGroup(i);
                }
            }
            listView.requestFocusFromTouch();
            listView.setSelection(0);
        }
    };
}
