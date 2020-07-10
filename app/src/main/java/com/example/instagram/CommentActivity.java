package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.instagram.adapters.CommentsAdapter;
import com.example.instagram.models.Comment;
import com.example.instagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private EditText etComment;
    private Button btnSend;
    private RecyclerView rvComments;
    private List<Comment> comments;
    private CommentsAdapter adapter;
    private TextView tvContent;
    private TextView tvUsername;
    private ImageView ivProfileImage;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        btnSend = findViewById(R.id.btnSend);
        etComment = findViewById(R.id.etComment);
        rvComments = findViewById(R.id.rvComments);

        tvContent = findViewById(R.id.tvContent);
        tvUsername = findViewById(R.id.tvUsername);

        ivProfileImage = findViewById(R.id.ivProfileImage);
        comments = new ArrayList<>();
        adapter = new CommentsAdapter(this, comments);

        post = (Post) getIntent().getSerializableExtra("post");
        tvUsername.setText(post.getUser().getUsername());
        tvContent.setText(post.getDescription());

        ParseFile profileImage = null;
        try {
            profileImage = post.getUser().fetchIfNeeded().getParseFile("profilepic");
            if (profileImage != null) {
                String url = profileImage.getUrl();
                Glide.with(this).load(url).transform(new CircleCrop()).into(ivProfileImage);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        rvComments.setAdapter(adapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComments.setLayoutManager(linearLayoutManager);
        populateComments();


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createComment();
            }
        });
    }

    private void createComment() {
        Comment newComment = new Comment();
        newComment.setContent(etComment.getText().toString());
        newComment.setPost(post);
        newComment.setUser(ParseUser.getCurrentUser());


        newComment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                    Log.d("CommentActivity", "Comment saved");
            }
        });
        etComment.setText(null);
        adapter.clear();
        populateComments();
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
}