package models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChatsDao {

    @Query("SELECT * FROM ChatModel")
    List<ChatModel> findChats();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ChatModel chatModel);

    @Query("SELECT * FROM ChatModel WHERE id=:id")
    ChatModel findSingleChat(int id);
    @Query("DELETE FROM ChatModel")
    void deleteAllChats();

}
