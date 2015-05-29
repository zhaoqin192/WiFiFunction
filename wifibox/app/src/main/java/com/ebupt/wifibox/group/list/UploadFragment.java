package com.ebupt.wifibox.group.list;


import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.ebupt.wifibox.R;
import com.ebupt.wifibox.databases.UnVisitorsMSG;
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




        updateUI();



        adapter = new ListAdapter(getActivity(), datalist);

        listView.addHeaderView(LayoutInflater.from(getActivity()).inflate(R.layout.listview_head, null));
        listView.setAdapter(adapter);


        IntentFilter addVisitor = new IntentFilter("addVisitor");
        IntentFilter deleteVisitor = new IntentFilter("deleteVisitor");
        IntentFilter updateVisitor = new IntentFilter("updateVisitor");
        IntentFilter getVistors = new IntentFilter("getVisitors");
        IntentFilter updatelist = new IntentFilter("updateList");
        IntentFilter error = new IntentFilter("error");
        getActivity().registerReceiver(broadcastReceiver, updatelist);
        getActivity().registerReceiver(broadcastReceiver, error);
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
                UnVisitorsMSG unVisitorsMSG = DataSupport.findFirst(UnVisitorsMSG.class);
                VisitorsMSG visitorsMSG = new VisitorsMSG();
                visitorsMSG.setName(unVisitorsMSG.getName());
                visitorsMSG.setPassports(unVisitorsMSG.getPassports());
                visitorsMSG.setBrokerage(unVisitorsMSG.getBrokerage());
                visitorsMSG.setGroupid(unVisitorsMSG.getGroupid());

                datalist.add(visitorsMSG);
                Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
            }
            if (intent.getAction().equals("deleteVisitor")) {
                Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
            }
            if (intent.getAction().equals("updateVisitor")) {
                Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
            }
            if (intent.getAction().equals("getVisitors")) {
                updateUI();
            }
            if (intent.getAction().equals("error")) {
                Toast.makeText(context, "上传失败", Toast.LENGTH_LONG).show();
            }
            if (intent.getAction().equals("updateList")) {
                updateUI();
                Toast.makeText(context, "上传成功", Toast.LENGTH_SHORT).show();
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

    private void updateUI() {
        datalist.clear();

        VisitorsMSG head = new VisitorsMSG();
        String str = "head";
        head.setGroupid(str);
        head.setName(str);
        head.setPassports(str);
        head.setBrokerage(str);
        datalist.add(head);

        List<VisitorsMSG> temp = DataSupport.where("groupid = ?", groupid).find(VisitorsMSG.class);
        if (temp.size() != 0) {
            for (VisitorsMSG visitorsMSG : temp) {
                datalist.add(0, visitorsMSG);
            }
        }
        List<UnVisitorsMSG> untemp = DataSupport.where("groupid = ?", groupid).find(UnVisitorsMSG.class);
        int size = untemp.size();
        if (size != 0) {
            for (UnVisitorsMSG unVisitorsMSG : untemp) {
                VisitorsMSG visitorsMSG = new VisitorsMSG();
                visitorsMSG.setGroupid(unVisitorsMSG.getGroupid());
                visitorsMSG.setBrokerage(unVisitorsMSG.getBrokerage());
                visitorsMSG.setPassports(unVisitorsMSG.getPassports());
                visitorsMSG.setName(unVisitorsMSG.getName());
                datalist.add(visitorsMSG);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }
}
