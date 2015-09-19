package com.creativedrewy.wearss.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.creativedrewy.wearss.R;
import com.creativedrewy.wearss.wearable.SendHeadline;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Adapter to show all of the items the user has set to read later
 */
public class ReadListAdapter extends ArrayAdapter<SendHeadline> {
    private LayoutInflater mInflater;
    private SendHeadline[] mDataSet;

    public SendHeadline[] getAllHeadlines() { return mDataSet; }

    public List<SendHeadline> getSelectedHeadlines() {
        ArrayList<SendHeadline> selectedHeadlines = new ArrayList<>();

        if (getAllHeadlines() != null) {
            for (SendHeadline headline : getAllHeadlines()) {
                if (headline.isItemSelected()) {
                    selectedHeadlines.add(headline);
                }
            }
        }

        return selectedHeadlines;
    }

    /**
     * Constructor
     */
    public ReadListAdapter(Context context, SendHeadline[] entries) {
        super(context, R.layout.read_list_item, entries);

        mDataSet = entries;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.read_list_item, parent, false);
            holder = new ViewHolder(view);
            holder.readItemCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int pos = (Integer) buttonView.getTag();
                getItem(pos).setItemSelected(buttonView.isChecked());
            });

            view.setTag(holder);
        }

        final SendHeadline headline = getItem(position);
        holder.articleTitleTextView.setText(headline.getHeadline());
        holder.articleSourceTextView.setText(headline.getSourceHost());

        holder.readItemCheckBox.setTag(position);
        holder.readItemCheckBox.setChecked(headline.isItemSelected());

        return view;
    }

    static class ViewHolder {
        @InjectView(R.id.readItemSelectCheckBox) CheckBox readItemCheckBox;
        @InjectView(R.id.articleTitleTextView) TextView articleTitleTextView;
        @InjectView(R.id.articleSourceTextView) TextView articleSourceTextView;

        ViewHolder(View view) { ButterKnife.inject(this, view); }
    }
}
