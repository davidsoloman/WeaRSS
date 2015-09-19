package com.creativedrewy.wearss.models;

import android.content.Context;

import com.creativedrewy.wearss.R;

import de.psdev.licensesdialog.licenses.License;

/**
 * Creative Commons Attribution 4.0. This is the license used by Google Material design icons.
 */
public class CCAttribution40IntlLicense extends License {
    @Override
    public String getName() {
        return "Creative Commons Attribution 4.0 International";
    }

    @Override
    public String getSummaryText(Context context) {
        return getContent(context, R.raw.cca_40_summary);
    }

    @Override
    public String getFullText(Context context) {
        return "";
    }

    @Override
    public String getVersion() {
        return "4.0";
    }

    @Override
    public String getUrl() {
        return "http://creativecommons.org/licenses/by/4.0/";
    }
}
