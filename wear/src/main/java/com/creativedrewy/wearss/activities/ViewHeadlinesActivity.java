package com.creativedrewy.wearss.activities;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.wearable.view.DismissOverlayView;
import android.support.wearable.view.GridViewPager;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.util.Log;

import com.creativedrewy.wearss.R;
import com.creativedrewy.wearss.model.FeedHeadline;
import com.creativedrewy.wearss.services.WearRSSDataListenerService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Wearable activity that allows user to swipe through headlines
 */
public class ViewHeadlinesActivity extends Activity {
    public static String PREFS_STORE = "WearRSS";
    public static String HEADLINES_DATA = "headlines_pref";

    private GridViewPager mHeadlinesPager;
    private DismissOverlayView mDismissOverlay;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_headlines);

        int notifyId = getIntent().getIntExtra(WearRSSDataListenerService.NOTIFICATION_ID_EXTRA, -1);
        if (notifyId > 0) {
            NotificationManagerCompat.from(this).cancel(notifyId);
        }

        mHeadlinesPager = (GridViewPager)findViewById(R.id.headlines_pager);

        mDismissOverlay = (DismissOverlayView) findViewById(R.id.dismiss_overlay);
        mDismissOverlay.setIntroText(R.string.close_instructions);
        mDismissOverlay.showIntroIfNecessary();

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            public void onLongPress(MotionEvent ev) {
                mDismissOverlay.show();
            }
        });

        mHeadlinesPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        List<FeedHeadline> headlineList = new ArrayList<FeedHeadline>();
        Gson gson = new Gson();
        boolean hasHeadlines = false;

        String headlineData = getSharedPreferences(PREFS_STORE, 0).getString(HEADLINES_DATA, "");

        //Deserialize headline data or setup the overview card for the empty data case
        if (!headlineData.equals("")) {
            headlineList = gson.fromJson(headlineData, new TypeToken<ArrayList<FeedHeadline>>(){}.getType());
            hasHeadlines = true;
        } else {
            FeedHeadline feedHeadline = new FeedHeadline(getString(R.string.setup_overview), getString(R.string.app_name));
            feedHeadline.setPostDate(new Date());
            feedHeadline.setArticleText("");
            headlineList.add(feedHeadline);
        }

        //Setup the headline swipe-through
        if (headlineList.size() > 0) {
            HeadlineGridPagerAdapter headlinesAdapter = new HeadlineGridPagerAdapter(getFragmentManager(), headlineList);
            headlinesAdapter.setHasHeadlines(hasHeadlines);
            mHeadlinesPager.setAdapter(headlinesAdapter);
        }
    }
}