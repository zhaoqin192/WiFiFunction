package com.ebupt.wifibox.group;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ebupt.wifibox.R;
import com.ebupt.wifibox.databases.GroupMSG;
import com.ebupt.wifibox.databases.RecordMSG;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by zhaoqin on 4/21/15.
 */
public class GroupAdapter extends BaseAdapter{
    private Context context;
    private List<GroupMSG> list;

    public GroupAdapter(Context context, List<GroupMSG> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.group_item, null);
            new ViewHolder(convertView);
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        GroupMSG groupMSG = list.get(position);

        holder.item_name.setText(groupMSG.getGroup_name());
        holder.item_date.setText(groupMSG.getGroup_date());
        holder.item_count.setText(groupMSG.getGroup_count());

        List<RecordMSG> record = DataSupport.where("group_id = ?", groupMSG.getGroup_id()).find(RecordMSG.class);
        RecordMSG recordMSG = record.get(0);
        holder.item_passport.setText("已上传护照:" + recordMSG.getUpload_passports());
        holder.item_list.setText("已上传列表:" + recordMSG.getNo_upload_passports());

        return convertView;
    }

    class ViewHolder{
        TextView item_name;
        TextView item_date;
        TextView item_count;
        TextView item_passport;
        TextView item_list;

        public ViewHolder(View view) {
            item_name = (TextView) view.findViewById(R.id.group_item_name);
            item_date = (TextView) view.findViewById(R.id.group_item_date);
            item_count = (TextView) view.findViewById(R.id.group_item_count);
            item_passport = (TextView) view.findViewById(R.id.group_item_passport_count);
            item_list = (TextView) view.findViewById(R.id.group_item_list_count);
            view.setTag(this);
        }

    }

}
