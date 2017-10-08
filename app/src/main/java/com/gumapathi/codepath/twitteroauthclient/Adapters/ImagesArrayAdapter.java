package com.gumapathi.codepath.twitteroauthclient.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.gumapathi.codepath.twitteroauthclient.Models.Tweet;
import com.gumapathi.codepath.twitteroauthclient.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by gumapathi on 10/7/17.
 */

public class ImagesArrayAdapter extends ArrayAdapter<Tweet> {

    Context mContext;
    public ImagesArrayAdapter(Context context, List<Tweet> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        if(convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false);
        ImageView ivImage = (ImageView)convertView.findViewById(R.id.ivImage);
        if(!tweet.getMediaUrl().isEmpty()) {
        Glide.with(getContext())
                .load(tweet.getMediaUrl())
                //.placeholder(R.drawable.loading)
                //.error(R.drawable.image_unavailable)
                .into(ivImage);

        }
        else {
            //ivImage.
        }
        return convertView;
    }
}