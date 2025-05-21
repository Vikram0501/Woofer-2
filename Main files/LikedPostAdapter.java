package com.example.testing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LikedPostAdapter extends RecyclerView.Adapter<LikedPostAdapter.MyViewHolder> {
    Context context;
    ArrayList<User> likedby;
    private final ViewPostInt viewPostInt;
    public LikedPostAdapter(Context context, ArrayList<User> likedby, ViewPostInt viewPostInt){
        this.context = context;
        this.likedby = likedby;
        this.viewPostInt = viewPostInt;
    }
    @NonNull
    @Override
    public LikedPostAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_like_main, parent, false);
        return new LikedPostAdapter.MyViewHolder(view, viewPostInt);
    }

    @Override
    public void onBindViewHolder(@NonNull LikedPostAdapter.MyViewHolder holder, int position) {
        holder.username.setText(likedby.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return likedby.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        Button viewpost;
        public MyViewHolder(@NonNull View itemView, ViewPostInt viewPostInt) {
            super(itemView);
            username = itemView.findViewById(R.id.username_liked);
            viewpost = itemView.findViewById(R.id.view_post_button);

            viewpost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (viewPostInt != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            viewPostInt.viewpost(pos);
                        }
                    }

                }
            });
        }
    }
}
