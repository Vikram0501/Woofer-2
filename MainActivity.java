package com.example.woofer;

import android.Manifest;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.PixelCopy;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.example.woofer.R;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    public User currentuser;
    private static final int REQUEST_READ_STORAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.loginactivity_main);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadImages();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void loadImages() {
        // Query external images
        Uri collection;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            collection = MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL);
        } else {
            collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED
        };

        String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

        try (Cursor cursor = getContentResolver().query(
                collection,
                projection,
                null,
                null,
                sortOrder
        )) {
            // Get first image
            if (cursor != null && cursor.moveToFirst()) {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                // Display in ImageView
                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageURI(contentUri);
            }
        }
    }
    public void editpicture(View v){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_STORAGE);
        } else{
            loadImages();
        }
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
                                    String password = list.get(2);
                                    String email = list.get(3);

                                    currentuser = new User(user_id, username, password, email);
                                    currentuser.importposts();
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
                                usernametxt.setText("user already exists");//**Placeholder**
                                passwordtxt.setText("");
                                confirmpasstxt.setText("");
                                emailtxt.setText("");
                            }
                            else if(responsebody.equals("ERR: MISSING FIELDS")){
                                usernametxt.setText("missing fields");//**Placeholder**
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
                                usernametxt.setText("user added");//**Placeholder**
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

    public void addpost(){
        TextView post = findViewById(R.id.AddPosttxt);
        String content = post.getText().toString();


    }
    //-------Buttons To Move Between Pages-------//
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
    }

    public void profilepage(View v){
        setContentView(R.layout.personal_profile_main2);
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


    public void deletepage(View v){setContentView(R.layout.delete_acc_main);}
    public void logoutpage(View v){
        setContentView(R.layout.logout_main);
    }
    public void mylikedpostspage(View v){
        setContentView(R.layout.list_my_liked_posts_main);
    }
    public void requestspage(View v){
        setContentView(R.layout.requests_list_main);
    }



}