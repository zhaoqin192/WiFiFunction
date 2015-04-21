package com.ebupt.wifibox.group;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ebupt.wifibox.R;
import com.ebupt.wifibox.databases.VisitorsMSG;

import java.util.List;

/**
 * Created by zhaoqin on 4/21/15.
 */
public class ListAdapter extends BaseExpandableListAdapter{
    private Context context;
    private List<VisitorsMSG> list;

    public ListAdapter(Context context, List<VisitorsMSG> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.group_list_item_son, null);
            new ViewHolder(convertView);
        }


        return convertView;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.group_list_item, null);
            new ViewHolder(convertView);
        }


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    class ViewHolder {
        TextView name;
        TextView passports;

        public ViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.group_list_item_name);
            passports = (TextView) view.findViewById(R.id.group_list_item_passport);
            view.setTag(this);
        }
    }

    class ViewHolderSon {
        TextView edit;
        TextView delete;

        public ViewHolderSon(View view) {
            edit = (TextView) view.findViewById(R.id.group_list_item_son_edit);
            delete = (TextView) view.findViewById(R.id.group_list_item_son_delete);
            view.setTag(this);
        }
    }
}
