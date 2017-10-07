package com.gumapathi.codepath.twitteroauthclient.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gumapathi.codepath.twitteroauthclient.Models.Tweet;
import com.gumapathi.codepath.twitteroauthclient.Models.Tweet_Table;
import com.gumapathi.codepath.twitteroauthclient.R;
import com.gumapathi.codepath.twitteroauthclient.TwitterApplication;
import com.gumapathi.codepath.twitteroauthclient.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

import static com.gumapathi.codepath.twitteroauthclient.TwitterClient.TWEET_COUNT;
import static com.gumapathi.codepath.twitteroauthclient.Utils.Utils.checkForInternet;

/**
 * Created by gumapathi on 10/1/17.
 */

public class HomeFragment extends TweetsDisplayFragment implements ComposeTweetDialogFragment.ComposeTweetDialogListener{

    boolean startOfOldTweets = false;
    boolean endOfOldTweets = false;
    private TwitterClient client;
    boolean noNewTweets = true;
    public static final String ARG_PAGE = "ARG_PAGE";
    ComposeTweetDialogFragment composeTweetDialogFragment;

    public static HomeFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        Log.i("SAMY-", "inflated view fragment");


        populateTimeline(0, false);

        return view;
    }*/

    public void populateTimeline(final int page, final boolean refreshing) {
        Log.i("SAMY-", "poptime");
        long storedSinceID = 1;
        long storedMaxId = 0;
        boolean isOnline = checkForInternet();
        if(!isOnline) {
            Toast.makeText(this.getContext(),"App is offline, showing stored tweets",Toast.LENGTH_LONG).show();
        }
        noNewTweets = true;
        try {
            storedSinceID = SQLite.select(Tweet_Table.uid).from(Tweet.class).orderBy(Tweet_Table.createdAt, false).limit(1).querySingle().getUid();
            storedMaxId = SQLite.select(Tweet_Table.uid).from(Tweet.class).orderBy(Tweet_Table.createdAt, true).limit(1).querySingle().getUid();
        } catch (Exception e) {
            Log.i("SAMY-sinceID-ex", e.getMessage());
        }
        Log.i("SAMY storedSinceID", String.valueOf(storedSinceID));
        Log.i("SAMY storedMaxId", String.valueOf(storedMaxId));

        if ((page == 0 || !startOfOldTweets) && isOnline) {
            Log.i("SAMY", "new tweets " + String.valueOf(startOfOldTweets) + " page - " + String.valueOf(page));
            client.getHomeTimeline(storedSinceID, 0,new JsonHttpResponseHandler() {
                boolean cleared = false;

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.i("SAMY-", response.toString());
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.i("SAMY-refreshing", String.valueOf(refreshing));
                    if (page == 0 && !cleared && !refreshing) {
                        //SQLite.delete(Tweet.class).async().execute();
                        //SQLite.delete(User.class).async().execute();
                        cleared = true;
                        clearItems();
                    }
                    if (response.length() < TWEET_COUNT) {
                        Log.i("SAMY", "setting startOfOldTweets to true ");
                        startOfOldTweets = true;
                    }
                    addAllItems(true,response);
                    if(response.length() > 0) noNewTweets = false;
                    Log.i("SAMY-eof-endOfOldTweets",String.valueOf(endOfOldTweets));
                    Log.i("SAMY-eof-noNewTweets",String.valueOf(noNewTweets));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.i("SAMY-", responseString);
                    startOfOldTweets = true;
                    throwable.printStackTrace();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.i("SAMY-", errorResponse.toString());
                    startOfOldTweets = true;
                    throwable.printStackTrace();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.i("SAMY-", errorResponse.toString());
                    startOfOldTweets = true;
                    throwable.printStackTrace();
                }
            });
        }
        if (!endOfOldTweets || !isOnline || noNewTweets) {
            Log.i("SAMY", "old tweets " + String.valueOf(startOfOldTweets) + " page - " + String.valueOf(page));
            addAllItems(SQLite.select().from(Tweet.class).orderBy(Tweet_Table.createdAt, false).queryList());
            /*tweets.addAll(SQLite.select().from(Tweet.class).orderBy(Tweet_Table.createdAt, false).queryList());
            tweetAdapter.notifyDataSetChanged();*/
            endOfOldTweets = true;
        }
        if(endOfOldTweets && isOnline ){
            client.getHomeTimeline(0, storedMaxId,new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.i("SAMY-", response.toString());
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.i("SAMY-maxid-", response.toString());
                    addAllItems(false,response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.i("SAMY-", responseString);
                    throwable.printStackTrace();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.i("SAMY-", errorResponse.toString());
                    throwable.printStackTrace();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.i("SAMY-", errorResponse.toString());
                    throwable.printStackTrace();
                }
            });
        }
        onFinishLoadMore();
    }


    public void composeNewTweet(View view) {
        //Toast.makeText(this, "Compose clicked", Toast.LENGTH_LONG).show();
        FragmentManager fm = getFragmentManager();
        composeTweetDialogFragment = ComposeTweetDialogFragment.newInstance("Filter");
        composeTweetDialogFragment.setTargetFragment(this,20);
        composeTweetDialogFragment.show(fm, "fragment_alert");
    }

    @Override
    public void onFinishComposeTweetDialog(Bundle bundle) {
        if (bundle != null) {
            Tweet postedTweet = (Tweet) Parcels.unwrap(bundle.getParcelable("PostedTweet"));
            addSingleTweetToTop(postedTweet);
        }
    }
}
