package com.gumapathi.codepath.twitteroauthclient.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gumapathi.codepath.twitteroauthclient.Adapters.TimelineFragmentPagerAdapter;
import com.gumapathi.codepath.twitteroauthclient.R;
import com.gumapathi.codepath.twitteroauthclient.TwitterApplication;
import com.gumapathi.codepath.twitteroauthclient.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by gumapathi on 10/1/17.
 */

public class TabbedLayoutActivity extends AppCompatActivity  {
    private int[] imageResId = {
            R.drawable.ic_home_black_24dp,
            R.drawable.like_black};
    ImageView ivProfilePhoto;
    private TwitterClient client;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_layout);
        client = TwitterApplication.getRestClient();
        Log.i("SAMY-", "inflated view activity");

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new TimelineFragmentPagerAdapter(getSupportFragmentManager(),
                TabbedLayoutActivity.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        Log.i("SAMY-", "set up view page");

        for (int i = 0; i < imageResId.length; i++) {
            tabLayout.getTabAt(i).setIcon(imageResId[i]);
        }
        setupProfileImage();
        Log.i("SAMY-", "set up profile image");


    }

    private void setupProfileImage() {
        client.getUserProfile(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String profileImageUrl = response.getString("profile_image_url");
                    ivProfilePhoto = (ImageView) findViewById(R.id.ivProfilePhoto);
                    Glide.with(getApplicationContext())
                            .load(profileImageUrl)
                            .bitmapTransform(new RoundedCornersTransformation(getApplicationContext(), 60, 0))
                            .into(ivProfilePhoto);
                    final String screenName = response.getString("screen_name");
                    ivProfilePhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent profileIntent = new Intent(v.getContext(), ProfileActivity.class);
                            profileIntent.putExtra("screen_name",screenName);
                            startActivity(profileIntent);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

    }
}

