package com.ebupt.wifibox.group.list;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.ebupt.wifibox.R;
import com.ebupt.wifibox.databases.VisitorsMSG;
import com.ebupt.wifibox.networks.Networks;


import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by zhaoqin on 4/21/15.
 */
public class ListAdapter extends BaseExpandableListAdapter{
    private Context context;
    private List<VisitorsMSG> list;
    private Dialog dialog;

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
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.group_list_item_son, null);
            new ViewHolderSon(convertView);
        }
        final VisitorsMSG visitorsMSG = list.get(groupPosition);
//        if (visitorsMSG.getGroupid().equals("head")) {
//            return null;
//        }
        final ViewHolderSon holder = (ViewHolderSon) convertView.getTag();

        holder.delete_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(groupPosition);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(groupPosition);
            }
        });

        holder.edit_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit(visitorsMSG.getName(), visitorsMSG.getPassports(), visitorsMSG);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit(visitorsMSG.getName(), visitorsMSG.getPassports(), visitorsMSG);
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
        if (visitorsMSG.getGroupid().equals("head")) {

            holder.head.setVisibility(View.VISIBLE);
            holder.content.setVisibility(View.GONE);
        } else {
            holder.head.setVisibility(View.GONE);
            holder.content.setVisibility(View.VISIBLE);
            holder.name.setText(visitorsMSG.getName());
            holder.passports.setText(visitorsMSG.getPassports());
            holder.brokerage.setText("返现金额:" + visitorsMSG.getBrokerage());
            if (isExpanded) {
                holder.img.setImageResource(R.drawable.close);
            } else {
                holder.img.setImageResource(R.drawable.open);
            }
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
        TextView brokerage;
        @InjectView(R.id.list_head)
        RelativeLayout head;
        @InjectView(R.id.list_content)
        RelativeLayout content;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
            name = (TextView) view.findViewById(R.id.group_list_item_name);
            passports = (TextView) view.findViewById(R.id.group_list_item_passport);
            img = (ImageView) view.findViewById(R.id.group_list_item_img);
            brokerage = (TextView) view.findViewById(R.id.group_list_item_brokerage);
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

    private void delete(final int groupPosition) {
        LayoutInflater inflaterDI = LayoutInflater.from(context);
        LinearLayout layout = (LinearLayout) inflaterDI.inflate(R.layout.delete_group_layout, null);
        dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        TextView title = (TextView) layout.findViewById(R.id.delete_title);
        title.setText("是否删除当前游客");

        Button ok = (Button) layout.findViewById(R.id.delete_group_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VisitorsMSG visitorsMSG = list.get(groupPosition);
                list.remove(groupPosition);
                Networks.delPassport(context, visitorsMSG.getGroupid(), visitorsMSG.getPassports());
                Intent intent = new Intent("deleteVisitor");
                context.sendBroadcast(intent);
                visitorsMSG.delete();
                dialog.hide();
            }
        });

        Button cancel = (Button) layout.findViewById(R.id.delete_group_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });
    }

    private void edit(String namestr, String passportstr, final VisitorsMSG visitorsMSG) {
        LayoutInflater inflaterDI = LayoutInflater.from(context);
        LinearLayout layout = (LinearLayout) inflaterDI.inflate(R.layout.add_visitor_layout, null);
        dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        final BootstrapEditText name = (BootstrapEditText) layout.findViewById(R.id.add_visitor_edit_name);
        final BootstrapEditText passport = (BootstrapEditText) layout.findViewById(R.id.add_visitor_edit_passport);
        name.setText(namestr);
        passport.setText(passportstr);

        Button ok = (Button) layout.findViewById(R.id.add_visitor_ok);
        ok.setBackgroundResource(R.drawable.btn_ok_background);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("")) {
                    Toast.makeText(context, "请输入姓名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (passport.getText().toString().equals("")) {
                    Toast.makeText(context, "请输入护照号", Toast.LENGTH_SHORT).show();
                    return;
                }

                visitorsMSG.setName(name.getText().toString());
                visitorsMSG.setPassports(passport.getText().toString());
                visitorsMSG.saveThrows();
                Intent intent = new Intent("updateVisitor");
                context.sendBroadcast(intent);
                Networks.updatePassport(context, visitorsMSG.getPassports_id(), visitorsMSG.getName(), visitorsMSG.getPassports());
                dialog.hide();
            }
        });

        Button cancel = (Button) layout.findViewById(R.id.add_visitor_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });

        ImageView passportButton = (ImageView) layout.findViewById(R.id.add_visitor_passport_button);
        passportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "当前不支持护照扫描", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }
}