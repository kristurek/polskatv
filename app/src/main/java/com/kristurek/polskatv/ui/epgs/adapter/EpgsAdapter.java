package com.kristurek.polskatv.ui.epgs.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kristurek.polskatv.R;
import com.kristurek.polskatv.ui.epgs.model.EpgModel;
import com.kristurek.polskatv.util.FontHelper;

import java.util.List;

public class EpgsAdapter extends ArrayAdapter<EpgModel> {

    public EpgsAdapter(Activity context) {
        super(context, R.layout.epgs_item);
    }

    public void setList(List<EpgModel> list) {
        clear();
        addAll(list);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        ImageView playView;
        TextView descriptionView;
        TextView titleView;
        TextView timeView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        EpgModel model = getItem(position);

        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            rowView = inflater.inflate(R.layout.epgs_item, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.descriptionView = rowView.findViewById(R.id.epgs_item_description);
            viewHolder.titleView = rowView.findViewById(R.id.epgs_item_title);
            viewHolder.timeView = rowView.findViewById(R.id.epgs_item_time);
            viewHolder.playView = rowView.findViewById(R.id.epgs_item_play);

            FontHelper.setFont(inflater.getContext(), viewHolder.titleView, FontHelper.Header.H_1);
            FontHelper.setFont(inflater.getContext(), viewHolder.timeView, FontHelper.Header.H_1);
            FontHelper.setFont(inflater.getContext(), viewHolder.descriptionView, FontHelper.Header.H_2);

            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.playView.setImageResource(model.getIcon());
        holder.descriptionView.setText(model.getDescription());
        holder.timeView.setText(model.getTime());
        holder.titleView.setText(model.getTitle());

        return rowView;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }
}