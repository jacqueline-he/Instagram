package com.example.instagram.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.instagram.MainActivity;
import com.example.instagram.Post;
import com.example.instagram.PostsAdapter;
import com.example.instagram.ProfileAdapter;
import com.example.instagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PostsFragment extends Fragment {

    public static final String TAG="PostsFragment";
    private RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;
    private LinearLayoutManager linearLayoutManager;
    boolean infScroll = false;

    private ImageView ivCamera;

    public PostsFragment() {
        // Required empty public constructor
    }


    public PostsAdapter getAdapter() {
        return adapter;
    }

    public List<Post> getPosts() {
        return allPosts;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeContainer = view.findViewById(R.id.swipeContainer);
        rvPosts = view.findViewById(R.id.rvPosts);

        ivCamera = view.findViewById(R.id.ivCamera);

        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), allPosts);

        linearLayoutManager = new LinearLayoutManager(getContext());
        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(linearLayoutManager);


        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).launchCamera();
            }
        });

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                infScroll = true;
                Log.i(TAG, "Asking for inf scroll posts");
                queryPosts();
            }
        };
        rvPosts.addOnScrollListener(scrollListener);

        swipeToRefresh();
        // adapter.clear();
        queryPosts();
    }

    public void swipeToRefresh() {
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clear();
                queryPosts();
                swipeContainer.setRefreshing(false);
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.medium_red);
    }

    public void queryPosts() {
        // Specify which class to query
        /*ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder("createdAt");
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
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });*/

        ParseQuery<Post> query = new ParseQuery<Post>(Post.class);
        query.include(Post.KEY_USER);

        if (infScroll && allPosts.size() > 0) {
            Date oldest = allPosts.get(allPosts.size() - 1).getCreatedAt();
            Log.i(TAG, "Getting inf scroll posts");
            query.whereLessThan("createdAt", oldest);
            infScroll = false;
        }

        query.setLimit(20);
        query.addDescendingOrder("createdAt");
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
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });


    }
}