package com.ebupt.wifibox.group.list;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.ebupt.wifibox.R;
import com.ebupt.wifibox.databases.DownVisitorMSG;
import com.ebupt.wifibox.databases.GroupMSG;
import com.ebupt.wifibox.ftp.FTPUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhaoqin on 4/23/15.
 */
public class SignFragment extends Fragment{
    private View contactsLayout;
    private ExpandableListView listView;
    private List<DownVisitorMSG> datalist;
    private SignAdapter adapter;
    private String groupid;

    @InjectView(R.id.sign_refresh)
    SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactsLayout = inflater.inflate(R.layout.sign_fragment_layout, container, false);
        ButterKnife.inject(this, contactsLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateUI();
            }
        });
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        listView = (ExpandableListView) contactsLayout.findViewById(R.id.sign_listview);
        listView.addHeaderView(LayoutInflater.from(getActivity()).inflate(R.layout.sign_listview_head, null));
        listView.setGroupIndicator(null);

        groupid = getArguments().getString("groupid");


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
        IntentFilter downDB = new IntentFilter("qiandao.db");
        IntentFilter readDB = new IntentFilter("readDBSuccess");
        IntentFilter uploadUserInfos = new IntentFilter("uploadUserInfos");
        getActivity().registerReceiver(broadcastReceiver, uploadUserInfos);
        getActivity().registerReceiver(broadcastReceiver, readDB);
        getActivity().registerReceiver(broadcastReceiver, downDB);
        getActivity().registerReceiver(broadcastReceiver, deleteVisitor);
        getActivity().registerReceiver(broadcastReceiver, updateVisitor);

        return contactsLayout;
    }

    private void updateUI() {
        datalist.clear();
        List<DownVisitorMSG> list = DataSupport.findAll(DownVisitorMSG.class);
        int size = list.size();
        if (size != 0) {
            for (int i = 0; i < size; i++) {
                datalist.add(list.get(i));
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
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }
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
            if (intent.getAction().equals("qiandao.db")) {
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
                List<GroupMSG> temp = DataSupport.where("group_id = ?", groupid).find(GroupMSG.class);
                if (temp.size() != 0) {
                    temp.get(0).setDownload(String.valueOf(list.size()));
                    temp.get(0).saveThrows();
                }
            }
            if (intent.getAction().equals("updateUserInfos")) {
                Toast.makeText(getActivity(), "上传用户数据成功", Toast.LENGTH_SHORT).show();
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
