package com.example.chatapplication;

import android.content.Context;
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
        holder.lastMessage.setText(message.getContent());

        // Use Glide to load the profile picture from its URL
        Glide.with(context)
                .load(user.getProfilePic())
                .into(holder.profile);
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
        }
    }
}

