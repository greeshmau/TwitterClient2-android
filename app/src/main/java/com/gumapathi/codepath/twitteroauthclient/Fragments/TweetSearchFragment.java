package com.gumapathi.codepath.twitteroauthclient.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.gumapathi.codepath.twitteroauthclient.Models.Tweet;
import com.gumapathi.codepath.twitteroauthclient.TwitterApplication;
import com.gumapathi.codepath.twitteroauthclient.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by gumapathi on 10/8/2017.
 */

public class TweetSearchFragment  extends TweetsDisplayFragment {
    private TwitterClient client;

        public static TweetSearchFragment newInstance(String query) {

            Bundle args = new Bundle();

            TweetSearchFragment fragment = new TweetSearchFragment();
            args.putString("q", query);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.i("HomeTimelineFragment", "oncreate");
            client  = TwitterApplication.getRestClient();
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        }

        public void populateTimeline(final int page, final boolean refreshing) {
            long sinceOrMaxId = 1;
            Log.i("SAMY", "populateTimeline");
            String query = getArguments().getString("q");
            Log.i("SAMY-quer", query);
            client.searchPopularTweets(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.i("SAMY-ser", String.valueOf(response));
                    //clearItems();
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
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.i("SAMY-serobj", String.valueOf(response));
                    //clearItems();
                    try {
                        if (refreshing) {
                            //addAllItems(true,response.toJSONArray());
                        } else {
                            //addAllItems(false,response);;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("SAMY-searw", "onFailure");
                    onFinishLoadMore();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("SAMY-searw", "onFailure");

                }
            },  query);
        }
    @Override
    public void onFinishComposeTweetDialog(Bundle bundle) {
    }
}