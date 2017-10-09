package com.gumapathi.codepath.twitteroauthclient.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gumapathi.codepath.twitteroauthclient.Fragments.ReplyTweetFragment;
import com.gumapathi.codepath.twitteroauthclient.Models.Tweet;
import com.gumapathi.codepath.twitteroauthclient.Models.User;
import com.gumapathi.codepath.twitteroauthclient.R;
import com.gumapathi.codepath.twitteroauthclient.TwitterApplication;
import com.gumapathi.codepath.twitteroauthclient.TwitterClient;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DetailActivity extends AppCompatActivity {
   @BindView(R.id.toolbar)
   Toolbar toolbar;
    @BindView(R.id.ivProfileImg)
    ImageView ivProfileImg;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvScreenName)
    TextView tvScreenName;
    @BindView(R.id.tvBody)
    TextView tvBody;
    @BindView(R.id.ivPhoto)
    ImageView ivPhoto;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvLikeCount)
    TextView tvLikeCount;
    @BindView(R.id.tvRetweetCount)
    TextView tvRetweetCount;
    @BindView(R.id.ivRetweetsCount)
    LikeButton ivRetweetsCount;
    @BindView(R.id.ivLike)
    LikeButton ivLike;
    @BindView(R.id.ivReply)
    LikeButton ivReply;

    TwitterClient client;

    private Tweet tweet;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);
        client = TwitterApplication.getRestClient();

        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        user = (User) Parcels.unwrap(getIntent().getParcelableExtra("user"));

        if (tweet != null) {
            Log.i("Tweetdetail", user.getName());
            loadViewItems(tweet, user);
        } else {
            Log.i("Tweetdetail", " tweet is null!");

        }
    }

    private void loadViewItems(final Tweet tweet, User user) {
        tvUserName.setText(user.getName());
        tvScreenName.setText(user.getScreenName());
        tvBody.setText(tweet.getBody());
        try {

            DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            DateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
            inputFormat.setLenient(true);

            Date twDate = inputFormat.parse(tweet.getCreatedAt());
            String outputText = outputFormat.format(twDate);

            Date newDate = new Date();
            long timeDiff = 0;
            String suffix = "m";
            timeDiff = getDateDiff(twDate, newDate, TimeUnit.MINUTES);
            boolean hour = false;
            if(timeDiff > 60) {
                timeDiff = getDateDiff(twDate, newDate, TimeUnit.HOURS);
                suffix = "h";
                hour = true;
            }
            if(timeDiff > 24 && hour) {
                timeDiff = getDateDiff(twDate, newDate, TimeUnit.DAYS);
                suffix = "d";
            }

            tvTime.setText(String.valueOf(timeDiff)+suffix);
        }
        catch (ParseException e) {
            Log.i("SAMY-dtParsEx ", e.getMessage());
            e.printStackTrace();
        }
        if(!user.getProfileImageURL().isEmpty()) {

            /*Glide.with(this)
                    .load(user.getProfileImageURL())
                    .bitmapTransform(new RoundedCornersTransformation(this,15, 0))
                    .into(ivProfileImg);*/
            Picasso.with(this)
                    .load(user.getProfileImageURL())
                    //.transform(new RoundedCornersTransformation(15, 0))
                    .into(ivProfileImg);
        }
        else {
            ivProfileImg.setImageResource(0);
        }
        ivPhoto.setImageResource(0);
        if (!tweet.getMediaUrl().isEmpty()) {

            /*Glide.with(this)
                    .load(tweet.getMediaUrl())
                    .bitmapTransform(new RoundedCornersTransformation(this,15, 0))
                    .into(ivPhoto);*/
            Picasso.with(this)
                    .load(tweet.getMediaUrl())
                    //.transform(new RoundedCornersTransformation(15, 0))
                    .into(ivPhoto);
        }
        tvRetweetCount.setText("");
        tvLikeCount.setText("");

        if (tweet.getRetweetCount() > 0) {
            tvRetweetCount.setText(String.valueOf(tweet.getRetweetCount()));
        }
        if(tweet.isRetweeted()){
            ivRetweetsCount.setLiked(true);

        }
        if (tweet.getFavouritesCount() > 0) {
            tvLikeCount.setText(String.valueOf(tweet.getFavouritesCount()));

        }
        if(tweet.isFavorited()){
            ivLike.setLiked(true);

        }

        ivLike.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                favorTweet(tweet, tvLikeCount, ivLike);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                favorTweet(tweet, tvLikeCount, ivLike);
            }
        });

        ivRetweetsCount.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                reTweet(tweet, tvRetweetCount, ivRetweetsCount);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                reTweet(tweet, tvRetweetCount, ivRetweetsCount);
            }
        });

        ivReply.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                replyToTweet();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                replyToTweet();
            }
        });
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    private void favorTweet(final Tweet tweet, final TextView tvFavorCount, final LikeButton ivFavor) {

        client.favorTweet(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("SAMY-fav", "favorited" + response.toString());
                try {
                    if(response.getBoolean("favorited")){
                        tweet.setFavouritesCount(Integer.parseInt(response.getString("favorite_count")));
                        ivLike.setLiked(true);
                    }else{
                        ivLike.setLiked(false);

                    }
                    tweet.setFavorited(response.getBoolean("favorited"));
                    if(response.getLong("favorite_count") > 0) {
                        tvFavorCount.setText(String.valueOf(response.getLong("favorite_count")));
                    }else{
                        tvFavorCount.setText("");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("SAMY-favex", responseString.toString() );
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("SAMY-favex", errorResponse.toString());

            }
        }, tweet.isFavorited(), tweet.getUid());

    }

    private void reTweet(final Tweet tweet, final TextView tvRetweetCount, final LikeButton ivRetweetCount) {

        client.reTweet(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("SAMY-rt", "retweeted" + response.toString());
                try {
                    if(response.getBoolean("retweeted")){
                        tweet.setRetweetCount(Integer.parseInt(response.getString("retweet_count")));
                        ivRetweetCount.setLiked(true);

                    }else {
                        ivRetweetCount.setLiked(false);
                    }
                    tweet.setRetweeted(response.getBoolean("retweeted"));
                    if(response.getLong("retweet_count") > 0) {
                        tvRetweetCount.setText(String.valueOf(response.getLong("retweet_count")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("SAMYrt-ex", responseString.toString() );

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("SAMY-rtex", errorResponse.toString() );

            }
        }, tweet.isRetweeted(), tweet.getUid());

    }

    public void replyToTweet() {

        ReplyTweetFragment myDialog = ReplyTweetFragment.newInstance(true, tweet, user);
        FragmentManager fm = getSupportFragmentManager();
        myDialog.show(fm, "reply tweet");
    }

    // pass context to Calligraphy
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }
}
