package com.example.woofer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.codec.digest.DigestUtils;
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

public class MainActivity extends AppCompatActivity {
    public User currentuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.loginactivity_main);

    }


    public void editpicture(View v){
        ImageView profilepic = findViewById(R.id.imageView);
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        ActivityResultLauncher<Intent> openGallery = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        Uri uriImage = o.getData().getData();
                        profilepic.setImageURI(uriImage);
                    }
                });
        openGallery.launch(intent);
    }


    //-------Login Button-------//
    public void login(View v){
        TextView usernametxt =  findViewById(R.id.Usernametxt);
        TextView passwordtxt =  findViewById(R.id.Passwordtxt);
        //**do password checks**
        String username = usernametxt.getText().toString();
        String password = DigestUtils.sha256Hex(passwordtxt.getText().toString());
        //**change from _GET to _POST in PHP**
        String url = "https://lamp.ms.wits.ac.za/home/s2798790/login.php?username=" + username + "&password=" + password;
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
                    String responsebody = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (responsebody.equals("ERR: USER NOT FOUND")){
                                //**add error message**
                                usernametxt.setText("");
                                passwordtxt.setText("");
                            }
                            else {
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

                                    currentuser = new User(user_id, username, email);
                                    profilepage(v);

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    });
                }
            }
        });
    }
    //-------Signup Button-------//
    public void signup(View v){
        TextView usernametxt =  findViewById(R.id.Usernametxt);
        TextView passwordtxt =  findViewById(R.id.Passwordtxt);
        TextView confirmpasstxt = findViewById(R.id.ConfirmPasswordtxt);
        TextView emailtxt = findViewById(R.id.EmailAddresstxt);

        String username = usernametxt.getText().toString();
        String password = DigestUtils.sha256Hex(passwordtxt.getText().toString());
        String confirm = DigestUtils.sha256Hex(confirmpasstxt.getText().toString());
        if (!password.equals(confirm)){
            usernametxt.setText("confirmed wrong");
            passwordtxt.setText("");
            confirmpasstxt.setText("");
            emailtxt.setText("");
            return;
        }

        String email = emailtxt.getText().toString();
        String url = "https://lamp.ms.wits.ac.za/home/s2798790/signup.php?username=" + username + "&password=" + password + "&email=" + email;
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
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (responsebody.equals("ERR: USERNAME ALREADY IN USE")){
                                usernametxt.setText("username already in use");
                                passwordtxt.setText("");
                                confirmpasstxt.setText("");
                                emailtxt.setText("");
                            }
                            else if(responsebody.equals("ERR: MISSING FIELDS")){
                                //**Placeholder**
                                passwordtxt.setText("");
                                confirmpasstxt.setText("");
                                emailtxt.setText("");
                            }
                            else if (responsebody.equals("ERR: FAILED TO REGISTER")){
                                usernametxt.setText("failed to register");//**Placeholder**
                                passwordtxt.setText("");
                                confirmpasstxt.setText("");
                                emailtxt.setText("");
                            }
                            else if (responsebody.equals("SUCCESS: USER REGISTERED")){
                                loginpage(v);//**Placeholder**
                                passwordtxt.setText("");
                                confirmpasstxt.setText("");
                                emailtxt.setText("");
                            }
                            else {
                                usernametxt.setText("unknown error");//**Placeholder**
                                passwordtxt.setText("");
                                confirmpasstxt.setText("");
                                emailtxt.setText("");
                            }
                        }
                    });
                }
            }
        });
    }

    public void addpost(View v){
        TextView post = findViewById(R.id.AddPosttxt);
        String content = post.getText().toString();

        //**change from _GET to _POST in PHP**
        String url = "https://lamp.ms.wits.ac.za/home/s2798790/addpost.php?user_id=" + currentuser.getUserId() + "&content=" + content;
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
                    String responsebody = response.body().string();
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (responsebody.equals("SUCCESS: POST ADDED")){
                                post.setText("added");//**Placeholder**
                                currentuser.importposts();
                            }
                            else{
                                post.setText("error");//**Placeholder**
                            }
                        }
                    });
                }
            }
        });

    }
    //-------Functions To Move Between Pages-------//
    public void signuppage(View v){
        setContentView(R.layout.signupactivity_main);
    }

    public void loginpage(View v){
        setContentView(R.layout.loginactivity_main);
    }

    public void addpostpage(View v){
        setContentView(R.layout.addpost_main);
    }

    public void homepage(View v){
        setContentView(R.layout.homepage_main);
        ArrayList<Post> postfeed = new ArrayList<>();
        for (Integer friendid : currentuser.getFriends()){
            postfeed.addAll(getposts(friendid));
        }
        RecyclerView postfeedview = findViewById(R.id.postfeedRecyclerView);

        postfeedview.setLayoutManager(new LinearLayoutManager(this));
        PostFeedAdapter adapter = new PostFeedAdapter(this,postfeed);
        postfeedview.setAdapter(adapter);


    }
    public ArrayList<Post> getposts(Integer UserId){
        ArrayList<Post> posts = new ArrayList<>();
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
        return posts;
    }
    public void profilepage(View v){
        setContentView(R.layout.personal_profile_main2);
        TextView username = findViewById(R.id.username);
        Button friendnum = findViewById(R.id.FollowerCount_btn);
        TextView postnum = findViewById(R.id.PostsCount_txt);
        RecyclerView myposts = findViewById(R.id.postRecyclerView);

        RecyclerView recyclerView = findViewById(R.id.postRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MyPostAdapter adapter = new MyPostAdapter(this, currentuser);
        recyclerView.setAdapter(adapter);

        username.setText(currentuser.getUsername());
        postnum.setText(Integer.toString(currentuser.numposts()));
        friendnum.setText(Integer.toString(currentuser.numfriends()));


    }

    public void notificationpage(View v){
        setContentView(R.layout.notifications_main);
    }

    public void editprofilepage(View v){
        setContentView(R.layout.edit_pfp);
    }
    public void settingspage(View v){
        setContentView(R.layout.settings_main);
    }

    public void mylikedpostspage(View v){
        setContentView(R.layout.list_my_liked_posts_main);
    }
    public void requestspage(View v){
        setContentView(R.layout.requests_list_main);

        RecyclerView friendrequestlist = findViewById(R.id.Requests_list);
        ArrayList<User> requests = new ArrayList<>();

        requests = getuser(currentuser.requests, requests);

        FriendRequestAdapter adapter = new FriendRequestAdapter(this, requests);

    }

    public void deletepage(View v){
        setContentView(R.layout.delete_acc_main);
    }
    public void logoutpage(View v){
        setContentView(R.layout.logout_main);
    }

    protected ArrayList<User> getuser(ArrayList<Integer> user_ids, ArrayList<User> users){
        for (Integer user_id : user_ids){
            String url = "https://lamp.ms.wits.ac.za/home/s2798790/getuser.php?user_id=" + user_id;
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
                        String responsebody = response.body().string();
                        if (responsebody.equals("ERR: USER NOT FOUND")){
                        }
                        else{
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

                                User user = new User(user_id, username, email);
                                users.add(user);

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            });
        }
        return users;
    }
}