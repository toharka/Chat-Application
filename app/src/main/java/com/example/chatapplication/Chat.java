package com.example.chatapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Adapter.MessageAdapter;
import models.Message;

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

    @Override
    public boolean onCreateOptionsMenu(Menu settings) {
        getMenuInflater().inflate(R.menu.settings, settings);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            // Start the SettingsActivity
            Intent intent = new Intent(this, SettingsPage.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
