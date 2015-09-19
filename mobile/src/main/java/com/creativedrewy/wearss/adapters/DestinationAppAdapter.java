package com.creativedrewy.wearss.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.creativedrewy.wearss.R;
import com.creativedrewy.wearss.models.DestinationAppInfo;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Adapter to allow user to select app to share to
 */
public class DestinationAppAdapter extends ArrayAdapter<DestinationAppInfo> {
    private LayoutInflater mInflater;

    /**
     * Constructor
     */
    public DestinationAppAdapter(Context context, DestinationAppInfo[] entries) {
        super(context, R.layout.destination_app_item, entries);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;

        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.destination_app_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        DestinationAppInfo item = getItem(position);
        holder.destNameTextView.setText(item.getReadableName());
        holder.destIconImageView.setImageDrawable(item.getAppIcon());

        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.destAppIcon) ImageView destIconImageView;
        @InjectView(R.id.destAppName) TextView destNameTextView;

        ViewHolder(View view) { ButterKnife.inject(this, view); }
    }
}
