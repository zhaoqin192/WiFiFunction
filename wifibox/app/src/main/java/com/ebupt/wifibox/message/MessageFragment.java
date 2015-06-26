package com.ebupt.wifibox.message;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ebupt.wifibox.MyApp;
import com.ebupt.wifibox.R;
import com.ebupt.wifibox.databases.GroupMSG;
import com.ebupt.wifibox.databases.MessageTable;
import com.ebupt.wifibox.group.list.ListActivity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

/**
 * Created by zhaoqin on 5/22/15.
 */
public class MessageFragment extends Fragment {
    private static String TAG = "MessageFragment";
    private View contactsLayout;
    private MessageAdapter adapter;
    private List<MessageTable> list;
    private MyApp myApp;

    @InjectView(R.id.message_listview)
    ListView listView;

    @OnItemClick(R.id.message_listview)
    void onItemClick(int position) {
        Log.e(TAG, position + "");
        MessageTable messageTable = list.get(position);

        String groupid = messageTable.getGroupid();
        Log.e(TAG, groupid);
        List<GroupMSG> grouplist = DataSupport.where("group_id = ?", groupid).find(GroupMSG.class);
        GroupMSG groupMSG = grouplist.get(0);

        messageTable.setStatus(false);
        messageTable.saveThrows();

        Intent intent = new Intent(getActivity(), ListActivity.class);
        intent.putExtra("name", groupMSG.getGroup_name());
        intent.putExtra("groupid", groupMSG.getGroup_id());
        intent.putExtra("type", messageTable.getType());
        startActivity(intent);
    }

    @InjectView(R.id.message_refresh)
    SwipeRefreshLayout refreshLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        contactsLayout = inflater.inflate(R.layout.message_layout, container, false);
        ButterKnife.inject(this, contactsLayout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateList();
            }
        });
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        listView = (ListView) contactsLayout.findViewById(R.id.message_listview);
        myApp = (MyApp) getActivity().getApplicationContext();

        list = new ArrayList<>();
        adapter = new MessageAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        return contactsLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        updateList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        List<MessageTable> list = DataSupport.findAll(MessageTable.class);
        int size = list.size();
        if (size != 0) {
            for (int i = 0; i < size; i++) {
                if (!list.get(i).getStatus()) {
                    list.get(i).delete();
                }
            }
        }
    }

    private void updateList() {
        list.clear();
        List<MessageTable> datalist = DataSupport.findAll(MessageTable.class);
        myApp.viewCount = 0;
        int size = datalist.size();
        if (size != 0) {
            for (int i = 0; i < size; i++) {
                list.add(0, datalist.get(i));
            }
        }
        Intent intent = new Intent("updateBadge");
        getActivity().sendBroadcast(intent);
        adapter.notifyDataSetChanged();
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }
    }

}
