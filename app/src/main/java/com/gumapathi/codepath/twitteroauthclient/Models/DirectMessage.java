package com.gumapathi.codepath.twitteroauthclient.Models;

import android.util.Log;

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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by gumapathi on 10/7/17.
 */


@Table(database = TweetDatabase.class)
@Parcel(analyze={DirectMessage.class})
public class DirectMessage extends BaseModel{

    @Column
    @PrimaryKey(autoincrement = true)
    long uid;

    @Column
    @ForeignKey
    User recipientUser;

    @Column
    @ForeignKey
    User senderUser;

    @Column
    String text;

    @Column
    String relativeDate;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public void setRecipientUser(User recipientUser) {
        this.recipientUser = recipientUser;
    }

    public void setSenderUser(User senderUser) {
        this.senderUser = senderUser;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setRelativeDate(String relativeDate) {
        this.relativeDate = relativeDate;
    }

    public User getRecipientUser() {
        return recipientUser;
    }

    public User getSenderUser() {
        return senderUser;
    }

    public String getText() {
        return text;
    }

    public String getRelativeDate() {
        return relativeDate;
    }

    public DirectMessage(){

    }

    public static DirectMessage fromJson(JSONObject jsonObject){

        DirectMessage message = new DirectMessage();
        try {
            message.text = jsonObject.getString("text");
            message.relativeDate = getRelativeTimeAgo(jsonObject.getString("created_at"));
            message.recipientUser = User.fromJSONWithDBSave(jsonObject.getJSONObject("recipient"));
            message.senderUser = User.fromJSONWithDBSave(jsonObject.getJSONObject("sender"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    public static ArrayList<DirectMessage> fromJsonArray(JSONArray jsonArray){

        ArrayList<DirectMessage> result = new ArrayList<>();
        for(int i=0; i< jsonArray.length(); i++){
            try {
                Log.i("Message", jsonArray.getJSONObject(i).toString());
                DirectMessage message = fromJson(jsonArray.getJSONObject(i));

                result.add(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    private static String getRelativeTimeAgo(String rawJsonDate) {
        try {

            DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            DateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
            inputFormat.setLenient(true);

            Date twDate = inputFormat.parse(rawJsonDate);
            String outputText = outputFormat.format(twDate);

            Date newDate = new Date();
            long timeDiff = 0;
            String suffix = "m";
            timeDiff = getDateDiff(twDate, newDate, TimeUnit.MINUTES);
            boolean hour = false;
            if(timeDiff > 60) {
                timeDiff = getDateDiff(twDate, newDate, TimeUnit.HOURS);
                suffix = "h";
                hour = true;
            }
            if(timeDiff > 24 && hour) {
                timeDiff = getDateDiff(twDate, newDate, TimeUnit.DAYS);
                suffix = "d";
            }
            return (String.valueOf(timeDiff)+suffix);
        }
        catch (ParseException e) {
            Log.i("SAMY-dtParsEx ", e.getMessage());
            e.printStackTrace();
        }
        return (String.valueOf(""));
    }

    private static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

}
