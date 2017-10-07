package com.gumapathi.codepath.twitteroauthclient.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.gumapathi.codepath.twitteroauthclient.Fragments.FavoriteFragment;
import com.gumapathi.codepath.twitteroauthclient.Fragments.HomeFragment;
import com.gumapathi.codepath.twitteroauthclient.Fragments.MentionsFragment;

/**
 * Created by gumapathi on 10/1/17.
 */

public class TimelineFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = new String[] { "Tab1", "Tab2"};

        private Context context;

        public TimelineFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            Log.i("SAMY-", "TimelineFragmentPagerAdapter");
            this.context = context;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeFragment();
            }
            else if (position == 1) {
                return new MentionsFragment();
            }
            else {
                return null;
            }
        }
}
