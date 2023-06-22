package com.example.chatapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import models.ChatModel;
import models.ChatsDao;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Contact extends AppCompatActivity {
    private RecyclerView chatRV;
    private String token;
    private List<ChatModel> chatList;
    private com.example.chatapplication.ChatAdapter chatAdapter;

    com.example.chatapplication.models.AppDB db;
    ChatsDao chatsDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        db = Room.databaseBuilder(getApplicationContext(), com.example.chatapplication.models.AppDB.class, "AppDB").build();
        chatsDao = db.chatsDao();
        new Thread(new Runnable() {
            @Override
            public void run() {
                chatsDao.deleteAllChats();
            }
        }).start();
        chatRV = findViewById(R.id.chatRV);
        chatList = new ArrayList<>();
        chatAdapter = new com.example.chatapplication.ChatAdapter(Contact.this, chatList);
        chatRV.setLayoutManager(new LinearLayoutManager(Contact.this));
        chatRV.setAdapter(chatAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "No token found", Toast.LENGTH_SHORT).show();
            return;
        }
        // Add this in your onCreate method, after you check if the token is empty
        ApiReq apiReq = ApiClient.getInstance().getApiInterface();
        Call<List<ChatModel>> call = apiReq.getChats("Bearer " + token);
        call.enqueue(new Callback<List<ChatModel>>() {
            @Override
            public void onResponse(Call<List<ChatModel>> call, Response<List<ChatModel>> response) {
                if(response.isSuccessful()) {
                    List<ChatModel> fetchedChats = response.body();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (ChatModel chatModel : fetchedChats) {
                                chatsDao.insert(chatModel); // Insert fetched chats into local DB
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new GetChatsTask().execute(); // Now fetch chats from local DB for display
                                }
                            });
                        }
                    }).start();
                } else {
                    Toast.makeText(Contact.this, "Unable to fetch chats: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChatModel>> call, Throwable t) {
                Toast.makeText(Contact.this, "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        new GetChatsTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
                                ChatModel newChat = response.body();
                                new InsertChatTask().execute(newChat);
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

            builder.show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetChatsTask extends AsyncTask<Void, Void, List<ChatModel>> {
        @Override
        protected List<ChatModel> doInBackground(Void... voids) {
            return chatsDao.findChats();
        }

        @Override
        protected void onPostExecute(List<ChatModel> chatModels) {
            chatList.clear();
            chatList.addAll(chatModels);
            chatAdapter.notifyDataSetChanged();
        }
    }

    private class InsertChatTask extends AsyncTask<ChatModel, Void, Void> {
        @Override
        protected Void doInBackground(ChatModel... chatModels) {
            chatsDao.insert(chatModels[0]);
            return null;
        }
    }
}
