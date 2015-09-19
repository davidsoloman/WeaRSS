package com.creativedrewy.wearss.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.commonsware.cwac.wakeful.WakefulIntentService;
import com.creativedrewy.wearss.R;
import com.creativedrewy.wearss.feedservice.SyncAlarmListenerService;
import com.creativedrewy.wearss.fragments.ChooseArticleOpenFragment;
import com.creativedrewy.wearss.models.CCAttribution40IntlLicense;
import com.creativedrewy.wearss.models.DestinationAppInfo;
import com.creativedrewy.wearss.models.RiaselLicense;
import com.creativedrewy.wearss.models.WearFeedEntry;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.MITLicense;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;

/**
 * Manage the settings for the app
 */
public class SettingsActivity extends PreferenceActivity {
    public static String TAP_DO_NOTHING_VALUE = "1";
    public static String TAP_OPEN_BROWSER_VALUE = "0";
    public static String TAP_SHARE_CHOOSER_VALUE = "2";
    public static String TAP_SPECIFIC_APP_VALUE = "3";

    private LicensesDialog mLicenseDialog;
    private Preference mReadlistTapPref;
    private CheckBoxPreference mArticleFromWatchPref;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        setupLicenseDialog();

        addPreferencesFromResource(R.xml.pref_data_sync);

        String refreshKey = getString(R.string.key_prefs_refresh_frequency);
        String headlineCountKey = getString(R.string.key_prefs_headlines_count);
        String readListTapKey = getString(R.string.key_prefs_read_item_tap);

        Preference refreshPref = findPreference(refreshKey);
        Preference headlinePref = findPreference(headlineCountKey);
        mReadlistTapPref = findPreference(readListTapKey);
        mArticleFromWatchPref = (CheckBoxPreference) findPreference(getString(R.string.key_prefs_article_select_from_watch));
        Preference samplePrefButton = findPreference(getString(R.string.key_prefs_sample_feeds));
        Preference ossPrefButton = findPreference(getString(R.string.key_prefs_licenses));

        //Setup the pref display with the default value
        setupPrefDisplay(refreshPref, PreferenceManager.getDefaultSharedPreferences(this).getString(refreshKey, ""));
        setupPrefDisplay(headlinePref, PreferenceManager.getDefaultSharedPreferences(this).getString(headlineCountKey, ""));
        setupReadListPrefDisplay(PreferenceManager.getDefaultSharedPreferences(this).getString(readListTapKey, TAP_OPEN_BROWSER_VALUE), null);

        refreshPref.setOnPreferenceChangeListener(mBindPrefsListener);
        headlinePref.setOnPreferenceChangeListener(mBindPrefsListener);
        mReadlistTapPref.setOnPreferenceChangeListener(mReadListTapListener);

        samplePrefButton.setOnPreferenceClickListener(preference -> {
            String cnnUrl = "http://rss.cnn.com/rss/cnn_topstories.rss";
            String nprUIrl = "http://www.npr.org/rss/rss.php?id=1001";
            String engadgetUrl = "http://www.engadget.com/rss.xml";

            if (!WearFeedEntry.isFollowingUrl(cnnUrl)) new WearFeedEntry("CNN.com - Top Stories", cnnUrl).save();
            if (!WearFeedEntry.isFollowingUrl(engadgetUrl)) new WearFeedEntry("Engadget", engadgetUrl).save();
            if (!WearFeedEntry.isFollowingUrl(nprUIrl)) new WearFeedEntry("News", nprUIrl).save();

            Toast.makeText(SettingsActivity.this, getString(R.string.sample_add_success), Toast.LENGTH_SHORT).show();
            return false;
        });

        ossPrefButton.setOnPreferenceClickListener(preference -> {
            mLicenseDialog.show();
            return false;
        });
    }

    /**
     * A preference value change listener that updates the preference's summary to reflect its new value.
     */
    private Preference.OnPreferenceChangeListener mBindPrefsListener = (preference, value) -> {
        setupPrefDisplay(preference, value);

        if (preference.getKey().equals(preference.getContext().getString(R.string.key_prefs_refresh_frequency))) {
            WakefulIntentService.scheduleAlarms(new SyncAlarmListenerService(), preference.getContext());
        }

        return true;
    };

    private Preference.OnPreferenceChangeListener mReadListTapListener = (pref, value) -> {
        if (value.toString().equals(TAP_SPECIFIC_APP_VALUE)) {
            ChooseArticleOpenFragment chooseArticleOpenFragment = new ChooseArticleOpenFragment();
            chooseArticleOpenFragment.setAppChoose(appInfo -> {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
                editor.putString(getString(R.string.key_stored_specific_share_app), appInfo.serialize());
                editor.commit();

                setupReadListPrefDisplay(value.toString(), appInfo);
                Toast.makeText(this, getString(R.string.articles_shared_notify) + appInfo.getReadableName(), Toast.LENGTH_LONG).show();
            });

            chooseArticleOpenFragment.show(getFragmentManager(), "item");
        } else {
            mArticleFromWatchPref.setChecked(false);
            setupReadListPrefDisplay(value.toString(), null);
        }

        return true;
    };

    /**
     * Setup the read list tap pref display
     * @param value The preference value that dictates what text to display
     */
    protected void setupReadListPrefDisplay(String value, DestinationAppInfo appInfo) {
        String displayText = getListItemByValue((ListPreference) mReadlistTapPref, value);

        if (appInfo == null) {
            String json = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.key_stored_specific_share_app), "");
            if (!json.equals(""))
                appInfo = DestinationAppInfo.deserialize(json);
        }

        if (value.equals(TAP_SPECIFIC_APP_VALUE)) {
            mArticleFromWatchPref.setEnabled(true);
            displayText += ": " + appInfo.getReadableName();
        } else {
            mArticleFromWatchPref.setEnabled(false);
        }

        mReadlistTapPref.setSummary(displayText);
    }

    /**
     * Show the selected value of a given pref on the UI
     */
    protected void setupPrefDisplay(Preference preference, Object value) {
        if (preference instanceof ListPreference) {
            preference.setSummary(getListItemByValue((ListPreference) preference, value.toString()));
        } else {
            preference.setSummary(value.toString());
        }
    }

    /**
     * Get the display text for a selected option from a list preference by a value
     */
    protected String getListItemByValue(ListPreference pref, String value) {
        int index = pref.findIndexOfValue(value);
        return index >= 0 ? (pref.getEntries()[index]).toString() : "";
    }

    /**
     * Setup the dialog with all the OSS licenses utilized in the app
     */
    private void setupLicenseDialog() {
        final Notices notices = new Notices();

        notices.addNotice(new Notice("Riasel", "https://github.com/thasmin/Riasel", "Dan Goldstein", new RiaselLicense()));
        notices.addNotice(new Notice("Material Design Icons", "https://github.com/google/material-design-icons", "Google", new CCAttribution40IntlLicense()));
        notices.addNotice(new Notice("Butterknife", "http://jakewharton.github.io/butterknife", "Jake Wharton", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("RxAndroid", "https://github.com/ReactiveX/RxAndroid", "Netflix", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("Google Gson", "https://code.google.com/p/google-gson", "Google", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("OkHttp", "http://square.github.io/okhttp", "Square", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("CWAC Wakeful", "http://commonsware.com/cwac", "commonsguy", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("Gradle Retrolambda Plugin", "https://github.com/evant/gradle-retrolambda", "Evan Tatarka", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("Android PagerSlidingTabStrip", "https://github.com/jpardogo/PagerSlidingTabStrip", "Andreas Stuetz", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("Sugar ORM", "http://satyan.github.com/sugar", "Satya Narayan", new MITLicense()));
        notices.addNotice(new Notice("jsoup", "http://http://jsoup.org", "Jonathan Hedley", new MITLicense()));

        LicensesDialog.Builder builder = new LicensesDialog.Builder(this);
        builder.setNotices(notices);
        builder.setIncludeOwnLicense(true);
        mLicenseDialog = builder.build();
    }
}