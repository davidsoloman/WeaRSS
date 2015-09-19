package com.creativedrewy.wearss.activities;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.Window;

import com.creativedrewy.wearss.R;
import com.creativedrewy.wearss.fragments.AddFeedFragment;

/**
 * Add a feed to be aggregated into the wear-pushed headlines list via the add fragment
 */
public class AddFeedActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_add_feed);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new AddFeedFragment()).commit();
        }
    }
}