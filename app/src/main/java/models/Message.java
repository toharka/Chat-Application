package models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

@Entity
public class Message {

    @PrimaryKey
    private int id;
    private int chatId;
    private String content;
    private String time;
    private boolean isReceived;
    private String created;
    private String senderUsername;

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public Message() {
        // Empty constructor needed for Room
    }

    // Getter and Setter methods

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        SimpleDateFormat originalFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.ENGLISH);
        SimpleDateFormat targetFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        Date date = null;
        try {
            date = originalFormat.parse(getCreated());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            return targetFormat.format(date);
        } else {
            return null;
        }
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isReceived() {
        return isReceived;
    }

    public void setReceived(boolean received) {
        isReceived = received;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
