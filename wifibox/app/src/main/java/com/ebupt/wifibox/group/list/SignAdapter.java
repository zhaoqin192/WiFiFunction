package com.ebupt.wifibox.group.list;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.ebupt.wifibox.R;
import com.ebupt.wifibox.databases.DownVisitorMSG;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhaoqin on 4/23/15.
 */
public class SignAdapter extends BaseExpandableListAdapter{
    private Context context;
    private List<DownVisitorMSG> list;
    private Dialog dialog;

    public SignAdapter(Context context, List<DownVisitorMSG> list) {
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
            convertView = View.inflate(context, R.layout.group_list_sign_item_son, null);
            new ViewHolderSon(convertView);
        }
        final ViewHolderSon holder = (ViewHolderSon) convertView.getTag();
        final DownVisitorMSG downVisitorMSG = list.get(groupPosition);
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
                edit(downVisitorMSG.getName(), downVisitorMSG.getPhone(), downVisitorMSG);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit(downVisitorMSG.getName(), downVisitorMSG.getPhone(), downVisitorMSG);
            }
        });

        holder.phone_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact(downVisitorMSG.getPhone());
            }
        });
        holder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact(downVisitorMSG.getPhone());
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
        DownVisitorMSG downVisitorMSG = list.get(groupPosition);
        holder.name.setText(downVisitorMSG.getName());
        holder.phone.setText(downVisitorMSG.getPhone());
        if (downVisitorMSG.getStatus().equals("online")) {
            holder.status.setTextColor(context.getResources().getColor(R.color.green));
            holder.status.setText("在线");
        }
        if (downVisitorMSG.getStatus().equals("offline")) {
            holder.status.setTextColor(context.getResources().getColor(R.color.red));
            holder.status.setText("不在线");
        }
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
        @InjectView(R.id.group_list_item_name) TextView name;
        @InjectView(R.id.group_list_item_passport) TextView phone;
        @InjectView(R.id.group_list_item_brokerage) TextView status;
        @InjectView(R.id.group_list_item_img) ImageView img;
        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
            view.setTag(this);
        }
    }

    class ViewHolderSon {
        ImageView edit_img;
        ImageView delete_img;
        ImageView phone_img;
        TextView edit;
        TextView delete;
        TextView phone;


        public ViewHolderSon(View view) {
            edit_img = (ImageView) view.findViewById(R.id.group_list_sign_item_son_edit_img);
            delete_img = (ImageView) view.findViewById(R.id.group_list_sign_item_son_delete_img);
            phone_img = (ImageView) view.findViewById(R.id.group_list_sign_item_son_phone_img);

            edit = (TextView) view.findViewById(R.id.group_list_sign_item_son_edit);
            delete = (TextView) view.findViewById(R.id.group_list_sign_item_son_delete);
            phone = (TextView) view.findViewById(R.id.group_list_sign_item_son_phone);
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
                DownVisitorMSG downVisitorMSG = list.get(groupPosition);
                downVisitorMSG.delete();
                list.remove(groupPosition);
                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent("deleteDownVisitor");
                context.sendBroadcast(intent);
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

    private void edit(String namestr, String phone, final DownVisitorMSG visitorsMSG) {
        LayoutInflater inflaterDI = LayoutInflater.from(context);
        LinearLayout layout = (LinearLayout) inflaterDI.inflate(R.layout.modified_visitor_layout, null);
        dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        final BootstrapEditText name = (BootstrapEditText) layout.findViewById(R.id.modified_visitor_edit_name);
        final BootstrapEditText passport = (BootstrapEditText) layout.findViewById(R.id.modified_visitor_edit_phone);
        name.setText(namestr);
        passport.setText(phone);

        Button ok = (Button) layout.findViewById(R.id.modified_visitor_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("")) {
                    Toast.makeText(context, "请输入姓名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (passport.getText().toString().equals("")) {
                    Toast.makeText(context, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }

                visitorsMSG.setName(name.getText().toString());
                visitorsMSG.setPhone(passport.getText().toString());
                visitorsMSG.saveThrows();
                Intent intent = new Intent("updateDownVisitor");
                context.sendBroadcast(intent);

                Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
                dialog.hide();
            }
        });

        Button cancel = (Button) layout.findViewById(R.id.modified_visitor_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });
    }

    private void contact(String phone) {
        Uri uri = Uri.parse("tel:" + phone);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(intent);
    }
}
