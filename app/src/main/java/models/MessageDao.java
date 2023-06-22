// MessageDao.java

package com.example.chatapplication.models;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import models.Message;

@Dao
// MessageDao.java


public interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessage(Message message);

    @Query("SELECT * FROM Message WHERE chatId = :chatId ")
    LiveData<List<Message>> getMessages(int chatId);
}

