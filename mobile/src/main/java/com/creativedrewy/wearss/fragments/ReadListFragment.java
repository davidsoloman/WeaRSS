package com.creativedrewy.wearss.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.creativedrewy.wearss.R;
import com.creativedrewy.wearss.activities.SettingsActivity;
import com.creativedrewy.wearss.adapters.ReadListAdapter;
import com.creativedrewy.wearss.helpers.AlertHelper;
import com.creativedrewy.wearss.services.DeviceAppService;
import com.creativedrewy.wearss.services.HeadlinesService;
import com.creativedrewy.wearss.wearable.SendHeadline;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;

/**
 * Fragment listing all of the articles the user has marked for reading
 */
public class ReadListFragment extends Fragment {
    private ReadListAdapter mListAdapter;
    private SharedPreferences mSharedPrefs;
    private DeviceAppService mAppService;

    @InjectView(R.id.selectAllCheckBox)
    CheckBox mSelectToggleCheckBox;

    @InjectView(R.id.readListItemListView)
    ListView mListView;

    @InjectView(R.id.noReadItemsTextView)
    TextView mNoReadItemsTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_read_list, container, false);
        ButterKnife.inject(this, view);

        mAppService = new DeviceAppService();

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setupList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_open_selected_in_browser) {
            performMultiSelectTapAction();
        } else if (item.getItemId() == R.id.action_delete_read_items) {
            deleteSelectedReadItems();
        }

        return super.onOptionsItemSelected(item);
    }

    @OnCheckedChanged(R.id.selectAllCheckBox)
    void onSelectionCheckBoxChange(CompoundButton buttonView, boolean isChecked) {
        buttonView.setText(getActivity().getString(isChecked ? R.string.select_none : R.string.select_all));

        SendHeadline[] allHeadlines = mListAdapter.getAllHeadlines();
        for (SendHeadline headline : allHeadlines) {
            headline.setItemSelected(isChecked);
        }

        mListAdapter.notifyDataSetChanged();
    }

    public void setupList() {
        List<SendHeadline> savedHeadlines = HeadlinesService.getSavedHeadlines();
        if (savedHeadlines.size() > 0) {
            SendHeadline[] itemsArray = savedHeadlines.toArray(new SendHeadline[savedHeadlines.size()]);
            if (getActivity() == null) return;

            mListAdapter = new ReadListAdapter(getActivity(), itemsArray);
            mListView.setAdapter(mListAdapter);
            mListView.setOnItemClickListener((parent, view, position, id) -> {
                SendHeadline selectedArticle = mListAdapter.getItem(position);
                performReadListTapAction(selectedArticle);
            });

            mNoReadItemsTextView.setVisibility(View.GONE);
            mSelectToggleCheckBox.setEnabled(true);
        } else {
            mListView.setAdapter(null);
            mSelectToggleCheckBox.setEnabled(false);
            mNoReadItemsTextView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Perform the read list item tap action based on the shared preferences on one item
     */
    private void performReadListTapAction(SendHeadline readListArticle) {
        String pref = mSharedPrefs.getString(getActivity().getString(R.string.key_prefs_read_item_tap), "0");

        if (pref.equals(SettingsActivity.TAP_OPEN_BROWSER_VALUE)) {
            openHeadlineInBrowser(readListArticle);
        } else if (pref.equals(SettingsActivity.TAP_SHARE_CHOOSER_VALUE)) {
            Intent intent = mAppService.getGeneralShareIntent(readListArticle.getArticleUrl());
            getActivity().startActivity(Intent.createChooser(intent, getActivity().getString(R.string.share_to_title)));
        } else if (pref.equals(SettingsActivity.TAP_SPECIFIC_APP_VALUE)) {
            mAppService.shareToSpecificSelectedApp(getActivity(), readListArticle);
        }
    }

    /**
     * Open the headline's article URL in the browser
     */
    private void openHeadlineInBrowser(SendHeadline headline) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(headline.getArticleUrl()));
        startActivity(intent);
    }

    /**
     * Perform the tap action on one or more selected read list items
     */
    private void performMultiSelectTapAction() {
        if (mListAdapter == null) return;

        List<SendHeadline> selectedHeadlines = mListAdapter.getSelectedHeadlines();
        if (selectedHeadlines.size() < 1) {
            AlertHelper.showOkDialog(getActivity(), R.string.no_items_selected_title, R.string.please_select_items_tap_action);
        } else {
            String pref = mSharedPrefs.getString(getActivity().getString(R.string.key_prefs_read_item_tap), "0");
            if (pref.equals(SettingsActivity.TAP_OPEN_BROWSER_VALUE) || pref.equals(SettingsActivity.TAP_SPECIFIC_APP_VALUE)) {
                for (SendHeadline headline : selectedHeadlines) {
                    performReadListTapAction(headline);
                }
            }
        }
    }

    /**
     * Delete the selected read list items
     */
    private void deleteSelectedReadItems() {
        if (mListAdapter == null) return;

        List<SendHeadline> selectedHeadlines = mListAdapter.getSelectedHeadlines();
        if (selectedHeadlines.size() < 1) {
            AlertHelper.showOkDialog(getActivity(), R.string.delete_article_title, R.string.select_articles_to_delete);
        } else {
            AlertHelper.showYesNoDialog(getActivity(), R.string.confirm_article_delete, R.string.confirm_article_delete_message, (dialogInterface, i) -> {
                for (SendHeadline headline : selectedHeadlines) {
                    headline.delete();
                }

                mSelectToggleCheckBox.setChecked(false);
                mSelectToggleCheckBox.setText(getActivity().getString(R.string.select_all));
                setupList();
            });
        }
    }
}
