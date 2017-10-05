package com.gumapathi.codepath.twitteroauthclient.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gumapathi.codepath.twitteroauthclient.Adapters.TweetAdapter;
import com.gumapathi.codepath.twitteroauthclient.Helpers.EndlessRecyclerViewScrollListener;
import com.gumapathi.codepath.twitteroauthclient.Models.Tweet;
import com.gumapathi.codepath.twitteroauthclient.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class TweetsDisplayFragment extends Fragment {

    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    EndlessRecyclerViewScrollListener scrollListener;

    public TweetsDisplayFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        rvTweets = (RecyclerView) v.findViewById(com.gumapathi.codepath.twitteroauthclient.R.id.rvTweet);
        tweets = new ArrayList<>();
        tweetAdapter = new TweetAdapter(tweets);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext());
        rvTweets.setLayoutManager(linearLayoutManager);
        rvTweets.setAdapter(tweetAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                //populateTimeline(page, false);
            }
        };
        rvTweets.addOnScrollListener(scrollListener);
        return v;
    }

    public void addAllItems(boolean refreshing, JSONArray response) {
        try {
            List<Tweet> newTweets = Tweet.fromJSONArray(response);
            Log.i("SAMY", "setting all tweets " + String.valueOf(newTweets.size()));
            if(refreshing) {
                tweets.addAll(0,newTweets);
            }
            else {
                tweets.addAll(newTweets);
            }
            tweetAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAllItems(List<Tweet> tweets) {
        try {
                tweets.addAll(tweets);

            tweetAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSingleTweetToTop(Tweet tweet) {
        tweets.add(0, tweet);
        tweetAdapter.notifyDataSetChanged();
        Log.i("SAMY", "added to adapter " + tweet.getBody());
        rvTweets.scrollToPosition(0);
    }

    public void clearItems() {
        tweets.clear();
        tweetAdapter.notifyDataSetChanged();
    }

}
