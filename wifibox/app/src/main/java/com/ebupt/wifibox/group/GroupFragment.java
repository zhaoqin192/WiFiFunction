package com.ebupt.wifibox.group;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ebupt.wifibox.R;
import com.ebupt.wifibox.databases.GroupMSG;
import com.ebupt.wifibox.group.list.GroupList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoqin on 4/21/15.
 */
public class GroupFragment extends Fragment {
    private View contactsLayout;
    private static final String TAG = "wififragment";
    private ListView listView;
    private List<GroupMSG> datalist;
    private GroupAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactsLayout = inflater.inflate(R.layout.group_layout, container, false);

        listView = (ListView) contactsLayout.findViewById(R.id.group_list);

        datalist = new ArrayList<>();


        GroupMSG groupMSG = null;
        for (int i = 0; i < 4; i++) {
            groupMSG = new GroupMSG();
            groupMSG.setGroup_name("1");
            groupMSG.setGroup_date("1");
            groupMSG.setGroup_count("1");
            datalist.add(groupMSG);
        }

        adapter = new GroupAdapter(getActivity(), datalist);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), GroupList.class);
                startActivity(intent);
            }
        });



        return contactsLayout;
    }
}
