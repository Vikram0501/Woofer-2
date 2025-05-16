package com.example.testing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.MyViewHolder> {
    Context context;
    ArrayList<Post> posts;
    User currentuser;
    public  MyPostAdapter(Context context, User currentuser){
        this.context = context;
        this.currentuser = currentuser;
        this.posts = currentuser.posts;
    }
    @NonNull
    @Override
    public MyPostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_post, parent, false);
        return new MyPostAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostAdapter.MyViewHolder holder, int position) {
        holder.username.setText(currentuser.getUsername());
        holder.content.setText(posts.get(position).getContent());
        holder.datetime.setText(posts.get(position).getDatetime());
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
