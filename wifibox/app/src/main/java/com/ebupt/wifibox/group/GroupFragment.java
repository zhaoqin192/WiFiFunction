package com.ebupt.wifibox.group;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.ebupt.wifibox.R;
import com.ebupt.wifibox.databases.GroupMSG;
import com.ebupt.wifibox.databases.UserMSG;
import com.ebupt.wifibox.group.list.GroupList;

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


        GroupMSG groupMSG = null;
        for (int i = 0; i < 4; i++) {
            groupMSG = new GroupMSG();
            groupMSG.setGroup_name("1");
            groupMSG.setGroup_date("1");
            groupMSG.setGroup_count(1);
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

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showdialog();
                return true;
            }
        });

        IntentFilter addGroup = new IntentFilter("addGroup");
        getActivity().registerReceiver(broadcastReceiver, addGroup);

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
        }
    };

    private void showdialog() {
        LayoutInflater inflaterDI = LayoutInflater.from(getActivity());
        LinearLayout layout = (LinearLayout) inflaterDI.inflate(R.layout.delete_group_layout, null);
        dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        BootstrapButton yes = (BootstrapButton) layout.findViewById(R.id.delete_group_ok);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.hide();
            }
        });

        BootstrapButton no = (BootstrapButton) layout.findViewById(R.id.delete_group_cancel);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });
    }
}
