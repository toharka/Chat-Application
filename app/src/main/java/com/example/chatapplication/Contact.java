package com.example.chatapplication;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
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
import okhttp3.ResponseBody;
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
    SharedPreferences sharedPreferences = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        sharedPreferences = getSharedPreferences("night",0);
        boolean bool = sharedPreferences.getBoolean("night_mode",true);
        if (bool){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        db = Room.databaseBuilder(getApplicationContext(), com.example.chatapplication.models.AppDB.class, "AppDB").build();
        chatsDao = db.chatsDao();

        refreshChats();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.settings){
            // Start the SettingsActivity
            Intent intent = new Intent(this, SettingsPage.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.logout) {
            // Get the username
            SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
            String currentUsername = sharedPreferences.getString("username", "");

            // Call API to delete token
            ApiReq apiReq = ApiClient.getInstance().getApiInterface();
            Call<ResponseBody> call = apiReq.deleteToken(currentUsername);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()) {
                        // After successfully deleting token, navigate to SignInActivity
                        Intent intent = new Intent(Contact.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(Contact.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(Contact.this, "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
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

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("LastActivity", getClass().getName());
        editor.apply();
    }
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Do something here when a new message is received
            refreshChats();
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("NewMessageReceived"));
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        boolean isNewMessage = sharedPreferences.getBoolean("isNewMessage", false);
        if (isNewMessage) {
            refreshChats();

            // Reset new message flag
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isNewMessage", false);
            editor.apply();
        }
    }


    @Override
    public void onRestart() {
        super.onRestart();
        // Refresh your data here
    }


    public void refreshChats() {
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
                Toast.makeText(Contact.this, "Failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        new GetChatsTask().execute(); // Fetch the updated chats from the local DB
    }
}
