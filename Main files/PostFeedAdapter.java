package com.example.woofer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostFeedAdapter extends RecyclerView.Adapter<PostFeedAdapter.MyViewHolder> {
    Context context;
    ArrayList<Post> posts;
    public  PostFeedAdapter(Context context, ArrayList<Post> posts){
        this.context = context;
        this.posts = posts;
    }
    @NonNull
    @Override
    public PostFeedAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_post, parent, false);
        return new PostFeedAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostFeedAdapter.MyViewHolder holder, int position) {
        //holder.username.setText(currentuser.getUsername());
        holder.content.setText(posts.get(position).getContent());
        holder.datetime.setText(posts.get(position).getDatatime());
        holder.likes.setText(String.valueOf(posts.get(position).getLikes()));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView username, content, datetime, likes;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username_liked);
            content = itemView.findViewById(R.id.postText);
            datetime = itemView.findViewById(R.id.datetimeText);
            likes = itemView.findViewById(R.id.liketext);
        }
    }
}
