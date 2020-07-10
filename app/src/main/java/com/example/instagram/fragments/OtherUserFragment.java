package com.example.instagram.fragments;

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
import com.example.instagram.models.Post;
import com.example.instagram.adapters.ProfileAdapter;
import com.example.instagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class OtherUserFragment extends Fragment {
    public static final String TAG="OtherUserFragment";
    private RecyclerView rvPosts;
    protected ProfileAdapter adapter;
    protected List<Post> userPosts;
    private GridLayoutManager gridLayoutManager;
    private ParseUser user;
    private TextView tvUsername;
    private TextView tvBio;
    private ImageView ivProfileImage;
    private EndlessRecyclerViewScrollListener scrollListener;
    boolean infScroll = false;

    public OtherUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_other_user, container, false);

        Post post = (Post)getArguments().get("Post");
        user = post.getUser();
        tvUsername = view.findViewById(R.id.tvUsername);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        tvBio = view.findViewById(R.id.tvBio);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvUsername.setText(user.getUsername());
        tvBio.setText(user.getString("bio"));
        ParseFile profileImage = user.getParseFile("profilepic");
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

        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                infScroll = true;
                Log.i(TAG, "Asking for inf scroll posts");
                queryUserPosts();
            }
        };
        rvPosts.addOnScrollListener(scrollListener);


    }



    private void queryUserPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);

        if (infScroll && userPosts.size() > 0) {
            Date oldest = userPosts.get(userPosts.size() - 1).getCreatedAt();
            Log.i(TAG, "Getting inf scroll posts");
            query.whereLessThan("createdAt", oldest);
            infScroll = false;
        }

        query.setLimit(24);
        query.addDescendingOrder("createdAt");
        Log.e(TAG, "Id: " + user.getObjectId());
        query.whereEqualTo("user", user);
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
