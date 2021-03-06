package com.ebupt.wifibox.group;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.ebupt.wifibox.R;
import com.ebupt.wifibox.databases.GroupMSG;
import com.ebupt.wifibox.group.list.ListActivity;
import com.ebupt.wifibox.networks.Networks;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhaoqin on 4/21/15.
 */
public class GroupFragment extends Fragment {
    private View contactsLayout;
    private static final String TAG = "wififragment";
    private ListView listView;
    private List<GroupMSG> datalist;
    private GroupAdapter adapter;
    private Dialog dialog;
    private String groupid;

    @InjectView(R.id.group_refresh)
    SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactsLayout = inflater.inflate(R.layout.group_layout, container, false);
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

        listView = (ListView) contactsLayout.findViewById(R.id.group_list);

        datalist = new ArrayList<>();

        adapter = new GroupAdapter(getActivity(), datalist);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ListActivity.class);
                intent.putExtra("name", datalist.get(position).getGroup_name());
                intent.putExtra("groupid", datalist.get(position).getGroup_id());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                groupid = datalist.get(position).getGroup_id();
                showdialog();
                return true;
            }
        });

        IntentFilter addGroup = new IntentFilter("addGroup");
        IntentFilter updateGroup = new IntentFilter("updateGroup");
        IntentFilter delGroup = new IntentFilter("delTour");
        getActivity().registerReceiver(broadcastReceiver, delGroup);
        getActivity().registerReceiver(broadcastReceiver, addGroup);
        getActivity().registerReceiver(broadcastReceiver, updateGroup);

        updateUI();
        Networks.getTours(getActivity());


        return contactsLayout;
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("addGroup")) {
                Log.e(TAG, "addGroup");
                GroupMSG groupMSG = DataSupport.findLast(GroupMSG.class);
                datalist.add(0, groupMSG);
                adapter.notifyDataSetChanged();
            }
            if (intent.getAction().equals("updateGroup")) {
                if (refreshLayout != null) {
                    refreshLayout.setRefreshing(false);
                }
                updateUI();
            }
            if (intent.getAction().equals("delTour")) {
                DataSupport.deleteAll(GroupMSG.class, "group_id = ?", groupid);
                updateUI();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void showdialog() {
        LayoutInflater inflaterDI = LayoutInflater.from(getActivity());
        LinearLayout layout = (LinearLayout) inflaterDI.inflate(R.layout.delete_group_layout, null);
        dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        Button yes = (Button) layout.findViewById(R.id.delete_group_ok);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Networks.delTour(getActivity(), groupid);
                dialog.hide();
            }
        });

        Button no = (Button) layout.findViewById(R.id.delete_group_cancel);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });
    }

    private void updateUI() {
        List<GroupMSG> list = DataSupport.findAll(GroupMSG.class);
        datalist.clear();
        for (GroupMSG groupMSG : list) {
            Log.e(TAG, "updateGroup");
            datalist.add(0, groupMSG);
        }
        adapter.notifyDataSetChanged();
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }
    }
}
