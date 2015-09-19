package com.creativedrewy.wearss.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.creativedrewy.wearss.R;
import com.creativedrewy.wearss.models.WearFeedEntry;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Adapter to manage the list items showing the user's feeds
 */
public class FeedItemsListAdapter extends ArrayAdapter<WearFeedEntry> {
    private LayoutInflater mInflater;

    /**
     * Constructor
     */
    public FeedItemsListAdapter(Context context, WearFeedEntry[] entries) {
        super(context, R.layout.feed_list_item, entries);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.feed_list_item, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        final WearFeedEntry entry = getItem(position);
        holder.feedNameTextView.setText(entry.getFeedName());
        holder.feedUrlTextView.setText(entry.getFeedUrl());
        holder.removeImageView.setOnClickListener(new FeedItemDeleteListener(this, entry, getContext()));

        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.feedListItemTitleTextView) TextView feedNameTextView;
        @InjectView(R.id.feedListItemUrlTextView) TextView feedUrlTextView;
        @InjectView(R.id.removeImageView) ImageView removeImageView;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
