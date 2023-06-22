package Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapplication.R;

import java.util.List;

import models.Message;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> messageList;
    private String currentUsername;
    public MessageAdapter(List<Message> messageList, String currentUsername) {  // Modify the constructor
        this.messageList = messageList;
        this.currentUsername = currentUsername;  // Set the current username
    }

    public class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        public SentMessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.senderTxt);
            timeText = itemView.findViewById(R.id.senderTime);
        }
    }

    public class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        public ReceivedMessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.rcvTxt);
            timeText = itemView.findViewById(R.id.rcvTime);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == 0) { // sent message
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_sender, parent, false);
            return new SentMessageViewHolder(itemView);
        } else { // received message
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_receiver, parent, false);
            return new ReceivedMessageViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        if (holder instanceof SentMessageViewHolder) {
            ((SentMessageViewHolder) holder).messageText.setText(message.getContent());
            ((SentMessageViewHolder) holder).timeText.setText(message.getTime());  // Changed
        } else if (holder instanceof ReceivedMessageViewHolder) {
            ((ReceivedMessageViewHolder) holder).messageText.setText(message.getContent());
            ((ReceivedMessageViewHolder) holder).timeText.setText(message.getTime());  // Changed
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        return message.getSenderUsername().equals(currentUsername) ? 0 : 1;  // Use the sender's username to determine view type
    }

    public void addMessage(Message message) {
        messageList.add(message);
        notifyItemInserted(messageList.size() - 1);
    }

}