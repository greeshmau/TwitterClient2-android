package com.gumapathi.codepath.twitteroauthclient.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gumapathi.codepath.twitteroauthclient.Activities.TabbedLayoutActivity;
import com.gumapathi.codepath.twitteroauthclient.Models.Tweet;
import com.gumapathi.codepath.twitteroauthclient.Models.User;
import com.gumapathi.codepath.twitteroauthclient.R;
import com.gumapathi.codepath.twitteroauthclient.TwitterApplication;
import com.gumapathi.codepath.twitteroauthclient.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;

/**
 * Created by gumapathi on 10/8/17.
 */
public class ReplyTweetFragment extends BottomSheetDialogFragment {

    private static ReplyTweetFragment instance = null;

    @BindView(R.id.etTweetText) EditText etTweetText;
    @BindView(R.id.btnPost) Button btnPostTweet;
    @BindView(R.id.tvCharCount) TextView tvCharCount;
    @BindView(R.id.tvInReplyTo) TextView tvInReplyTo;

    Boolean isReply;
    Tweet tweet;
    User user;
    private Unbinder unbinder;

    TwitterClient client;
    //    TextView tvCharCount;
    public static int TWEET_CHAR_LIMIT = 140;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reply_tweet, container);
        unbinder = ButterKnife.bind(this, view);
        tweet = Parcels.unwrap(getArguments().getParcelable("tweet"));
        user = Parcels.unwrap(getArguments().getParcelable("user"));
        isReply = (Boolean) getArguments().get("is_reply");
        return view;
    }

    public static ReplyTweetFragment newInstance(Boolean isReply, Tweet tweet, User user) {
        ReplyTweetFragment fragment = new ReplyTweetFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putBoolean("is_reply", isReply);
        args.putParcelable("user", Parcels.wrap(user));
        args.putParcelable("tweet", Parcels.wrap(tweet));
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //get singleton rest client
        client = TwitterApplication.getRestClient();
        btnPostTweet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                client.composeReTweet(new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("SAMY-rt", "onsuccess" + response.toString());
                        Intent intent = new Intent(getActivity(), TabbedLayoutActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        Log.d("SAMY-rt", "fail" + responseString);

                        dismiss();
                    }
                }, etTweetText.getText().toString(), isReply, tweet.getUid());
            }


        });

        etTweetText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                tvCharCount.setText(String.valueOf(TWEET_CHAR_LIMIT - s.length()));
                if(s.length() > 0 && s.length() <= TWEET_CHAR_LIMIT){
                    btnPostTweet.setEnabled(true);
                    btnPostTweet.setBackground(getContext().getResources().getDrawable(R.drawable.button_on));
                }else{
                    btnPostTweet.setEnabled(false);
                    btnPostTweet.setBackground(getContext().getResources().getDrawable(R.drawable.button_off));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if(isReply){
            tvInReplyTo.setVisibility(View.VISIBLE);
            tvInReplyTo.setText("In reply to " + user.getName());
            etTweetText.setText(user.getScreenName() + " ");
            etTweetText.requestFocus();
        }

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}