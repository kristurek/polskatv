package com.kristurek.polskatv.ui.similarepgs.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kristurek.polskatv.R;
import com.kristurek.polskatv.ui.epgs.model.EpgModel;
import com.kristurek.polskatv.util.DateTimeHelper;
import com.kristurek.polskatv.util.FontHelper;

import java.util.List;

public class SimilarEpgsAdapter extends ArrayAdapter<EpgModel> {

    public SimilarEpgsAdapter(Activity context) {
        super(context, R.layout.similar_epgs_item);
    }

    public void setList(List<EpgModel> list) {
        clear();
        addAll(list);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView channelNameView;
        TextView descriptionView;
        TextView titleView;
        TextView dateTimeView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        EpgModel model = getItem(position);

        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            rowView = inflater.inflate(R.layout.similar_epgs_item, null);

            SimilarEpgsAdapter.ViewHolder viewHolder = new SimilarEpgsAdapter.ViewHolder();
            viewHolder.descriptionView = rowView.findViewById(R.id.similar_epgs_description);
            viewHolder.titleView = rowView.findViewById(R.id.similar_epgs_title);
            viewHolder.dateTimeView = rowView.findViewById(R.id.similar_epgs_datetime);
            viewHolder.channelNameView = rowView.findViewById(R.id.similar_epgs_channel_name);

            FontHelper.setFont(inflater.getContext(), viewHolder.titleView, FontHelper.Header.H_1);
            FontHelper.setFont(inflater.getContext(), viewHolder.channelNameView, FontHelper.Header.H_1);
            FontHelper.setFont(inflater.getContext(), viewHolder.descriptionView, FontHelper.Header.H_2);
            FontHelper.setFont(inflater.getContext(), viewHolder.dateTimeView, FontHelper.Header.H_2);

            rowView.setTag(viewHolder);
        }

        SimilarEpgsAdapter.ViewHolder holder = (SimilarEpgsAdapter.ViewHolder) rowView.getTag();
        holder.channelNameView.setText(model.getChannelName());
        holder.descriptionView.setText(model.getDescription());
        holder.dateTimeView.setText(DateTimeHelper.rangeUnixTimeToString(model.getBeginTime(), model.getEndTime()));
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