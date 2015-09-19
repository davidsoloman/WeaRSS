package com.creativedrewy.wearss.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.BaseAdapter;

import com.creativedrewy.wearss.R;
import com.creativedrewy.wearss.models.WearFeedEntry;

/**
 * Click handler for "delete" button on each item in the feed listview; deletes the relevant
 * entry and notifies the listview/adapter to update itself
 */
public class FeedItemDeleteListener implements View.OnClickListener {
    private BaseAdapter mAdapter;
    private WearFeedEntry mEntry;
    private Context mContext;

    /**
     * Constructor
     */
    public FeedItemDeleteListener(BaseAdapter adapter, WearFeedEntry entry, Context context) {
        mAdapter = adapter;
        mEntry = entry;
        mContext = context;
    }

    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle(mContext.getString(R.string.remove_feed_title))
                .setMessage(mContext.getString(R.string.remove_feed_confirm_message))
                .setNegativeButton(android.R.string.no, (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
                    mEntry.delete();
                    mAdapter.notifyDataSetInvalidated();
                });

        builder.show();
    }
}
