package converters;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import models.User;

public class UserConverter {

    public static final Gson gson = new Gson();

    @TypeConverter
    public static User fromString(String value){
        return gson.fromJson(value, User.class);
    }

    @TypeConverter
    public static String toString(User user){
        return gson.toJson(user);
    }

}
