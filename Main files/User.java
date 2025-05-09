package com.example.woofer;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class User {
    int UserId;
    String Username;
    String Password;
    String Email;

    ArrayList<Post> posts;

    public User(int i, String u, String p, String e){
        UserId = i;
        Username = u;
        Password = p;
        Email = e;
    }

    public void importposts(){
        String url = "https://lamp.ms.wits.ac.za/home/s2798790/importposts.php?userid=" + UserId;
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    final String responsebody = response.body().string();
                    if (!responsebody.equals("NO POSTS")){
                        try {
                            JSONArray ja = new JSONArray(responsebody);
                            ArrayList<String> list = new ArrayList<String>();
                            Iterator<String> keys = ja.getJSONObject(0).keys();
                            for (int i=0; i<ja.length();i++){
                                while (keys.hasNext()){
                                    list.add(ja.getJSONObject(i).getString(keys.next()));
                                }
                                int post_id = Integer.parseInt(list.get(0));
                                int user_id = Integer.parseInt(list.get(1));
                                String content = list.get(2);
                                String datetime = list.get(3);
                                int likes = Integer.parseInt(list.get(4));

                                Post post = new Post(post_id,user_id,content,datetime,likes);
                                posts.add(post);
                                list.clear();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                }
            }
        });
    }
    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
