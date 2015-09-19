package com.creativedrewy.wearss.models;

import android.content.Context;

import de.psdev.licensesdialog.licenses.License;

/**
 * Licenses dialog special class for Riasel
 */
public class RiaselLicense extends License {
    @Override
    public String getName() {
        return "Riasel";
    }

    @Override
    public String getSummaryText(Context context) {
        return "Original code base implemented by Dan Goldstein. No license specified. Library utilized and some modifications made by WeaRSS author.";
    }

    @Override
    public String getFullText(Context context) { return ""; }

    @Override
    public String getVersion() { return ""; }

    @Override
    public String getUrl() { return ""; }
}
