package com.example.testing;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;

class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.MyViewHolder> {
    Context context;
    ArrayList<Post> posts;
    User currentuser;

    PostInt postInt;

    HashSet<Integer> userlikes;
    public  MyPostAdapter(Context context, User currentuser, PostInt postInt){
        this.context = context;
        this.currentuser = currentuser;
        this.posts = currentuser.posts;
        this.postInt = postInt;
        this.userlikes =currentuser.likedposts;
    }
    @NonNull
    @Override
    public MyPostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_post, parent, false);
        return new MyPostAdapter.MyViewHolder(v, postInt, posts, context);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostAdapter.MyViewHolder holder, int position) {
        holder.username.setText(currentuser.getUsername());
        holder.content.setText(posts.get(position).getContent());
        holder.datetime.setText(posts.get(position).getDatetime());
        holder.likes.setText(String.valueOf(posts.get(position).getLikes()));

        Drawable like = ContextCompat.getDrawable(context, R.drawable.likehearticon);
        Drawable unlike = ContextCompat.getDrawable(context, R.drawable.unlikehearticon);

        if (userlikes.contains(posts.get(position).getPost_id())){
            holder.likebutton.setImageDrawable(like);
        }
        else{
            holder.likebutton.setImageDrawable(unlike);
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView username, content, datetime, likes;
        ImageButton likebutton;

        Drawable like, unlike;
        public MyViewHolder(@NonNull View itemView, PostInt postInt, ArrayList<Post> posts, Context context) {
            super(itemView);
            username = itemView.findViewById(R.id.username_liked);
            content = itemView.findViewById(R.id.postText);
            datetime = itemView.findViewById(R.id.datetimeText);
            likes = itemView.findViewById(R.id.liketext);

            like = ContextCompat.getDrawable(context, R.drawable.likehearticon);
            unlike = ContextCompat.getDrawable(context, R.drawable.unlikehearticon);

            likebutton = itemView.findViewById(R.id.likeButton);
            likebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (postInt != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){

                            if (areDrawablesIdentical(likebutton.getDrawable(), like)) {
                                likebutton.setImageDrawable(unlike);
                                postInt.Unlike(pos, posts.get(pos).post_id);
                                posts.get(pos).setLikes(posts.get(pos).getLikes() -1);
                                likes.setText(String.valueOf(posts.get(pos).getLikes()));
                            } else if (areDrawablesIdentical(likebutton.getDrawable(), unlike)) {
                                likebutton.setImageDrawable(like);
                                postInt.Like(pos, posts.get(pos).post_id);
                                posts.get(pos).setLikes(posts.get(pos).getLikes() +1);
                                likes.setText(String.valueOf(posts.get(pos).getLikes()));
                            } else {
                                Log.d("Err", "like error");
                            }
                        }
                    }
                }
            });
        }
        public boolean areDrawablesIdentical(Drawable d1, Drawable d2) {
            if (d1 == null || d2 == null) return false;

            if (d1.getConstantState() != null && d2.getConstantState() != null) {
                return d1.getConstantState().equals(d2.getConstantState());
            }

            return false;
        }
    }
}
