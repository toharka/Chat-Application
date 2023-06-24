package com.example.chatapplication.models;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import models.ChatModel;
import models.ChatsDao;
import models.Message;

@Database(entities = {ChatModel.class, Message.class}, version = 1, exportSchema = false)
public abstract class AppDB extends RoomDatabase {
    public abstract ChatsDao chatsDao();
    public abstract com.example.chatapplication.models.MessageDao messageDao();

    private static volatile AppDB INSTANCE;

    public static AppDB getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDB.class, "database-name").build();
                }
            }
        }
        return INSTANCE;
    }
}
