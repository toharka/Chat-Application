package com.example.chatapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Api.ApiClient;
import Api.ApiReq;
import models.AppDB;
import models.ChatModel;
import models.ChatsDao;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Contact extends AppCompatActivity {
    private RecyclerView chatRV;
    private String tokens;
    private String token;
    private List<ChatModel> chatList;
    private com.example.chatapplication.ChatAdapter chatAdapter;

    AppDB db;
    ChatsDao chatsDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        db = Room.databaseBuilder(getApplicationContext(), AppDB.class, "AppDB").build();
//        chatsDao= db.chatsDao();

        chatRV = findViewById(R.id.chatRV);
        chatList = new ArrayList<>();
        chatAdapter = new com.example.chatapplication.ChatAdapter(Contact.this, chatList);
        chatRV.setLayoutManager(new LinearLayoutManager(Contact.this)); // Adding a LinearLayoutManager
        chatRV.setAdapter(chatAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");  // Use the default value "" or any other value that you wish in case the token is not found



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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // logout menu item selected
        if (id == R.id.logout) {
            Intent intent = new Intent(Contact.this, SignInActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.groupChat) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter username");
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String userName = input.getText().toString();
                    Map<String, String> user = new HashMap<>();
                    user.put("username", userName);
                    ApiReq apiReq = ApiClient.getInstance().getApiInterface();
                    Call<ChatModel> call = apiReq.createChat("Bearer " + token, user);
                    call.enqueue(new Callback<ChatModel>() {
                        @Override
                        public void onResponse(Call<ChatModel> call, Response<ChatModel> response) {
                            if(response.isSuccessful()) {
                                // The chat has been successfully created. Update your UI here.
                                ChatModel newChat = response.body();
                                // Add the new chat to your chat list and notify the adapter.
                                chatList.add(newChat);
                                chatAdapter.notifyItemInserted(chatList.size() - 1);
                            } else {
                                Toast.makeText(Contact.this, "Unable to create chat: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ChatModel> call, Throwable t) {
                            Toast.makeText(Contact.this, "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Show the AlertDialog
            builder.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getChats() {
        Call<List<ChatModel>> call = ApiClient.getInstance().getApiInterface().getChats("Bearer " + token);
        call.enqueue(new Callback<List<ChatModel>>() {
            @Override
            public void onResponse(Call<List<ChatModel>> call, Response<List<ChatModel>> response) {
                if (response.isSuccessful()) {
                    List<ChatModel> chatResponseList = response.body();
                    if(chatResponseList != null){
                        chatList.clear();
                        chatList.addAll(chatResponseList);
                        chatAdapter.notifyDataSetChanged();
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
