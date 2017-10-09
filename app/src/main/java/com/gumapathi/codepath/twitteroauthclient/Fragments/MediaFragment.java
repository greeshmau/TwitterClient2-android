package com.gumapathi.codepath.twitteroauthclient.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.gumapathi.codepath.twitteroauthclient.Adapters.ImagesArrayAdapter;
import com.gumapathi.codepath.twitteroauthclient.Helpers.EndlessScrollListener;
import com.gumapathi.codepath.twitteroauthclient.Models.Tweet;
import com.gumapathi.codepath.twitteroauthclient.R;
import com.gumapathi.codepath.twitteroauthclient.TwitterApplication;
import com.gumapathi.codepath.twitteroauthclient.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by gumapathi on 10/6/2017.
 */

public class MediaFragment extends Fragment {

    static final int FIRST_PAGE = 1;
    static long maxID = 1;
    GridView gvPhotos;
    ImagesArrayAdapter imagesArrayAdapter;
    ArrayList<Tweet> tweets;
    TwitterClient client;
    String screenName;
    public static final String ARG_PAGE = "ARG_PAGE";


    public MediaFragment() {
        // Required empty public constructor
    }

    public static MediaFragment newInstance(int page) {
        MediaFragment fragment = new MediaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        tweets = new ArrayList<>();
        Bundle args = getArguments();
        this.screenName = args.getString("screenName");
        Log.i("SAMY-bun-med", screenName);
        imagesArrayAdapter = new ImagesArrayAdapter(getContext(), tweets);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media, container, false);
        gvPhotos = (GridView) view.findViewById(R.id.gvMedia);
        gvPhotos.setOnScrollListener(scrollListener);

        gvPhotos.setAdapter(imagesArrayAdapter);
        populatePhotos(FIRST_PAGE);
        Log.i("SAMY-bun-med", screenName);
        return view;
    }

    EndlessScrollListener scrollListener = new EndlessScrollListener() {
        @Override
        public boolean onLoadMore(int page, int totalItemsCount) {
            populatePhotos(0);
            return true;
        }
    };

    public void populatePhotos(long max_id) {
        Log.i("SAMY-", "populatePhotos");

        client.getUserTimeline(0,screenName, handler);
    }

    JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            ArrayList<Tweet> tweets = new ArrayList<>();
            try {
                tweets = (ArrayList<Tweet>) Tweet.fromJSONArray(response);
            }
            catch (Exception e) {
                Log.i("SAMY-ex-phot", e.getMessage());
            }

            if (tweets.size() > 0) {
                Tweet mostRecentTweet = tweets.get(tweets.size() - 1);
                maxID = mostRecentTweet.getUid();
            }

            ArrayList<Tweet> toRemove = new ArrayList<>();
            for (Tweet tweet : tweets) {
                if (tweet.getMediaUrl() == null)
                    toRemove.add(tweet);
            }
            tweets.removeAll(toRemove);
            imagesArrayAdapter.addAll(tweets);
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            Log.i("SAMY-media-err", errorResponse.toString());
        }
    };
}