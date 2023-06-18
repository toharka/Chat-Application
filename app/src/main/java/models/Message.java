package models;

public class Message {
    public String text;
    public String time;
    public boolean isReceived;  // true if received, false if sent

    public Message(String text, String time, boolean isReceived) {
        this.text = text;
        this.time = time;
        this.isReceived = isReceived;
    }
}
