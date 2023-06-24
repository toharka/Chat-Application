package com.example.chatapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import models.ChatModel;
import models.Message;
import models.User;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private Context context;
    private List<ChatModel> chatModels;

    public ChatAdapter(Context context, List<ChatModel> chatModels) {
        this.context = context;
        this.chatModels = chatModels;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_user, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatModel chatModel = chatModels.get(position);
        User user = chatModel.getUser();
        Message message = chatModel.getLastMessage();

        // Replace your text views and profile picture with the data from the server
        holder.userName.setText(user.getDisplayName());
        if (message!=null) {
            holder.lastMessage.setText(message.getContent()+"\n"+message.getHoursMinutesDayOfWeek());
        }
        else {
            holder.lastMessage.setText("No messages yet");
        }
        byte[] imageBytes = Base64.decode(user.getProfilePic(), Base64.DEFAULT);
        Glide.with(context).load(imageBytes).into(holder.profile);
    }

    @Override
    public int getItemCount() {
        return chatModels.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView userName;
        TextView lastMessage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profile);
            userName = itemView.findViewById(R.id.UserName);
            lastMessage = itemView.findViewById(R.id.lastMessage);

            // Add a click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    ChatModel chatModel = chatModels.get(position);
                    Intent intent = new Intent(context, Chat.class);
                    intent.putExtra("displayName", chatModel.getUser().getDisplayName());
                    intent.putExtra("chatId", chatModel.getId());
                    intent.putExtra("profilePic", chatModel.getUser().getProfilePic());

                    context.startActivity(intent);
                }
            });
        }
    }
}

