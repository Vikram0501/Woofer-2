// PostAdapter.java
package com.example.woofer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private final List<Post> posts;

    public PostAdapter(List<Post> posts) {
        this.posts = posts;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView postText;
        ImageButton likeButton;

        public PostViewHolder(View itemView) {
            super(itemView);
            postText = itemView.findViewById(R.id.postText);
            likeButton = itemView.findViewById(R.id.likeButton);
        }
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.postText.setText(post.text);
        holder.likeButton.setImageResource(
                post.liked ? R.drawable.likehearticon : R.drawable.unlikehearticon
        );
        holder.likeButton.setOnClickListener(v -> {
            post.liked = !post.liked;
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
