package models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Message {

    @PrimaryKey
    private int id;
    private int chatId;  // Added this line
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

        LocalDateTime dateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            dateTime = LocalDateTime.parse(getCreated(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }
        String hourAndMinute = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            hourAndMinute = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return hourAndMinute;


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
