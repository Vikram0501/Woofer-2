package com.example.testing;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testing.Post;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements RequestInt, PostInt, ViewPostInt, FriendInt, MutualInt {
    public User currentuser;
    FriendRequestAdapter reqadapter;
    FriendFeedAdapter friendadapter;
    MutualFeedAdapter mutualadapter;
    MutualFeedAdapter searchadapter;
    PostFeedAdapter postfeedadapter;
    PostFeedAdapter mylikedadapter;
    PostFeedAdapter friendpostadapter;
    MyPostAdapter mypostsadapter;
    LikedPostAdapter likedpostadapter;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.loginactivity_main);

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
                                Toast.makeText(getApplicationContext(), "Wrong Username/Password", Toast.LENGTH_SHORT).show();
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
                                    currentuser.importposts();
                                    currentuser.importfriends();
                                    currentuser.importrequests();
                                    currentuser.importlikes();
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
                                Toast.makeText(getApplicationContext(), "Username in Use Already", Toast.LENGTH_SHORT).show();
                                usernametxt.setText("");
                                passwordtxt.setText("");
                                confirmpasstxt.setText("");
                                emailtxt.setText("");
                            }
                            else if(responsebody.equals("ERR: MISSING FIELDS")){
                                Toast.makeText(getApplicationContext(), "Fill in All Fields", Toast.LENGTH_SHORT).show();
                                usernametxt.setText("");
                                passwordtxt.setText("");
                                confirmpasstxt.setText("");
                                emailtxt.setText("");
                            }
                            else if (responsebody.equals("ERR: FAILED TO REGISTER")){
                                Toast.makeText(getApplicationContext(), "Failed to Register", Toast.LENGTH_SHORT).show();
                                usernametxt.setText("");
                                passwordtxt.setText("");
                                confirmpasstxt.setText("");
                                emailtxt.setText("");
                            }
                            else if (responsebody.equals("SUCCESS: USER REGISTERED")){
                                Toast.makeText(getApplicationContext(), "Signed Up", Toast.LENGTH_SHORT).show();
                                loginpage(v);
                                passwordtxt.setText("");
                                confirmpasstxt.setText("");
                                emailtxt.setText("");
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Unknown Error", Toast.LENGTH_SHORT).show();
                                usernametxt.setText("");
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
                                post.setText("");
                                Toast.makeText(getApplicationContext(), "Post Added", Toast.LENGTH_SHORT).show();
                                currentuser.importposts();
                            }
                            else{
                                post.setText("");
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
    //-------Functions To Move Between Pages-------//
    public void signuppage(View v){
        setContentView(R.layout.signup_main);
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
        RecyclerView postfeedview = findViewById(R.id.postfeedRecyclerView);
        postfeedview.setLayoutManager(new LinearLayoutManager(this));
        postfeedadapter = new PostFeedAdapter(this, postfeed, this, currentuser.likedposts);
        postfeedview.setAdapter(postfeedadapter);


        for (Integer friendid : currentuser.getFriends()) {
            getposts(friendid, new PostCallback() {
                @Override
                public void onPostsReceived(ArrayList<Post> posts) {
                    runOnUiThread(() -> {
                        postfeed.addAll(posts);
                        postfeedadapter.notifyDataSetChanged();
                    });
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        }

        androidx.appcompat.widget.SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getsearch(query, new SearchCallback() {
                    @Override
                    public void onSearchReceived(ArrayList<User> users) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setContentView(R.layout.search_results);
                                RecyclerView searchlist = findViewById(R.id.Search_list);
                                searchlist.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                                searchadapter = new MutualFeedAdapter(MainActivity.this, users, MainActivity.this, currentuser.UserId, currentuser.friends);
                                searchlist.setAdapter(searchadapter);
                            }
                        });

                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void profilepage(View v){
        setContentView(R.layout.personal_profile_main);

        TextView username = findViewById(R.id.username);
        Button friendnum = findViewById(R.id.FollowerCount_btn);
        TextView postnum = findViewById(R.id.PostsCount_txt);

        RecyclerView recyclerView = findViewById(R.id.postRecyclerView);
        username.setText(currentuser.getUsername());
        if (count == 0){
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            ArrayList<Post> postfeed = new ArrayList<>();

            PostFeedAdapter adapter = new PostFeedAdapter(this, postfeed, this, currentuser.likedposts);
            recyclerView.setAdapter(adapter);
            getposts(currentuser.UserId, new PostCallback() {
                @Override
                public void onPostsReceived(ArrayList<Post> posts) {
                    runOnUiThread(() -> {
                        postfeed.addAll(posts);
                        adapter.notifyDataSetChanged();
                        postnum.setText(Integer.toString(postfeed.size()));
                    });
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
            getfriends(currentuser.UserId, new FriendCallback() {
                @Override
                public void onFriendsReceived(ArrayList<Integer> friends) {
                    runOnUiThread(() -> {
                        friendnum.setText(Integer.toString(friends.size()));
                    });
                }

                @Override
                public void onError(Exception e) {

                }
            });
            count ++;
        }
        else{
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            mypostsadapter = new MyPostAdapter(this, currentuser, this);
            recyclerView.setAdapter(mypostsadapter);


            postnum.setText(Integer.toString(currentuser.numposts()));
            friendnum.setText(Integer.toString(currentuser.numfriends()));
        }



    }

    public void notificationpage(View v){
        setContentView(R.layout.notifications_new);

        ArrayList<User> likedby = new ArrayList<>();
        RecyclerView likedpostfeed = findViewById(R.id.likes_recycler_view);
        likedpostfeed.setLayoutManager(new LinearLayoutManager(this));
        likedpostadapter = new LikedPostAdapter(this, likedby, this);
        likedpostfeed.setAdapter(likedpostadapter);
        for (Post post : currentuser.posts){
            int post_id = post.getPost_id();
            getlikeby(post_id, new LikedbyCallback() {
                @Override
                public void onLikesReceived(ArrayList<Integer> user_ids) {
                    for (Integer user_id : user_ids){
                        if (user_id != currentuser.getUserId()){
                            getuser((Integer) user_id, new UserCallback() {
                                @Override
                                public void onUserReceived(User user) {
                                    runOnUiThread(() -> {
                                        likedby.add(user);
                                        likedpostadapter.notifyDataSetChanged();
                                    });
                                }

                                @Override
                                public void onError(Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    }
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void editprofilepage(View v){
        setContentView(R.layout.new_edit_pfp);
        EditText usernametxt = findViewById(R.id.UsernameEditText);
        EditText emailtxt = findViewById(R.id.emailEditText);
        EditText passwordtxt = findViewById(R.id.passwordEditText);
        usernametxt.setText("");
        emailtxt.setText("");
        passwordtxt.setText("");
    }
    public void editprofile(View v){
        EditText usernametxt = findViewById(R.id.UsernameEditText);
        EditText emailtxt = findViewById(R.id.emailEditText);
        EditText passwordtxt = findViewById(R.id.passwordEditText);
        String username = String.valueOf(usernametxt.getText());
        String email = String.valueOf(emailtxt.getText());
        String password = String.valueOf(passwordtxt.getText());
        if (!password.isBlank()){
            password = DigestUtils.sha256Hex(passwordtxt.getText().toString());
        }
        String url = "https://lamp.ms.wits.ac.za/home/s2798790/editprofile.php?user_id=" + currentuser.getUserId() +"&username=" + username + "&email=" + email +"&password=" + password;
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
                            if (responsebody.equals("SUCCESS: DETAILS UPDATED")){
                                Toast.makeText(getApplicationContext(), "Edits Made", Toast.LENGTH_SHORT).show();
                                usernametxt.setText("");
                                emailtxt.setText("");
                                passwordtxt.setText("");
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                usernametxt.setText("");
                                emailtxt.setText("");
                                passwordtxt.setText("");
                            }

                        }
                    });
                }
            }
        });

    }
    public void settingspage(View v){
        setContentView(R.layout.settings_main);
    }

    public void mylikedpostspage(View v){
        setContentView(R.layout.list_my_liked_posts_main);
        RecyclerView mylikedpostsfeed = findViewById(R.id.my_liked_posts_list);
        ArrayList<Post> postfeed = new ArrayList<>();
        mylikedpostsfeed.setLayoutManager(new LinearLayoutManager(this));
        mylikedadapter = new PostFeedAdapter(this, postfeed, this, currentuser.likedposts);
        mylikedpostsfeed.setAdapter(mylikedadapter);
        for (Integer friendid : currentuser.getFriends()) {
            getposts(friendid, new PostCallback() {
                @Override
                public void onPostsReceived(ArrayList<Post> posts) {
                    runOnUiThread(() -> {
                        for (Post post : posts){
                            if (currentuser.likedposts.contains(post.getPost_id())){
                                postfeed.add(post);
                                mylikedadapter.notifyDataSetChanged();
                            }
                        }

                    });
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
    public void requestspage(View v){
        setContentView(R.layout.requests_list_main);

        RecyclerView friendrequestlist = findViewById(R.id.Requests_list);
        ArrayList<User> requests = new ArrayList<>();
        friendrequestlist.setLayoutManager(new LinearLayoutManager(this));
        reqadapter = new FriendRequestAdapter(this, requests, this);
        friendrequestlist.setAdapter(reqadapter);

        for (Integer request_id : currentuser.requests){
            getuser(request_id, new UserCallback() {
                @Override
                public void onUserReceived(User user) {
                    runOnUiThread(() -> {
                        requests.add(user);
                        reqadapter.notifyDataSetChanged();
                    });
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void deletepage(View v){
        setContentView(R.layout.delete_acc_main);
    }
    public void logoutpage(View v){
        setContentView(R.layout.logout_main);
    }

    public void friendpage(View v){
        setContentView(R.layout.friends_list);

        RecyclerView friendlist = findViewById(R.id.Friends_list);
        //2800630
        ArrayList<User> friends = new ArrayList<>();
        friendlist.setLayoutManager(new LinearLayoutManager(this));
        friendadapter = new FriendFeedAdapter(this, friends, this);
        friendlist.setAdapter(friendadapter);

        for (Integer friend_id : currentuser.friends){
            getuser(friend_id, new UserCallback() {
                @Override
                public void onUserReceived(User user) {
                    runOnUiThread(() -> {
                        friends.add(user);
                        friendadapter.notifyDataSetChanged();
                    });
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    //Interface functions and MISC
    @Override
    public void onAccept(int user_id, int position) {
        String url = "https://lamp.ms.wits.ac.za/home/s2798790/acceptfriend.php?user1_id=" + user_id + "&user2_id=" + currentuser.getUserId();
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
                            if (responsebody.equals("SUCCESS: FRIEND ADDED")){
                                remove(user_id, position);
                                currentuser.friends.add(user_id);
                            }
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onReject(int user_id, int position) {
        String url = "https://lamp.ms.wits.ac.za/home/s2798790/rejectfriend.php?user1_id=" + user_id + "&user2_id=" + currentuser.getUserId();
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
                            if (responsebody.equals("SUCCESS: REJECTED")){
                                remove(user_id, position);
                                Log.d("Removed", "True");
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void remove(int user_id, int position) {
        currentuser.requests.remove(currentuser.getposofreq(user_id));
        reqadapter.notifyItemRemoved(position);
    }

    @Override
    public void Like(int position, int post_id) {
        String url = "https://lamp.ms.wits.ac.za/home/s2798790/like.php?post_id=" + post_id + "&user_id=" + currentuser.getUserId();
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
                            if (!responsebody.equals("SUCCESS: LIKE ADDED")) {
                                Log.d("ERROR", responsebody);
                            }
                        }
                    });
                }
            }
        });
        currentuser.likedposts.add(post_id);
    }

    @Override
    public void Unlike(int position, int post_id) {
        String url = "https://lamp.ms.wits.ac.za/home/s2798790/unlike.php?post_id=" + post_id + "&user_id=" + currentuser.getUserId();
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
                            if (!responsebody.equals("SUCCESS: LIKE REMOVED")) {
                                Log.d("ERROR", responsebody);
                            }
                        }
                    });
                }
            }
        });
        currentuser.likedposts.remove(post_id);
    }

    @Override
    public void viewpost(int pos) {
        profilepage(null);
    }

    @Override
    public void friendprofile(int pos, User user) {
        setContentView(R.layout.friend_profile);
        TextView friend_username = findViewById(R.id.friend_username);
        TextView friend_postnum = findViewById(R.id.friend_postscount_txt);
        Button friend_friendnum = findViewById(R.id.friend_followercount_btn);

        friend_username.setText(user.getUsername());
        ArrayList<User> mutuals = new ArrayList<>();

        RecyclerView friendpostfeed = findViewById(R.id.friendpostRecyclerView);
        ArrayList<Post> friendposts = new ArrayList<>();
        friendpostfeed.setLayoutManager(new LinearLayoutManager(this));
        friendpostadapter = new PostFeedAdapter(this, friendposts, this, currentuser.likedposts);
        friendpostfeed.setAdapter(friendpostadapter);

        getposts(user.UserId, new PostCallback() {
            @Override
            public void onPostsReceived(ArrayList<Post> posts) {
                runOnUiThread(() -> {
                    friendposts.addAll(posts);
                    friendpostadapter.notifyDataSetChanged();
                    friend_postnum.setText(String.valueOf(posts.size()));
                });

            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

        getfriends(user.UserId, new FriendCallback() {
            @Override
            public void onFriendsReceived(ArrayList<Integer> friends_ids) {
                runOnUiThread(() -> {
                    friend_friendnum.setText(String.valueOf(friends_ids.size()));
                    for (Integer friend_id : friends_ids){
                        getuser(friend_id, new UserCallback() {
                            @Override
                            public void onUserReceived(User user) {
                                runOnUiThread(() -> {
                                    mutuals.add(user);
                                });
                            }

                            @Override
                            public void onError(Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }

                });
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

        friend_friendnum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.mutuals_list);
                ImageButton back = findViewById(R.id.imageButton);
                RecyclerView mutuallist = findViewById(R.id.Mutuals_list);
                mutuallist.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                mutualadapter = new MutualFeedAdapter(MainActivity.this, mutuals, MainActivity.this, currentuser.UserId, currentuser.friends);
                mutuallist.setAdapter(mutualadapter);

                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        friendprofile(0, user);
                    }
                });
            }
        });
    }

    @Override
    public void sendrequest(int user_id) {
        String url = "https://lamp.ms.wits.ac.za/home/s2798790/friendrequest.php?user1_id=" + currentuser.getUserId() + "&user2_id=" + user_id;
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
                            if (responsebody.equals("SUCCESS: REQUEST SENT")){
                            }
                            else{
                                Log.d("Err", "Friend Request error");
                            }
                        }
                    });
                }
            }
        });
    }

    public interface FriendCallback {
        void onFriendsReceived(ArrayList<Integer> friends);
        void onError(Exception e);
    }

    protected  void getfriends(Integer user_id, MainActivity.FriendCallback callback){
        ArrayList<Integer> friends = new ArrayList<>();
        String url = "https://lamp.ms.wits.ac.za/home/s2798790/importfriends.php?user_id=" + user_id;
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
                    if (!responsebody.equals("NO FRIENDS")) {
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

                                if (user_sender == user_id){
                                    friends.add(user_receiver);
                                }
                                else if (user_receiver == user_id){
                                    friends.add(user_sender);
                                }
                                list.clear();
                            }

                            callback.onFriendsReceived(friends);
                        } catch (JSONException e) {
                            callback.onError(e);
                        }
                    } else {
                        callback.onFriendsReceived(new ArrayList<>());
                    }
                } else {
                    callback.onError(new IOException("Unsuccessful response"));
                }
            }
        });
    }

    public interface UserCallback {
        void onUserReceived(User user);
        void onError(Exception e);
    }

    public interface SearchCallback {
        void onSearchReceived(ArrayList<User> users);
        void onError(Exception e);
    }

    protected void getsearch(String query, MainActivity.SearchCallback callback){
        ArrayList<User> users = new ArrayList<>();
        String url = "https://lamp.ms.wits.ac.za/home/s2798790/search.php?search=" + query;
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
                            JSONArray ja = new JSONArray(responsebody);
                            ArrayList<String> list = new ArrayList<String>();
                            for (int i=0; i<ja.length();i++){
                                Iterator<String> keys = ja.getJSONObject(i).keys();
                                while (keys.hasNext()){
                                    list.add(ja.getJSONObject(i).getString(keys.next()));
                                }
                                int user_id = Integer.parseInt(list.get(0));
                                String username = list.get(1);
                                String email = list.get(3);

                                User user = new User(user_id, username, email);
                                users.add(user);
                                list.clear();
                            }
                            callback.onSearchReceived(users);
                        } catch (JSONException e) {
                            callback.onError(e);
                        }
                    } else {
                        callback.onSearchReceived(new ArrayList<>());
                    }
                } else {
                    callback.onError(new IOException("Unsuccessful response"));
                }
            }
        });
    }

    protected void getuser(Integer user_id, MainActivity.UserCallback callback){
        User user = new User(0, null, null);
        String url = "https://lamp.ms.wits.ac.za/home/s2798790/getuser.php?user_id=" + user_id;
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();


        //2800630
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
    }

    public interface PostCallback {
        void onPostsReceived(ArrayList<Post> posts);
        void onError(Exception e);
    }

    protected void getposts(Integer UserId, PostCallback callback){
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
                callback.onError(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responsebody = response.body().string();
                    if (!responsebody.equals("NO POSTS")) {
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
                            callback.onPostsReceived(posts);
                        } catch (JSONException e) {
                            callback.onError(e);
                        }
                    } else {
                        callback.onPostsReceived(new ArrayList<>());
                    }
                } else {
                    callback.onError(new IOException("Unsuccessful response"));
                }
            }
        });
    }

    public interface LikedbyCallback{
        void onLikesReceived(ArrayList<Integer> user_id);
        void onError(Exception e);
    }
    protected  void getlikeby(Integer post_id, LikedbyCallback callback){
        ArrayList<Integer> users = new ArrayList<>();
        String url = "https://lamp.ms.wits.ac.za/home/s2798790/likedby.php?post_id=" + post_id;
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
                    if (!responsebody.equals("NO LIKES")) {
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

                                users.add(user_id);
                                list.clear();
                            }
                            callback.onLikesReceived(users);
                        } catch (JSONException e) {
                            callback.onError(e);
                        }
                    } else {
                        callback.onLikesReceived(new ArrayList<>());
                    }
                } else {
                    callback.onError(new IOException("Unsuccessful response"));
                }
            }
        });
    }
}