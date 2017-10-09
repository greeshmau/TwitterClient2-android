package com.gumapathi.codepath.twitteroauthclient.Fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gumapathi.codepath.twitteroauthclient.Models.Tweet;
import com.gumapathi.codepath.twitteroauthclient.Models.Tweet_Table;
import com.gumapathi.codepath.twitteroauthclient.TwitterApplication;
import com.gumapathi.codepath.twitteroauthclient.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.gumapathi.codepath.twitteroauthclient.TwitterClient.TWEET_COUNT;
import static com.gumapathi.codepath.twitteroauthclient.Utils.Utils.checkForInternet;

/**
 * Created by gumapathi on 10/1/17.
 */

public class UserTimelineFragment extends TweetsDisplayFragment {

    private TwitterClient client;
    public static final String ARG_PAGE = "ARG_PAGE";
    String screenName;


    public static UserTimelineFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        UserTimelineFragment fragment = new UserTimelineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        this.screenName = args.getString("screenName");
        Log.i("SAMY-bun", screenName);
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
        boolean isOnline = checkForInternet();
        if (!isOnline) {
            Toast.makeText(this.getContext(), "App is offline, cannot show favorite tweets", Toast.LENGTH_LONG).show();
        } else {
            client.getUserTimeline(0, screenName,new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.i("SAMY-", response.toString());
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.i("SAMY-refreshing", String.valueOf(refreshing));
                    Log.i("SAMY-getUserTimeline", response.toString());

                    clearItems();
                    try {
                        if (refreshing) {
                            addAllItems(true,response);
                        } else {
                            addAllItems(false,response);;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.i("SAMY-", responseString);
                    throwable.printStackTrace();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.i("SAMY-", "error");
                    throwable.printStackTrace();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.i("SAMY-", "error");
                    throwable.printStackTrace();
                }
            });
            onFinishLoadMore();
        }
    }
    @Override
    public void onFinishComposeTweetDialog(Bundle bundle) {
    }
}
