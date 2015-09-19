package com.creativedrewy.wearss.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.wearable.view.FragmentGridPagerAdapter;

import com.creativedrewy.wearss.fragment.ArticleCardFragment;
import com.creativedrewy.wearss.fragment.OpenOnPhoneFragment;
import com.creativedrewy.wearss.model.FeedHeadline;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter to render the headline pages
 */
public class HeadlineGridPagerAdapter extends FragmentGridPagerAdapter {
    private List<FeedHeadline> mHeadlines = new ArrayList<FeedHeadline>();
    private boolean mHasHeadlines = false;

    public boolean hasHeadlines() { return mHasHeadlines; }
    public void setHasHeadlines(boolean mHasHeadlines) { this.mHasHeadlines = mHasHeadlines; }

    /**
     * App currently has two rows - top is headlines, bottom is "open on phone"
     */
    @Override
    public int getRowCount() {
        return mHasHeadlines ? 2 : 1;
    }

    /**
     * Columns is the number of headlines sent to the device
     */
    @Override
    public int getColumnCount(int i) {
        return mHeadlines.size();
    }

    /**
     * Sync up the bottom and top column items for both rows
     */
    @Override
    public int getCurrentColumnForRow(int row, int currentColumn) {
        return currentColumn;
    }

    /**
     * Constructor
     */
    public HeadlineGridPagerAdapter(FragmentManager fragmentManager, List<FeedHeadline> headlines) {
        super(fragmentManager);
        mHeadlines = headlines;
    }

    @Override
    public Fragment getFragment(int row, int col) {
        Fragment returnFragment = null;

        if (row == 0) {
            FeedHeadline currentHeadline = mHeadlines.get(col);

            String timeFromNow = currentHeadline.hasPubDate() ? new PrettyTime().format(currentHeadline.getPostDate()) : "No publish date";
            String headlineWithDate = String.format("-- %s --\n%s\n\n%s", timeFromNow, currentHeadline.getHeadline(), currentHeadline.getArticleText());

//            returnFragment = CardFragment.create(currentHeadline.getSourceHost(), headlineWithDate);
//            ((CardFragment)returnFragment).setCardGravity(Gravity.BOTTOM);
//            ((CardFragment)returnFragment).setExpansionEnabled(true);
//            ((CardFragment)returnFragment).setExpansionDirection(CardFragment.EXPAND_DOWN);
            returnFragment = ArticleCardFragment.newInstance("hi", "How are you");
        } else if (row == 1) {
            returnFragment = OpenOnPhoneFragment.newInstance(mHeadlines.get(col));
        }

        return returnFragment;
    }
}