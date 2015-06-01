package com.ebupt.wifibox.message;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ebupt.wifibox.R;
import com.ebupt.wifibox.databases.MessageTable;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhaoqin on 6/1/15.
 */
public class MessageAdapter extends BaseAdapter{
    private Context context;
    private List<MessageTable> list;

    public MessageAdapter(Context context, List<MessageTable> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return 0;
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
            convertView = View.inflate(context, R.layout.message_item_layout, null);
            new ViewHolder(convertView);
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        MessageTable table = list.get(position);
        holder.time.setText(table.getTime());
        holder.content.setText(table.getContent());
        if (table.getStatus()) {
            holder.status.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            holder.status.setTextColor(context.getResources().getColor(R.color.message_content));
        }
        return convertView;
    }

    class ViewHolder {
        @InjectView(R.id.message_item_time)
        TextView time;
        @InjectView(R.id.message_item_content)
        TextView content;
        @InjectView(R.id.message_item_status)
        TextView status;
        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
            view.setTag(this);
        }
    }
}
