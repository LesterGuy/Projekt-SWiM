package com.example.Project;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.ProjectSWiM.R;

public class Notification {

    public void showToast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    static class Notify {

        String notificationID = "Notification";
        Context context;
        NotificationManager manager;

        Notify(Context context){
            this.context = context;
            this.manager  = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        @SuppressLint("MissingPermission")
        public void showNotification(String title, String text, int id) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, notificationID);

            builder.setContentTitle(title);
            builder.setContentText(text);
            builder.setSmallIcon(R.drawable.ic_launcher_foreground);

            NotificationChannel channel = new NotificationChannel(notificationID, notificationID, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            builder.setChannelId(notificationID);

            NotificationManagerCompat.from(context).notify(id, builder.build());
        }

        public void hideNotification(int id){
            manager.cancel(id);
        }
    }
}
