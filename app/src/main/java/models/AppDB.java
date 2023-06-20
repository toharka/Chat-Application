package models;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ChatModel.class}, version = 1, exportSchema = false)
public abstract class AppDB extends RoomDatabase {

    public abstract ChatsDao chatsDao();
}
