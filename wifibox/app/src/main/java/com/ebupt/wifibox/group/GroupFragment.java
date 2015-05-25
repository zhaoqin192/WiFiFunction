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
import com.ebupt.wifibox.group.list.GroupList;
import com.ebupt.wifibox.group.list.ListActivity;
import com.ebupt.wifibox.networks.Networks;

import org.litepal.crud.DataSupport;

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
    private Dialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactsLayout = inflater.inflate(R.layout.group_layout, container, false);

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
                showdialog();
                return true;
            }
        });

        IntentFilter addGroup = new IntentFilter("addGroup");
        IntentFilter updateGroup = new IntentFilter("updateGroup");
        getActivity().registerReceiver(broadcastReceiver, addGroup);
        getActivity().registerReceiver(broadcastReceiver, updateGroup);

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
                Log.e(TAG, "updateGroup");
                List<GroupMSG> list = DataSupport.findAll(GroupMSG.class);
                int size = list.size();
                Log.e(TAG, size + "");
                for (int i = 0; i < size; i++) {
                    datalist.add(list.get(i));
                }
                adapter.notifyDataSetChanged();
            }
        }
    };

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
}
