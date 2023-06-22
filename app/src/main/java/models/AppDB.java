// AppDB.java

package com.example.chatapplication.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import models.ChatModel;
import models.ChatsDao;
import models.Message;

@Database(entities = {ChatModel.class, Message.class}, version = 1, exportSchema = false)
public abstract class AppDB extends RoomDatabase {
    public abstract ChatsDao chatsDao();
    public abstract com.example.chatapplication.models.MessageDao messageDao();
}
