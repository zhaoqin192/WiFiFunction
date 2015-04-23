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
import com.ebupt.wifibox.databases.DownVisitorMSG;


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
        DownVisitorMSG visitorMSG = null;
        for (int i = 0; i < 4; i++) {
            visitorMSG = new DownVisitorMSG();
            datalist.add(0, visitorMSG);
        }

        adapter = new SignAdapter(getActivity(), datalist);
        listView.setAdapter(adapter);



        IntentFilter deleteVisitor = new IntentFilter("deleteDownVisitor");
        IntentFilter updateVisitor = new IntentFilter("updateDownVisitor");
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
