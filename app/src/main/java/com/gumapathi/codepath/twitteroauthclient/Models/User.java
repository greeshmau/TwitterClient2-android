package com.gumapathi.codepath.twitteroauthclient.Models;

import com.gumapathi.codepath.twitteroauthclient.Database.TweetDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by gumapathi on 9/26/2017.
 */
@Table(database = TweetDatabase.class)
@Parcel(analyze={User.class})
public class User extends BaseModel{
    @Column
    @PrimaryKey
    long uid;

    @Column
    String name;

    @Column
    String screenName;

    @Column
    String profileImageURL;

    @Column
    String headerImageURL;

    @Column
    int followingCount;

    @Column
    int followersCount;

    @Column
    String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public String getHeaderImageURL() {
        return headerImageURL;
    }

    public void setHeaderImageURL(String headerImageURL) {
        this.headerImageURL = headerImageURL;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public static User fromJSONWithDBSave(JSONObject json) throws JSONException {
        User user = new User();

        user.name = json.getString("name");
        user.uid = json.getLong("id");
        user.screenName = json.getString("screen_name");
        user.profileImageURL = json.getString("profile_image_url");
        user.description = json.getString("description");
        if(json.has("profile_banner_url")) {
            user.headerImageURL = json.getString("profile_banner_url");
        }
        else {
            user.headerImageURL = "";
        }
        user.followersCount = json.getInt("followers_count");
        user.followingCount = json.getInt("friends_count");
        user.save();
        return user;
    }
}
