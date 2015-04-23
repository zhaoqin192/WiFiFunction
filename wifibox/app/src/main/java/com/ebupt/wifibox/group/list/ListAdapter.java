package com.ebupt.wifibox.group.list;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
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
            new ViewHolderSon(convertView);
        }
        final ViewHolderSon holder = (ViewHolderSon) convertView.getTag();
        holder.delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        holder.edit_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });
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
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        VisitorsMSG visitorsMSG = list.get(groupPosition);
        holder.name.setText(visitorsMSG.getName());
        holder.passports.setText(visitorsMSG.getPassports());
        if (isExpanded) {
            holder.img.setImageResource(R.drawable.close);
        } else {
            holder.img.setImageResource(R.drawable.open);
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
        ImageView img;

        public ViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.group_list_item_name);
            passports = (TextView) view.findViewById(R.id.group_list_item_passport);
            img = (ImageView) view.findViewById(R.id.group_list_item_img);
            view.setTag(this);
        }
    }

    class ViewHolderSon {
        ImageView edit_img;
        ImageView delete_img;
        TextView edit;
        TextView delete;

        public ViewHolderSon(View view) {
            edit_img = (ImageView) view.findViewById(R.id.group_list_item_son_edit_img);
            delete_img = (ImageView) view.findViewById(R.id.group_list_item_son_delete_img);

            edit = (TextView) view.findViewById(R.id.group_list_item_son_edit);
            delete = (TextView) view.findViewById(R.id.group_list_item_son_delete);
            view.setTag(this);
        }
    }

    private void delete() {

    }

    private void edit() {

    }
}