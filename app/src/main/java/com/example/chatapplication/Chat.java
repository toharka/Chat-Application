package com.example.chatapplication;
import android.os.Bundle;
import models.Message;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapplication.R;

import java.util.ArrayList;
import java.util.List;

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
        RecyclerView recyclerView = findViewById(R.id.chatRV);

        // Initialize messages
        messageList.add(new Message("Hi, how are you?", "12:05 PM", false));
        messageList.add(new Message("I'm fine, thank you. How about you?", "12:06 PM", true));
        // Add more messages as needed...

        adapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }
}
