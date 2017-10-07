package com.gumapathi.codepath.twitteroauthclient.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.gumapathi.codepath.twitteroauthclient.Fragments.FavoriteFragment;
import com.gumapathi.codepath.twitteroauthclient.Fragments.MediaFragment;
import com.gumapathi.codepath.twitteroauthclient.Fragments.UserTimelineFragment;

/**
 * Created by gumapathi on 10/1/17.
 */

public class ProfileFragmentPagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 3;
        private String tabTitles[] = new String[] { "Tweets", "Media", "Likes"};

        private final Bundle fragmentBundle;

        public ProfileFragmentPagerAdapter(FragmentManager fm, Bundle bundle) {
            super(fm);
            Log.i("SAMY-", "ProfileFragmentPagerAdapter");
            this.fragmentBundle = bundle;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
                userTimelineFragment.setArguments(this.fragmentBundle);
                return userTimelineFragment;
            }
            else if (position == 1) {
                return new MediaFragment();
            }
            else if (position == 2) {
                FavoriteFragment favoriteFragment = new FavoriteFragment();
                favoriteFragment.setArguments(this.fragmentBundle);
                return favoriteFragment;
            }

            else {
                return null;
            }
        }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
