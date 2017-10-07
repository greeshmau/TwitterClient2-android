package com.gumapathi.codepath.twitteroauthclient.Activities;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.gumapathi.codepath.twitteroauthclient.Adapters.ProfileFragmentPagerAdapter;
import com.gumapathi.codepath.twitteroauthclient.Adapters.TimelineFragmentPagerAdapter;
import com.gumapathi.codepath.twitteroauthclient.Models.User;
import com.gumapathi.codepath.twitteroauthclient.R;
import com.gumapathi.codepath.twitteroauthclient.TwitterApplication;
import com.gumapathi.codepath.twitteroauthclient.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class ProfileActivity extends AppCompatActivity {
    TwitterClient client;
    @BindView(R.id.ivProfilePic)
    ImageView ivProfilePic;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvScreenName)
    TextView tvScreenName;
    @BindView(R.id.tvFollowers)
    TextView tvFollowers;
    @BindView(R.id.tvFollowing)
    TextView tvFollowing;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.ivHeaderPic)
    ImageView ivHeaderPic;
    @BindView(R.id.vpProfile)
    ViewPager viewPager;
    @BindView(R.id.tbLayout)
    TabLayout tabLayout;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        client = TwitterApplication.getRestClient();
        if (savedInstanceState == null) {
            //get screen name
            lookupUser(getIntent().getStringExtra("screen_name"));
            Log.i("SAMY-uin", getIntent().getStringExtra("screen_name"));
        }
        Bundle bundle = new Bundle();
        bundle.putString("screenName",getIntent().getStringExtra("screen_name"));
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager.setAdapter(new ProfileFragmentPagerAdapter(getSupportFragmentManager(), bundle));

        // Give the TabLayout the ViewPager
        tabLayout.setupWithViewPager(viewPager);
        Log.i("SAMY-", "set up view page");

    }

    private void lookupUser(String screenName) {
        Log.i("SAMY-lookupUser", screenName);
        client.lookupUser(screenName, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.i("SAMY", "lookupUser-success" + response.toString());
                try {
                    user = User.fromJSONWithDBSave(response);
                }
                catch (Exception e) {
                    Log.i("SAMY", "exception" + e.getMessage());
                }
                setupView();

            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.i("SAMY", "lookupUser-success" + response.toString());
                try {
                    user = User.fromJSONWithDBSave((JSONObject)response.get(0));
                }
                catch (Exception e) {
                    Log.i("SAMY", "exception" + e.getMessage());
                }
                setupView();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.i("SAMY", "onFailure" + responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.i("SAMY", "onFailure" + errorResponse.toString());
            }
        });
    }

    private void setupView() {
        String screenName = user.getScreenName();
        //UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);
        //display user fragment dynamically within this activity
        //FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //ft.replace(R.id.flContainer, fragmentUserTimeline);
        //ft.commit();

        collapsingToolbar.setTitle(user.getName());

        tvName.setText(user.getName());
        tvScreenName.setText(user.getScreenName());
        tvName.setText(user.getName());
        //tvDescription.setText(user.getDescription());
        String followers = "Followers " + String.valueOf(user.getFollowersCount());
        String following = "Following " + String.valueOf(user.getFollowingCount());
        tvFollowers.setText(followers);
        tvFollowing.setText(following);

        Log.i("SAMY-getProfileImageU", user.getFollowersCount() +" " +user.getProfileImageURL());
        Log.i("SAMY-getHeaderImageURL", user.getFollowingCount() +" " +user.getHeaderImageURL());

        Picasso.with(getApplicationContext())
                .load(user.getProfileImageURL())
                //.transform(new RoundedCornersTransformation(30, 0, RoundedCornersTransformation.CornerType.ALL))
                .into(ivProfilePic);
        Picasso.with(getApplicationContext())
                .load(user.getHeaderImageURL())
                //.transform(new RoundedCornersTransformation(30, 0, RoundedCornersTransformation.CornerType.ALL))
                .into(ivHeaderPic);
    }

}
