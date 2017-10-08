package com.gumapathi.codepath.twitteroauthclient.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.astuetz.PagerSlidingTabStrip;
import com.gumapathi.codepath.twitteroauthclient.Fragments.DirectMessageFragement;
import com.gumapathi.codepath.twitteroauthclient.Fragments.HomeFragment;
import com.gumapathi.codepath.twitteroauthclient.Fragments.MentionsFragment;
import com.gumapathi.codepath.twitteroauthclient.R;

/**
 * Created by gumapathi on 10/1/17.
 */

public class TimelineFragmentPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider  {
    final int PAGE_COUNT = 3;
    private String tabText[] = {"Home", "Mentions", "Messages"};
    private int tabIcons[] = {R.drawable.home, R.drawable.at, R.drawable.dm};

    private Context context;

    public TimelineFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        Log.i("SAMY-", "TimelineFragmentPagerAdapter");
        this.context = context;
    }

    @Override
    public int getPageIconResId(int position) {
        return tabIcons[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HomeFragment();
        } else if (position == 1) {
            return new MentionsFragment();
        } else if (position == 2) {
            return new DirectMessageFragement();
        } else {
            return null;
        }
    }
}
