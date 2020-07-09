package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class PostDetailActivity extends AppCompatActivity {

    private ImageView ivPostImage;
    private ImageView ivProfileImage;
    private TextView tvUsername;
    private TextView tvDate;
    private TextView tvCaption;
    private TextView tvLikes;
    private FloatingActionButton fabFavorite;
    private FloatingActionButton fabComment;
    private FloatingActionButton fabMessage;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        ivPostImage = findViewById(R.id.ivPostImage);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvUsername = findViewById(R.id.tvUsername);
        tvDate = findViewById(R.id.tvDate);
        tvCaption = findViewById(R.id.tvCaption);
        tvLikes = findViewById(R.id.tvLikes);
        fabFavorite = findViewById(R.id.fabFavorite);
        fabComment = findViewById(R.id.fabComment);
        fabMessage = findViewById(R.id.fabMessage);

        Intent intent = getIntent();
        post = (Post) intent.getExtras().get("PostDetails");

        tvUsername.setText(post.getUser().getUsername());
        tvCaption.setText(post.getDescription());
        tvDate.setText(getRelativeTimeAgo(post.getCreatedAt().toString()));


        int likes = post.getLikes().length();
        try {
            if (post.isLiked()) {
                fabFavorite.setColorFilter(getResources().getColor(R.color.medium_red));
            }
            else {
                fabFavorite.clearColorFilter();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (likes == 1)
            tvLikes.setText(String.format("%d", 1) + " like");
        else
            tvLikes.setText(String.format("%d", likes) + " likes");


        ParseFile profileImage = post.getUser().getParseFile("profilepic");
        if (profileImage != null) {
            Glide.with(this).load(profileImage.getUrl()).transform(new CircleCrop()).into(ivProfileImage);
        }


        ParseFile postImage = post.getImage();
        if (postImage != null)
            Glide.with(this).load(postImage.getUrl()).into(ivPostImage);

        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (post.isLiked()) {
                        post.unlikePost();
                        fabFavorite.clearColorFilter();

                    }
                    else {
                        post.likePost();
                        fabFavorite.setColorFilter(getResources().getColor(R.color.medium_red));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                post.saveInBackground();
                tvLikes.setText(String.format("%d", post.getLikes().length()) + " likes");
                tvLikes.setVisibility(View.VISIBLE);
            }});




    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}