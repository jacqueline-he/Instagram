package com.example.instagram;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.instagram.fragments.OtherUserFragment;
import com.example.instagram.fragments.ProfileFragment;
import com.example.instagram.models.Post;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.json.JSONException;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;
    int detailPosition = 0;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return posts.size();
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvUsername;
        private ImageView ivPostImage;
        private ImageView ivProfileImage;
        private TextView tvDate;
        private FloatingActionButton fabFavorite;
        private FloatingActionButton fabComment;
        private FloatingActionButton fabMessage;
        private TextView tvDescription;
        private TextView tvLikes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivPostImage = itemView.findViewById(R.id.ivPostImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvLikes = itemView.findViewById(R.id.tvLikes);

            fabFavorite = itemView.findViewById(R.id.fabFavorite);
            fabComment = itemView.findViewById(R.id.fabComment);
            fabMessage = itemView.findViewById(R.id.fabMessage);

            itemView.setOnClickListener(this);
        }



        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        public void bind(final Post post) {
            tvDescription.setText(post.getDescription());
            tvUsername.setText(post.getUser().getUsername());
            tvDate.setText(getRelativeTimeAgo(post.getCreatedAt().toString()));


            ParseFile profileImage = post.getUser().getParseFile("profilepic");
            if (profileImage != null) {
                Glide.with(context).load(profileImage.getUrl()).transform(new CircleCrop()).into(ivProfileImage);
            }

            int likes = post.getLikes().length();
            try {
                if (post.isLiked()) {
                    fabFavorite.setImageResource(R.drawable.ic_likes_filled);
                    fabFavorite.setColorFilter(context.getResources().getColor(R.color.medium_red));
                }
                else {
                    fabFavorite.setImageResource(R.drawable.ic_likes);
                    fabFavorite.clearColorFilter();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (likes == 1)
                tvLikes.setText(String.format("%d", 1) + " like");
            else
                tvLikes.setText(String.format("%d", likes) + " likes");


            ivProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    FragmentManager fm = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                    if (post.getUser().getUsername().equals(currentUser.getUsername())) {
                        fm.beginTransaction().replace(R.id.flContainer, new ProfileFragment()).commit();
                        ((MainActivity) context).bottomNavigationView.setSelectedItemId(R.id.action_profile);
                    }
                    else {
                        Fragment fragment = new OtherUserFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Post", post);
                        fragment.setArguments(bundle);
                        fm.beginTransaction().replace(R.id.flContainer, fragment).commit();
                    }
                }
            });

            tvUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    FragmentManager fm = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                    if (post.getUser().getUsername().equals(currentUser.getUsername())) {
                        fm.beginTransaction().replace(R.id.flContainer, new ProfileFragment()).commit();
                        ((MainActivity) context).bottomNavigationView.setSelectedItemId(R.id.action_profile);
                    }
                    else {
                        Fragment fragment = new OtherUserFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Post", post);
                        fragment.setArguments(bundle);
                        fm.beginTransaction().replace(R.id.flContainer, fragment).commit();
                    }
                }
            });



            fabComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CommentActivity.class);
                    intent.putExtra("post", (Serializable) post);
                    context.startActivity(intent);
                }
            });



            ParseFile postImage = post.getImage();
            if (postImage != null)
                Glide.with(context).load(postImage.getUrl()).into(ivPostImage);

            fabFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Post post = posts.get(position);
                        try {
                           favorite(post);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        post.saveInBackground();

                    }
                }
            });
        }

        public void favorite(Post post) throws JSONException {
            if (post.isLiked()) {
                post.unlikePost();
                fabFavorite.clearColorFilter();
                fabFavorite.setImageResource(R.drawable.ic_likes);
            }
            else {
                post.likePost();
                Log.d("PostsAdapter", "in here!!!!!!!!!");
                fabFavorite.setImageResource(R.drawable.ic_likes_filled);
                fabFavorite.setColorFilter(context.getResources().getColor(R.color.medium_red));

            }
            int likes = post.getLikes().length();
            if (likes == 1)
                tvLikes.setText(String.format("%d", 1) + " like");
            else
                tvLikes.setText(String.format("%d", likes) + " likes");
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Post post = posts.get(position);
                detailPosition = position;
                Intent intent = new Intent((MainActivity) context, PostDetailActivity.class);
                intent.putExtra("PostDetails", (Serializable) post);
                ((MainActivity) context).startActivityForResult(intent, 230);
            }
        }
    }
}
