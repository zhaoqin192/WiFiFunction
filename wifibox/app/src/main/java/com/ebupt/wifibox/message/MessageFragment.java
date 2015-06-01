package com.ebupt.wifibox.message;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ebupt.wifibox.R;
import com.ebupt.wifibox.databases.MessageTable;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhaoqin on 5/22/15.
 */
public class MessageFragment extends Fragment {
    private View contactsLayout;
    private MessageAdapter adapter;
    private List<MessageTable> list;

    @InjectView(R.id.message_listview)
    ListView listView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactsLayout = inflater.inflate(R.layout.message_layout, container, false);
        ButterKnife.inject(this, contactsLayout);

        list = new ArrayList<>();
        List<MessageTable> datalist = DataSupport.findAll(MessageTable.class);
        int size = datalist.size();
        if (size != 0) {
            for (int i = 0; i < size; i++) {
                list.add(datalist.get(i));
            }
        }

        adapter = new MessageAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        return contactsLayout;
    }
}
