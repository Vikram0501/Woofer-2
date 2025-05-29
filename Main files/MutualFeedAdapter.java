package com.example.woofer;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MutualFeedAdapter extends RecyclerView.Adapter<MutualFeedAdapter.MyViewHolder>{
    Context context;
    ArrayList<User> Friends;
    Integer currentuser_id;
    ArrayList<Integer> Currentuser_friends;
    private final MutualInt mutualInt;
    public MutualFeedAdapter(Context context, ArrayList<User> Friends, MutualInt mutualInt, Integer currentuser_id, ArrayList<Integer> Currentuser_friends){
        this.context = context;
        this.Friends = Friends;
        this.mutualInt = mutualInt;
        this.currentuser_id = currentuser_id;
        this.Currentuser_friends = Currentuser_friends;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.friends, parent, false);

        return new MyViewHolder(view, mutualInt, Friends);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.username.setText(Friends.get(position).getUsername());
        if (Friends.get(position).getUserId() == currentuser_id){
            holder.addfriend.setText("You");
            holder.addfriend.setBackgroundColor(Color.parseColor("#0A0A0A"));
        }
        else if (Currentuser_friends.contains(Friends.get(position).getUserId())){
            holder.addfriend.setText("Already Friends");
            holder.addfriend.setBackgroundColor(Color.parseColor("#0A0A0A"));
        }
        else{
            holder.addfriend.setText("Add Friend");
        }
    }

    @Override
    public int getItemCount() {
        return Friends.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView username;
        Button addfriend;
        public MyViewHolder(@NonNull View itemView, MutualInt mutualInt, ArrayList<User> Friends) {
            super(itemView);
            username = itemView.findViewById(R.id.username_poster);
            addfriend = itemView.findViewById(R.id.view_post_button_poster);

            addfriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mutualInt != null){
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION){
                            if (addfriend.getText().equals("Add Friend")){
                                mutualInt.sendrequest(Friends.get(pos).getUserId());
                                addfriend.setText("Request Sent");
                                addfriend.setBackgroundColor(Color.parseColor("#0A0A0A"));
                            }
                        }
                    }
                }
            });
        }
    }
}
