package com.example.testing;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class User {
    int UserId;
    String Username;
    String Email;

    ArrayList<Post> posts;
    ArrayList<Integer> friends;
    ArrayList<Integer> requests;

    HashSet<Integer> likedposts;


    public User(int i, String u, String e){
        UserId = i;
        Username = u;
        Email = e;
        posts = new ArrayList<Post>();
        friends = new ArrayList<Integer>();
        requests = new ArrayList<Integer>();
        likedposts = new HashSet<>();
    }
    public void importlikes(){
        likedposts = new HashSet<>();
        String url = "https://lamp.ms.wits.ac.za/home/s2798790/getlikes.php?user_id=" + UserId;
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
                    if (!responsebody.equals("NO LIKES")){
                        try {
                            JSONArray ja = new JSONArray(responsebody);
                            ArrayList<String> list = new ArrayList<String>();
                            for (int i=0; i<ja.length();i++){
                                Iterator<String> keys = ja.getJSONObject(i).keys();
                                while (keys.hasNext()){
                                    list.add(ja.getJSONObject(i).getString(keys.next()));
                                }
                                int post_id = Integer.parseInt(list.get(0));
                                int user_id = Integer.parseInt(list.get(1));

                                likedposts.add(post_id);
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
    public void importposts(){
        posts.clear();
        String url = "https://lamp.ms.wits.ac.za/home/s2798790/importposts.php?user_id=" + UserId;
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
                            for (int i=0; i<ja.length();i++){
                                Iterator<String> keys = ja.getJSONObject(i).keys();
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

    public void importfriends(){
        String url = "https://lamp.ms.wits.ac.za/home/s2798790/importfriends.php?user_id=" + UserId;
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
                    if (!responsebody.equals("NO FRIENDS")){
                        try {
                            JSONArray ja = new JSONArray(responsebody);
                            ArrayList<String> list = new ArrayList<String>();
                            for (int i=0; i<ja.length();i++){
                                Iterator<String> keys = ja.getJSONObject(i).keys();
                                while (keys.hasNext()){
                                    list.add(ja.getJSONObject(i).getString(keys.next()));
                                }
                                int user_sender = Integer.parseInt(list.get(0));
                                int user_receiver = Integer.parseInt(list.get(1));

                                if (user_sender == UserId){
                                    friends.add(user_receiver);
                                }
                                else if (user_receiver == UserId){
                                    friends.add(user_sender);
                                }
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

    public void importrequests(){
        String url = "https://lamp.ms.wits.ac.za/home/s2798790/importrequests.php?user_id=" + UserId;
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
                    if (!responsebody.equals("NO REQUESTS")){
                        try {
                            JSONArray ja = new JSONArray(responsebody);
                            ArrayList<String> list = new ArrayList<String>();
                            for (int i=0; i<ja.length();i++){
                                Iterator<String> keys = ja.getJSONObject(i).keys();
                                while (keys.hasNext()){
                                    list.add(ja.getJSONObject(i).getString(keys.next()));
                                }
                                int user_sender = Integer.parseInt(list.get(0));
                                int user_receiver = Integer.parseInt(list.get(1));

                                if (user_sender == UserId){
                                    requests.add(user_receiver);
                                }
                                else if (user_receiver == UserId){
                                    requests.add(user_sender);
                                }
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

    public int numposts(){
        return posts.size();
    }
    public int numfriends(){
        return friends.size();
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

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public ArrayList<Integer> getFriends() {
        return friends;
    }

    public ArrayList<Integer> getRequests() {
        return requests;
    }

    public int getposofreq(int user_id){
        for (int i=0; i<requests.size(); i++){
            if (requests.get(i) == user_id){
                return i;
            }
        }
        return -1;
    }
}
