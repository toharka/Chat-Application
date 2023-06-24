package com.example.chatapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.room.Room;

import com.example.chatapplication.models.AppDB;
import com.example.chatapplication.models.MessageDao;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import models.Message;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public MyFirebaseMessagingService() {

    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification() != null) {
            createNotificationChannel();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody()).
                    setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);


            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManager.notify(1, builder.build());

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                String chatIdStr = remoteMessage.getData().get("chatId");
                String messageIdStr = remoteMessage.getData().get("messageId");
                String sender = remoteMessage.getData().get("sender");
                String content = remoteMessage.getData().get("content");

                // parse the chatId and messageId into integers
                int chatId = Integer.parseInt(chatIdStr);
                int messageId = Integer.parseInt(messageIdStr);

                // create a new message object
                Message message = new Message();
                message.setId(messageId);
                message.setChatId(chatId);
                message.setSenderUsername(sender);
                message.setContent(content);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                String currentDateAndTime = sdf.format(new Date());
                message.setCreated(currentDateAndTime);
                message.setCreated(new Date().toString());  // adjust if necessary
                message.setReceived(true);  // this is a received message

                // add the new message to your local database
                new Thread(() -> {
                    AppDB appDB = AppDB.getInstance(getApplicationContext());

                    MessageDao messageDao = appDB.messageDao();
                    messageDao.insertMessage(message);
                }).start();
            }
        }
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("1", "My channel", importance);
            channel.setDescription("Demo channel");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(channel);
}
}
}