package com.gumapathi.codepath.twitteroauthclient.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.gumapathi.codepath.twitteroauthclient.Adapters.ProfileFragmentPagerAdapter;
import com.gumapathi.codepath.twitteroauthclient.Models.User;
import com.gumapathi.codepath.twitteroauthclient.R;
import com.gumapathi.codepath.twitteroauthclient.TwitterApplication;
import com.gumapathi.codepath.twitteroauthclient.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


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
    //@BindView(R.id.collapsing_toolbar)
    //CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.ivHeaderPic)
    ImageView ivHeaderPic;
    @BindView(R.id.vpProfile)
    ViewPager viewPager;
    //@BindView(R.id.tbLayout)
    //TabLayout tabLayout;
    @BindView(R.id.tvDescription)
    TextView tvDescription;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        //final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
        //tabLayout.setupWithViewPager(viewPager);
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tbLayout);
        //attach the tab to viewpager
        tabStrip.setViewPager(viewPager);
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

        //collapsingToolbar.setTitle(user.getName());

        tvName.setText(user.getName());
        tvScreenName.setText(user.getScreenName());
        tvName.setText(user.getName());
        tvDescription.setText(user.getDescription());
        String followers = "Followers " + String.valueOf(user.getFollowersCount());
        String following = "Following " + String.valueOf(user.getFollowingCount());
        tvFollowers.setText(followers);
        tvFollowing.setText(following);

        Log.i("SAMY-getProfileImageU", followers);
        Log.i("SAMY-getHeaderImageURL", following);

        Picasso.with(getApplicationContext())
                .load(user.getProfileImageURL())
                //.transform(new RoundedCornersTransformation(30, 0, RoundedCornersTransformation.CornerType.ALL))
                .into(ivProfilePic);
        if(!user.getHeaderImageURL().isEmpty()) {
            Picasso.with(getApplicationContext())
                    .load(user.getHeaderImageURL())
                    //.transform(new RoundedCornersTransformation(30, 0, RoundedCornersTransformation.CornerType.ALL))
                    .into(ivHeaderPic);
        }
    }

    public void onFollowingCountClick(View view) {
    }

    public void onFollowersCountClick(View view) {
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
