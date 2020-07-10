package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.instagram.adapters.CommentsAdapter;
import com.example.instagram.models.Comment;
import com.example.instagram.models.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONException;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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

    private RecyclerView rvComments;
    private List<Comment> comments;
    private CommentsAdapter adapter;

    boolean needsUpdate;
    int numLikes;

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
        rvComments = findViewById(R.id.rvComments);
        comments = new ArrayList<>();
        adapter = new CommentsAdapter(this, comments);
        rvComments.setAdapter(adapter);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComments.setLayoutManager(linearLayoutManager);


        Intent intent = getIntent();
        post = (Post) intent.getExtras().get("PostDetails");

        tvUsername.setText(post.getUser().getUsername());
        tvCaption.setText(post.getDescription());
        tvDate.setText(getRelativeTimeAgo(post.getCreatedAt().toString()));


        numLikes = post.getLikes().length();
        try {
            if (post.isLiked()) {
                fabFavorite.setImageResource(R.drawable.ic_likes_filled);
                fabFavorite.setColorFilter(getResources().getColor(R.color.medium_red));
            }
            else {
                fabFavorite.clearColorFilter();
                fabFavorite.setImageResource(R.drawable.ic_likes);

            }
            needsUpdate = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (numLikes == 1)
            tvLikes.setText(String.format("%d", 1) + " like");
        else
            tvLikes.setText(String.format("%d", numLikes) + " likes");


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
                        fabFavorite.setImageResource(R.drawable.ic_likes);
                        fabFavorite.clearColorFilter();

                    }
                    else {
                        post.likePost();
                        fabFavorite.setImageResource(R.drawable.ic_likes_filled);
                        fabFavorite.setColorFilter(getResources().getColor(R.color.medium_red));


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e("PostDetailActivity", "Error while saving", e);
                        }
                    }
                });
                tvLikes.setText(String.format("%d", post.getLikes().length()) + " likes");
            }});

        fabComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostDetailActivity.this, CommentActivity.class);
                intent.putExtra("post", (Serializable) post);
                PostDetailActivity.this.startActivity(intent);
            }
        });


            // populateComments();

    }

    private void populateComments() {
        ParseQuery<Comment> query = new ParseQuery<>(Comment.class);
        // query.whereEqualTo("post_id", post.getObjectId());
        // query.include("user");
        query.whereEqualTo("post", post);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> objects, ParseException e) {
                if (e == null) {
                    adapter.addAll(objects);
                    adapter.notifyItemInserted(0);
                    rvComments.scrollToPosition(0);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
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

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
        adapter.clear();
        populateComments();
        Log.d("PostDetailActivity", "Got here after comment");
    }
}