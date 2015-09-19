package com.creativedrewy.wearss.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.axelby.riasel.Feed;
import com.creativedrewy.wearss.R;
import com.creativedrewy.wearss.feedservice.FeedProcessingService;
import com.creativedrewy.wearss.feedservice.IFeedProcessedHandler;
import com.creativedrewy.wearss.helpers.ClipboardHelper;
import com.creativedrewy.wearss.models.WearFeedEntry;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Add a feed to be aggregated into the wear-pushed headlines list
 */
public class AddFeedFragment extends Fragment {
    private ClipboardHelper mClipboardHelper;

    @InjectView(R.id.feedUrlEditText) EditText mUrlEditText;
    @InjectView(R.id.statusMessageTextView) TextView mStatusTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_feed, container, false);
        ButterKnife.inject(this, view);

        mClipboardHelper = new ClipboardHelper(getActivity());
        mUrlEditText.setText(mClipboardHelper.getClipboardURL());

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_feed_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save_feed) {
            saveNewFeed();
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.clearTextImageView)
    protected void clearUrlText() {
        mUrlEditText.setText("");
    }

    /**
     * Attempt to save the new feed to the list
     */
    private void saveNewFeed() {
        String rssUrl = mUrlEditText.getText().toString();

        if (!rssUrl.equals("") && userNotAlreadyFollowing(rssUrl)) {
            getActivity().setProgressBarIndeterminateVisibility(true);

            FeedProcessingService service = new FeedProcessingService(getActivity(), mFeedHandler);
            service.getAndProcessFeed(rssUrl);
        }
    }

    /**
     * Check to see if the user is already following this feed URL
     */
    private boolean userNotAlreadyFollowing(String url) {
        mStatusTextView.setText("");

        if (WearFeedEntry.isFollowingUrl(url)) {
            mStatusTextView.setText(getActivity().getString(R.string.already_following_feed));
            return false;
        }

        return true;
    }

    /**
     * Does final steps after service has attempted to parse feed
     */
    private IFeedProcessedHandler mFeedHandler = new IFeedProcessedHandler() {
        @Override
        public void processSuccessful(final Feed feed) {
            getActivity().runOnUiThread(() -> {
                getActivity().setProgressBarIndeterminateVisibility(false);

                WearFeedEntry entry = new WearFeedEntry(feed.getTitle(), mUrlEditText.getText().toString());
                entry.save();

                String successMsg = String.format(getActivity().getString(R.string.add_feed_notify), feed.getTitle());
                Toast.makeText(getActivity(), successMsg, Toast.LENGTH_LONG).show();
                getActivity().finish();
            });
        }

        @Override
        public void errorProcessingFeed(Throwable throwable) {
            getActivity().runOnUiThread(() -> {
                getActivity().setProgressBarIndeterminateVisibility(false);
                mStatusTextView.setText(getActivity().getString(R.string.not_valid_rss_feed));
            });
        }
    };
}
