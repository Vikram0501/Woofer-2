package com.example.testing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PostFeedAdapter extends RecyclerView.Adapter<PostFeedAdapter.MyViewHolder> {
    Context context;
    ArrayList<Post> posts;
    public  PostFeedAdapter(Context context, ArrayList<Post> posts){
        this.context = context;
        this.posts = posts;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.item_post, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        getuser(posts.get(position).user_id, new PostFeedAdapter.UserCallback() {

            @Override
            public void onUserReceived(User user) {
                holder.username.setText(user.getUsername());
            }

            @Override
            public void onError(Exception e) {

            }
        });
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
    public interface UserCallback {
        void onUserReceived(User user);
        void onError(Exception e);
    }

    protected User getuser(Integer user_id, UserCallback callback){
        User user = new User(0, null, null);
        String url = "https://lamp.ms.wits.ac.za/home/s2798790/getuser.php?user_id=" + user_id;
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                callback.onError(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responsebody = response.body().string();
                    if (!responsebody.equals("ERR: USER NOT FOUND")) {
                        try {
                            JSONObject jo = new JSONObject(responsebody);
                            ArrayList<String> list = new ArrayList<String>();
                            Iterator<String> keys = jo.keys();
                            while (keys.hasNext()){
                                list.add(jo.getString(keys.next()));
                            }
                            int user_id = Integer.parseInt(list.get(0));
                            String username = list.get(1);
                            String email = list.get(3);

                            user.setUserId(user_id);
                            user.setUsername(username);
                            user.setEmail(email);

                            callback.onUserReceived(user);
                        } catch (JSONException e) {
                            callback.onError(e);
                        }
                    } else {

                    }
                } else {
                    callback.onError(new IOException("Unsuccessful response"));
                }
            }
        });
        return user;
    }
}
