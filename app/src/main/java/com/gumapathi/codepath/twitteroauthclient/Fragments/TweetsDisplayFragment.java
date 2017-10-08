package com.gumapathi.codepath.twitteroauthclient.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gumapathi.codepath.twitteroauthclient.Activities.DetailActivity;
import com.gumapathi.codepath.twitteroauthclient.Adapters.TweetAdapter;
import com.gumapathi.codepath.twitteroauthclient.Helpers.EndlessRecyclerViewScrollListener;
import com.gumapathi.codepath.twitteroauthclient.Helpers.ItemClickSupport;
import com.gumapathi.codepath.twitteroauthclient.Models.Tweet;
import com.gumapathi.codepath.twitteroauthclient.R;

import org.json.JSONArray;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public abstract class TweetsDisplayFragment extends Fragment implements ComposeTweetDialogFragment.ComposeTweetDialogListener {

    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeContainer;
    ComposeTweetDialogFragment composeTweetDialogFragment;

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
        Log.i("SAMY", "TweetsListFragment onCreateView");

        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        rvTweets = (RecyclerView) v.findViewById(com.gumapathi.codepath.twitteroauthclient.R.id.rvTweet);
        tweets = new ArrayList<>();
        tweetAdapter = new TweetAdapter(tweets);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext());
        rvTweets.setLayoutManager(linearLayoutManager);
        rvTweets.setAdapter(tweetAdapter);

        ItemClickSupport.addTo(rvTweets).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Tweet tweet = tweets.get(position);
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra("tweet", Parcels.wrap(tweet));
                        intent.putExtra("user", Parcels.wrap(tweet.getUser()));
                        startActivity(intent);
                    }
                }
        );
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                populateTimeline(page, false);
            }
        };
        rvTweets.addOnScrollListener(scrollListener);

        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.refreshing
                populateTimeline(0, true);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        populateTimeline(0, false);
        FloatingActionButton fabCompose = (FloatingActionButton) v.findViewById(R.id.fabCompose);
        fabCompose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                composeNewTweet(v);
            }
        });
        return v;
    }

    public void addAllItems(boolean refreshing, JSONArray response) {
        try {
            List<Tweet> newTweets = Tweet.fromJSONArray(response);
            Log.i("SAMY", "setting all tweets " + String.valueOf(newTweets.size()));
            if (refreshing) {
                tweets.addAll(0, newTweets);
            } else {
                tweets.addAll(newTweets);
            }
            tweetAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.i("SAMY", "setting all tweets exception" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void addAllItems(List<Tweet> tweets) {
        try {
            tweets.addAll(tweets);
            tweetAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Log.i("SAMY", "1setting all tweets exception" + e.getMessage());
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

    protected void onFinishLoadMore() {
        //ldProgress.setVisibility(View.GONE);
        //rvTweets.setVisibility(View.VISIBLE);
        swipeContainer.setRefreshing(false);
    }

    abstract void populateTimeline(int page, boolean refreshing);

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
            if(getParentFragment().getClass().equals(HomeFragment.class)) {
                addSingleTweetToTop(postedTweet);
            }
        }
    }
}
