package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

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
        Post post = (Post) intent.getExtras().get("PostDetails");

        tvUsername.setText(post.getUser().getUsername());
        tvCaption.setText(post.getDescription());
        tvDate.setText(getRelativeTimeAgo(post.getCreatedAt().toString()));

        ParseFile file = post.getImage();
        file.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                ivPostImage.setImageBitmap(bitmap);
            }
        });


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