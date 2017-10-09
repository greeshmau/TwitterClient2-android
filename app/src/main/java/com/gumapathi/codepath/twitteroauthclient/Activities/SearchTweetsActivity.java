package com.gumapathi.codepath.twitteroauthclient.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gumapathi.codepath.twitteroauthclient.Fragments.TweetSearchFragment;
import com.gumapathi.codepath.twitteroauthclient.Fragments.TweetsDisplayFragment;
import com.gumapathi.codepath.twitteroauthclient.R;
import com.gumapathi.codepath.twitteroauthclient.TwitterApplication;
import com.gumapathi.codepath.twitteroauthclient.TwitterClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by gumapathi on 10/8/2017.
 */

public class SearchTweetsActivity  extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;
    private TwitterClient client;
    String queryText;
    private TweetsDisplayFragment tweetsDisplayFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        client = TwitterApplication.getRestClient();
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Search Popular Tweets");


        if (savedInstanceState == null) {
            if (Intent.ACTION_SEARCH.equals(getIntent().getAction())) {
                String query = getIntent().getStringExtra(SearchManager.QUERY);
                Toast.makeText(this, "Searching for " + query, Toast.LENGTH_LONG ).show();
                loadFragment(query);
            }else {
                loadFragment(getIntent().getStringExtra("q"));
            }
        }

    }


    private void loadFragment(String query){

        TweetSearchFragment fragmentUserTimeline = TweetSearchFragment.newInstance(query);
        Log.i("SAMY-serfr",query);
        //display user fragment dynamically within this activity
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, fragmentUserTimeline);
        ft.commit();

    }

    // pass context to Calligraphy
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(context));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        int searchImgId = android.support.v7.appcompat.R.id.search_button;
        ImageView v = (ImageView) searchView.findViewById(searchImgId);
        v.setImageResource(R.drawable.search_black);
        // Customize searchview text and hint colors
        int searchEditId = android.support.v7.appcompat.R.id.search_src_text;
        EditText et = (EditText) searchView.findViewById(searchEditId);
        et.setTextColor(Color.BLACK);
        et.setHintTextColor(Color.BLACK);

        // Customize searchview text and hint colors
        //int searchEditId = android.support.v7.appcompat.R.id.search_src_text;
        //EditText et = (EditText) searchView.findViewById(searchEditId);
        //et.setTextColor(Color.BLACK);
        //et.setHintTextColor(Color.GRAY);
        et.setHint("Search tweets");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryText = query;
                Log.i("SAMY-serr",query);
                // perform query here
                searchView.clearFocus();
                loadFragment(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

}

