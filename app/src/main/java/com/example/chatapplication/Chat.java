package com.example.chatapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapplication.models.AppDB;
import com.example.chatapplication.models.MessageDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.MessageAdapter;
import Api.ApiClient;
import Api.ApiReq;
import models.Message;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chat extends AppCompatActivity {

    private List<Message> messageList = new ArrayList<>();
    private MessageAdapter adapter;
    private EditText messageInput;
    private ApiReq apiReq;
    private String token;
    private int chatId;

    private AppDB appDB;
    private MessageDao messageDao;
    SharedPreferences sharedPreferences = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        sharedPreferences = getSharedPreferences("night",0);
        boolean bool = sharedPreferences.getBoolean("night_mode",true);
        if (bool){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize ApiReq from ApiClient
        apiReq = ApiClient.getInstance().getApiInterface();

        // Retrieve token and chatId
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        chatId = getIntent().getIntExtra("chatId", -1);

        // Initialize database
        appDB = AppDB.getInstance(getApplicationContext());
        messageDao = appDB.messageDao();

        ImageView backArrow = findViewById(R.id.BArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isNewMessage", true);
                editor.apply();

                onBackPressed();



                onBackPressed();

                // Send broadcast to inform Contact activity to refresh the chat list
                Intent intent = new Intent("NewMessageReceived");
                LocalBroadcastManager.getInstance(Chat.this).sendBroadcast(intent);
            }
        });

        Intent intent = getIntent();
        String displayName = intent.getStringExtra("displayName");
        String profilePic = intent.getStringExtra("profilePic");

        TextView userNameView = findViewById(R.id.UserName);
        userNameView.setText(displayName);

        ImageView profileImageView = findViewById(R.id.profileImage);
        byte[] imageBytes = Base64.decode(profilePic, Base64.DEFAULT);
        Glide.with(this).load(imageBytes).into(profileImageView);

        RecyclerView recyclerView = findViewById(R.id.chatRV);
        String currentUsername = sharedPreferences.getString("username", "");
        adapter = new MessageAdapter(messageList, currentUsername);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        messageInput = findViewById(R.id.entM);
        ImageView sendButton = findViewById(R.id.send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageInput.getText().toString();
                if (!message.isEmpty()) {
                    sendMessage(chatId, message);
                }
            }
        });



        messageDao.getMessages(chatId).observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                messageList.clear();
                messageList.addAll(messages);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void sendMessage(int chatId, String content) {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String currentUsername = sharedPreferences.getString("username", "");
        Map<String, String> body = new HashMap<>();
        body.put("msg", content);
        Call<Message> call = apiReq.sendMessage("Bearer " + token, chatId, body);
        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    messageInput.setText("");
                    Message message = response.body();
                    message.setChatId(chatId);
                    // Set the chatId
                    message.setSenderUsername(currentUsername); // Set sender's username
                    new Thread(() -> messageDao.insertMessage(message)).start();

                    // Send broadcast to inform Contact that a new message has been sent
                    Intent intent = new Intent("NewMessageReceived");
                    LocalBroadcastManager.getInstance(Chat.this).sendBroadcast(intent);


                } else {
                    Toast.makeText(Chat.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(Chat.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

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
