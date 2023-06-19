package com.example.chatapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import Api.ApiClient;
import models.ChatModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Contact extends AppCompatActivity {
    private RecyclerView chatRV;
    private String tokens;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        chatRV = findViewById(R.id.chatRV);

        //SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        //tokens = sharedPreferences.getString("token", "");
        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6ImFhYSIsImlhdCI6MTY4NzE4NzM2MywiZXhwIjoxNjg3MTkwOTYzfQ.2d0VyEOQoKkVaAFZURpslH2ivJplDKD-26MoTIQbY_s";
        if (token.isEmpty()) {
            Toast.makeText(this, "No token found", Toast.LENGTH_SHORT).show();
            return;
        }

        getChats();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void getChats() {
        Call<List<ChatModel>> call = ApiClient.getInstance().getApiInterface().getChats("Bearer " + token);
        call.enqueue(new Callback<List<ChatModel>>() {
            @Override
            public void onResponse(Call<List<ChatModel>> call, Response<List<ChatModel>> response) {
                if (response.isSuccessful()) {
                    List<ChatModel> chatList = response.body();
                    if(chatList != null){
                        com.example.chatapplication.ChatAdapter chatAdapter = new com.example.chatapplication.ChatAdapter(Contact.this, chatList);

                        chatRV.setLayoutManager(new LinearLayoutManager(Contact.this)); // Adding a LinearLayoutManager
                        chatRV.setAdapter(chatAdapter);
                    }
                } else {
                    Toast.makeText(Contact.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChatModel>> call, Throwable t) {
                Toast.makeText(Contact.this, "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
