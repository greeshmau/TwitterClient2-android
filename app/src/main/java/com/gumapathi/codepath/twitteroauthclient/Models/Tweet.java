package com.gumapathi.codepath.twitteroauthclient.Models;

import com.gumapathi.codepath.twitteroauthclient.Database.TweetDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gumapathi on 9/26/2017.
 */
@Table(database = TweetDatabase.class)
@Parcel(analyze={Tweet.class})
public class Tweet extends BaseModel{
    @Column
    @PrimaryKey
    long uid;

    @Column
    String body;

    @Column
    @ForeignKey //(saveForeignKeyModel = false)
    User user;

    @Column
    String createdAt;

    @Column
    int retweetCount = 0;
    
    @Column
    int favouritesCount = 0;

    @Column
    String mediaUrl = "";

    @Column
    boolean favorited = false;


    public boolean isRetweeted() {
        return retweeted;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    @Column
    boolean retweeted = false;

    public Tweet() {
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getFavouritesCount() {
        return favouritesCount;
    }

    public void setFavouritesCount(int favouritesCount) {
        this.favouritesCount = favouritesCount;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public static Tweet fromJSON(JSONObject json) throws JSONException {
        Tweet tweet = new Tweet();

        tweet.body = json.getString("text");
        tweet.uid = json.getLong("id");
        tweet.createdAt = json.getString("created_at");
        tweet.user = User.fromJSONWithDBSave(json.getJSONObject("user"));
        if(json.getJSONObject("entities").has("media")) {
            tweet.mediaUrl = getMediaUrlFromjson(json.getJSONObject("entities").getJSONArray("media"));
        }
        else {
            tweet.mediaUrl = "";
        }
        if(json.getBoolean("favorited")==true) {
            tweet.favorited = true;
        }
        else{
            tweet.favorited = false;
        }
        if(json.getBoolean("retweeted")==true) {
            tweet.retweeted = true;
        }
        else{
            tweet.retweeted = false;
        }
        if(json.has("retweet_count")) {
            tweet.retweetCount = Integer.parseInt(json.getString("retweet_count"));
        }
        if(json.has("favorite_count")) {
            tweet.favouritesCount = Integer.parseInt(json.getString("favorite_count"));
        }
        
        tweet.save();
        return tweet;
    }

    public static List<Tweet> fromJSONArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> allTweets = new ArrayList<>();
        for(int i = 0; i < jsonArray.length();i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            Tweet tweet = new Tweet();

            tweet.body = json.getString("text");
            tweet.uid = json.getLong("id");
            tweet.createdAt = json.getString("created_at");
            tweet.user = User.fromJSONWithDBSave(json.getJSONObject("user"));

            if(json.getJSONObject("entities").has("media")) {
                tweet.mediaUrl = getMediaUrlFromjson(json.getJSONObject("entities").getJSONArray("media"));
            }
            else {
                tweet.mediaUrl = "";
            }
            if(json.has("retweet_count")) {
                tweet.retweetCount = Integer.parseInt(json.getString("retweet_count"));
            }
            if(json.has("favorite_count")) {
                tweet.favouritesCount = Integer.parseInt(json.getString("favorite_count"));
            }
            tweet.save();
            allTweets.add(tweet);
        }

        /*FlowManager.getDatabase(TweetDatabase.class).executeTransaction(FastStoreModelTransaction
                .insertBuilder(FlowManager.getModelAdapter(Tweet.class))
                .addAll(allTweets)
                .build());
         */

        /*FlowManager.getDatabase(TweetDatabase.class).executeTransaction(FastStoreModelTransaction
                .insertBuilder(FlowManager.getModelAdapter(Use.class))
                .addAll(allTweets)
                .build());
*/
        return allTweets;
    }

    public static String getMediaUrlFromjson(JSONArray jsonArray) {

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                if (jsonArray.getJSONObject(i).has("media_url")) {
                    String mediaUrl = jsonArray.getJSONObject(i).get("media_url").toString();

                    if (mediaUrl.equals("")) {
                        continue;
                    } else {
                        return mediaUrl;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    /*
    * Hi there, I am using FastStoreModelTransaction to store list of Tweets (getModelAdapter(Tweet.class)) as suggested here - https://agrosner.gitbooks.io/dbflow/content/StoringData.html. But this doesn't store the User object in the DB although User is a member of Tweet class.
    * Is that how it should behave?
    */
}
