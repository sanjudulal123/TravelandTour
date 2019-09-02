package com.death.tnt;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

public class FCM extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        showNotification(remoteMessage.getNotification().getBody());
    }

    public void showNotification(String message){
        PendingIntent intent = PendingIntent.getActivity(this,0,new Intent(this,Nexample.class),0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("YATRA")
                .setContentText(message)
                .setContentIntent(intent)
                .setAutoCancel(true)
                .build();
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0,notification);
    }
}
