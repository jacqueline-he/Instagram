package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
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
    private TextView tvCaption;
    private TextView tvUsername;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        btnSend = findViewById(R.id.btnSend);
        etComment = findViewById(R.id.etComment);
        rvComments = findViewById(R.id.rvComments);

        tvCaption = findViewById(R.id.tvCaption);
        tvUsername = findViewById(R.id.tvUsername);

        comments = new ArrayList<>();
        adapter = new CommentsAdapter(this, comments);

        post = (Post) getIntent().getSerializableExtra("post");
        tvUsername.setText(post.getUser().getUsername());
        tvCaption.setText(post.getDescription());



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