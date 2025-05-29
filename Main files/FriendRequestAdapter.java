package com.example.woofer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.MyViewHolder>{
    private final RequestInt requestInt;
    Context context;
    ArrayList<User> FriendRequests;
    public FriendRequestAdapter(Context context, ArrayList<User> FriendRequests, RequestInt requestInt){
        this.context = context;
        this.FriendRequests = FriendRequests;
        this.requestInt = requestInt;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.requests_main, parent, false);

        return new MyViewHolder(view, requestInt, FriendRequests);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
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

        Button accept;
        Button reject;
        public MyViewHolder(@NonNull View itemView, RequestInt requestInt, ArrayList<User> FriendRequests) {
            super(itemView);
            username = itemView.findViewById(R.id.username_request);
            //profilepic = itemView.findViewById(R.id.profile_image_requests);

            accept = itemView.findViewById(R.id.Accept);
            reject = itemView.findViewById(R.id.Reject);

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (requestInt != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            requestInt.onAccept(FriendRequests.get(pos).getUserId(), pos);
                            FriendRequests.remove(pos);
                        }
                    }
                }
            });
            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (requestInt != null){
                        int pos = getAdapterPosition();

                        if (pos != RecyclerView.NO_POSITION){
                            requestInt.onReject(FriendRequests.get(pos).getUserId(), pos);
                            FriendRequests.remove(pos);
                        }
                    }
                }
            });
        }
    }
}
