package com.example.industrialexperiencee15;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.Notification;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra("notificationId",0);
        String message = intent.getStringExtra("todo");

        Intent mainIntent = new Intent(context, Notification.class);

        PendingIntent contentIntent = PendingIntent.getActivity(context,0,mainIntent,0 );

//        NotificationManager myNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notify");

        builder.setSmallIcon(android.R.drawable.ic_dialog_info).setContentTitle("It's time to do").setContentText(message).setWhen(System.currentTimeMillis())
        .setAutoCancel(true).setContentIntent(contentIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(notificationId,builder.build());
     }
}
