package com.example.woofer;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.graphics.Color;
import android.os.Handler;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private List<Post> posts;         // Moved here
    private PostAdapter adapter;      // Moved here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.homepage_main);

        RecyclerView recyclerView = findViewById(R.id.postRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize posts and adapter
        posts = new ArrayList<>();
        posts.add(new Post("Post #1: First Post."));
        posts.add(new Post("Post #2: Second Post."));
        posts.add(new Post("Post #3: Third Post."));

        adapter = new PostAdapter(posts);
        recyclerView.setAdapter(adapter);

        // Now this works — has access to posts and adapter
        new Handler().postDelayed(() -> {
            posts.add(new Post("New post appears!"));
            adapter.notifyItemInserted(posts.size() - 1);
        }, 3000);


        SearchView searchView = findViewById(R.id.searchView);

// This line must be typed exactly
        int id = getResources().getIdentifier("search_src_text", "id", getPackageName());
        EditText searchEditText = searchView.findViewById(id);

// Change colors
        searchEditText.setTextColor(Color.WHITE);
        searchEditText.setHintTextColor(getResources().getColor(android.R.color.darker_gray));




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
