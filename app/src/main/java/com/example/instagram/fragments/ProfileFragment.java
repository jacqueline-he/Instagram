package com.example.instagram.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.instagram.LoginActivity;
import com.example.instagram.Post;
import com.example.instagram.PostsAdapter;
import com.example.instagram.ProfileAdapter;
import com.example.instagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {
    public static final String TAG="ProfileFragment";
    private ImageView ivLogout;
    private RecyclerView rvPosts;
    protected ProfileAdapter adapter;
    protected List<Post> userPosts;
    private GridLayoutManager gridLayoutManager;
    private ParseUser currentUser;
    private TextView tvUsername;
    private ImageView ivProfileImage;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ivLogout = view.findViewById(R.id.ivLogout);
        currentUser = ParseUser.getCurrentUser();
        tvUsername = view.findViewById(R.id.tvUsername);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvUsername.setText(currentUser.getUsername());

        ParseFile profileImage = currentUser.getParseFile("profilepic");
        if (profileImage != null) {
            Glide.with(this).load(profileImage.getUrl()).transform(new CircleCrop()).into(ivProfileImage);
        }


        rvPosts = view.findViewById(R.id.rvPosts);
        userPosts = new ArrayList<>();
        adapter = new ProfileAdapter(getContext(), userPosts);

        gridLayoutManager = new GridLayoutManager(getContext(), 3);
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(gridLayoutManager);
        queryUserPosts();


    }

    private void queryUserPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(24);
        query.addDescendingOrder("createdAt");
        Log.e(TAG, "Id: " + currentUser.getObjectId());
        query.whereEqualTo("user", currentUser);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription()+ " , username: " + post.getUser().getUsername());
                }
                userPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
