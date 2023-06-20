package converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import models.Message;

public class MessageConverter {

    public static final Gson gson = new Gson();

    @TypeConverter
    public static Message fromString(String value){
        return gson.fromJson(value, Message.class);
    }

    @TypeConverter
    public static String toString(Message message){
        return gson.toJson(message);
    }

}
