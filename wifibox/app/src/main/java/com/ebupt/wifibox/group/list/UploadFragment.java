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
import com.ebupt.wifibox.databases.VisitorsMSG;
import com.ebupt.wifibox.networks.Networks;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoqin on 4/23/15.
 */
public class UploadFragment extends Fragment {
    private View contactsLayout;
    private ExpandableListView listView;
    private ListAdapter adapter;
    private List<VisitorsMSG> datalist;
    private String groupid;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactsLayout = inflater.inflate(R.layout.upload_fragment_layout, container, false);

        listView = (ExpandableListView) contactsLayout.findViewById(R.id.upload_list_expand);
        listView.setGroupIndicator(null);

        datalist = new ArrayList<>();



        groupid = getArguments().getString("groupid");

        adapter = new ListAdapter(getActivity(), datalist);

        listView.addHeaderView(LayoutInflater.from(getActivity()).inflate(R.layout.listview_head, null));
        listView.setAdapter(adapter);


        IntentFilter addVisitor = new IntentFilter("addVisitor");
        IntentFilter deleteVisitor = new IntentFilter("deleteVisitor");
        IntentFilter updateVisitor = new IntentFilter("updateVisitor");
        IntentFilter getVistors = new IntentFilter("getVisitors");
        getActivity().registerReceiver(broadcastReceiver, getVistors);
        getActivity().registerReceiver(broadcastReceiver, addVisitor);
        getActivity().registerReceiver(broadcastReceiver, deleteVisitor);
        getActivity().registerReceiver(broadcastReceiver, updateVisitor);

        Networks.getPassports(getActivity(), groupid);
        return contactsLayout;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("addVisitor")) {
                datalist.add(0, DataSupport.findLast(VisitorsMSG.class));
                Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
            }
            if (intent.getAction().equals("deleteVisitor")) {
                Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
            }
            if (intent.getAction().equals("updateVisitor")) {
                Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
            }
            if (intent.getAction().equals("getVisitors")) {
                Log.e("upload", "get");
                List<VisitorsMSG> temp = DataSupport.where("groupid = ?", groupid).find(VisitorsMSG.class);
                if (temp.size() != 0) {
                    for (VisitorsMSG visitorsMSG : temp) {
                        datalist.add(0, visitorsMSG);
                        datalist.add(0, visitorsMSG);
                        datalist.add(0, visitorsMSG);
                        datalist.add(0, visitorsMSG);
                        datalist.add(0, visitorsMSG);

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


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }
}
