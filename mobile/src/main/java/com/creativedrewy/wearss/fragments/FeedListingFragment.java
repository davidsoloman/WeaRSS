package com.creativedrewy.wearss.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.creativedrewy.wearss.R;
import com.creativedrewy.wearss.adapters.FeedItemsListAdapter;
import com.creativedrewy.wearss.feedservice.RefreshHeadlinesService;
import com.creativedrewy.wearss.models.WearFeedEntry;
import com.creativedrewy.wearss.receivers.LocalSyncFinishedReceiver;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * List the feeds that will be checked and headlines sent to the wear device
 */
public class FeedListingFragment extends Fragment {
    private LocalSyncFinishedReceiver mSyncReceiver;

    @InjectView(R.id.feedItemsDisplayView)
    ListView mListView;

    @InjectView(R.id.noFeedsTextView)
    TextView mNoFeedsTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feeds_list, container, false);
        ButterKnife.inject(this, view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mSyncReceiver = new LocalSyncFinishedReceiver(getActivity());
        getActivity().registerReceiver(mSyncReceiver, new IntentFilter(getActivity().getString(R.string.action_local_sync_finished)));

        setupListView();
    }

    /**
     * Provide the set of entry items to the listview and set up the handling when data is changed
     */
    private void setupListView() {
        List<WearFeedEntry> feedItems = WearFeedEntry.listAll(WearFeedEntry.class);
        if (feedItems.size() > 0) {
            WearFeedEntry[] itemsArray = feedItems.toArray(new WearFeedEntry[feedItems.size()]);

            ListAdapter arrayAdapter = new FeedItemsListAdapter(getActivity(), itemsArray);
            arrayAdapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onInvalidated() {
                    setupListView();
                }
            });

            mListView.setAdapter(arrayAdapter);
            mNoFeedsTextView.setVisibility(View.GONE);
        } else {
            mListView.setAdapter(null);
            mNoFeedsTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mSyncReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh && mListView.getCount() > 0) {
            syncHeadlines();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Grab the latest feed headlines and send them to the wearable
     */
    private void syncHeadlines() {
        Intent intent = new Intent(getActivity(), RefreshHeadlinesService.class);
        getActivity().startService(intent);
        getActivity().setProgressBarIndeterminateVisibility(true);

        Toast.makeText(getActivity(), getActivity().getString(R.string.syncing_headlines_toast), Toast.LENGTH_SHORT).show();
    }
}
