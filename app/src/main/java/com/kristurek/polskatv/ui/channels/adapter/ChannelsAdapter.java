package com.kristurek.polskatv.ui.channels.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kristurek.polskatv.R;
import com.kristurek.polskatv.ui.channels.model.ChannelModel;
import com.kristurek.polskatv.ui.channels.model.GroupModel;
import com.kristurek.polskatv.util.FontHelper;

import java.io.Serializable;
import java.util.List;

public class ChannelsAdapter extends ArrayAdapter<Serializable> {

    public enum RowType {
        LIST_ITEM, HEADER_ITEM
    }

    public ChannelsAdapter(Activity context) {
        super(context, 0);
    }

    public void setList(List<Serializable> list) {
        clear();
        addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public int getViewTypeCount() {
        return RowType.values().length;
    }

    @Override
    public boolean isEnabled(int position) {
        if (getItem(position) instanceof ChannelModel)
            return true;
        else if (getItem(position) instanceof GroupModel)
            return false;
        else
            throw new RuntimeException("Wrong type item, make sure your using types correctly");
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position) instanceof ChannelModel)
            return RowType.LIST_ITEM.ordinal();
        else if (getItem(position) instanceof GroupModel)
            return RowType.HEADER_ITEM.ordinal();
        else
            throw new RuntimeException("Wrong type item, make sure your using types correctly");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Serializable item = getItem(position);

        if (item instanceof ChannelModel) {
            ChannelModel model = (ChannelModel) item;

            ChannelViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ChannelViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.channels_item, parent, false);
                viewHolder.nameView = convertView.findViewById(R.id.channels_item_name);
                viewHolder.titleView = convertView.findViewById(R.id.channels_item_title);
                viewHolder.timeView = convertView.findViewById(R.id.channels_item_time);
                viewHolder.iconView = convertView.findViewById(R.id.channels_item_icon);
                viewHolder.progressView = convertView.findViewById(R.id.channels_item_progressbar);

                FontHelper.setFont(inflater.getContext(), viewHolder.nameView, FontHelper.Header.H_1);
                FontHelper.setFont(inflater.getContext(), viewHolder.timeView, FontHelper.Header.H_3);
                FontHelper.setFont(inflater.getContext(), viewHolder.titleView, FontHelper.Header.H_2);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ChannelViewHolder) convertView.getTag();
            }

            viewHolder.iconView.setImageDrawable(model.getIcon());
            viewHolder.nameView.setText(model.getName());
            viewHolder.timeView.setText(model.getTime());
            viewHolder.titleView.setText(model.getLiveEpgTitle());
            viewHolder.progressView.setProgress(model.getLiveEpgProgress());

            return convertView;
        } else if (item instanceof GroupModel) {
            GroupModel model = (GroupModel) item;

            GroupViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new GroupViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.channels_header, parent, false);
                viewHolder.titleView = convertView.findViewById(R.id.channels_header_group_name);

                FontHelper.setFont(inflater.getContext(), viewHolder.titleView, FontHelper.Header.H_1);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (GroupViewHolder) convertView.getTag();
            }

            viewHolder.titleView.setText(model.getTitle());

            return convertView;
        } else
            throw new RuntimeException("Wrong type item, make sure your using types correctly");
    }

    static class GroupViewHolder {
        TextView titleView;
    }

    static class ChannelViewHolder {
        ImageView iconView;
        TextView nameView;
        TextView titleView;
        TextView timeView;
        ProgressBar progressView;
    }
}