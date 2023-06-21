package com.example.chatapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.chatapplication.R;
import java.util.ArrayList;
import java.util.List;
import models.Message;
import Adapter.MessageAdapter;

public class Chat extends AppCompatActivity {

    private List<Message> messageList = new ArrayList<>();
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        ImageView backArrow = findViewById(R.id.BArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(); // go back to the previous activity
            }
        });
        // Retrieve the display name and chat id
        Intent intent = getIntent();
        String displayName = intent.getStringExtra("displayName");
        int chatId = intent.getIntExtra("chatId", -1);
        String profilePic = intent.getStringExtra("profilePic");

        // Set the display name as the UserName TextView
        TextView userNameView = findViewById(R.id.UserName);
        userNameView.setText(displayName);

        // Set the profile picture
        ImageView profileImageView = findViewById(R.id.profileImage);
        byte[] imageBytes = Base64.decode(profilePic, Base64.DEFAULT);
        Glide.with(this).load(imageBytes).into(profileImageView);





        RecyclerView recyclerView = findViewById(R.id.chatRV);

        // Initialize messages
        // You will likely fetch these based on the chatId
        messageList.add(new Message("Hi, how are you?", "12:05 PM", false));
        messageList.add(new Message("I'm fine, thank you. How about you?", "12:06 PM", true));

        adapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }
}
