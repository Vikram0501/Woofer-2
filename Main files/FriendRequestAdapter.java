package com.example.testing;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.MyViewHolder>{
    Context context;
    ArrayList<User> FriendRequests;
    public FriendRequestAdapter(Context context, ArrayList<User> FriendRequests){
        this.context = context;
        this.FriendRequests = FriendRequests;
    }
    @NonNull
    @Override
    public FriendRequestAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.requests_main, parent, false);

        return new FriendRequestAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestAdapter.MyViewHolder holder, int position) {
        Log.d("AdapterBind", "Binding: " + FriendRequests.get(position).getUsername());
        holder.username.setText(FriendRequests.get(position).getUsername());
        //holder.profilepic.
    }

    @Override
    public int getItemCount() {
        return FriendRequests.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView username;
        ImageView profilepic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username_request);
            //profilepic = itemView.findViewById(R.id.profile_image_requests);
        }
    }
}
