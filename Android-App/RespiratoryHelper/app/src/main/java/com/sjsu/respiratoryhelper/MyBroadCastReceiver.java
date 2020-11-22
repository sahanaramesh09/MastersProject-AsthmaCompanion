package com.sjsu.respiratoryhelper;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

public class MyBroadCastReceiver extends BroadcastReceiver {
    private static final String TAG = "MyBroadCastReceiver";

    public static final String CHANNEL_ID = "appChannel";
    public static final int NOTIFICATION_ID_BROADCAST = 6001;
    private NotificationManagerCompat notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.i(TAG, "BOOT_COMPLETED : ");
            Intent serviceIntent = new Intent(context, MyService.class);
            context.startService(serviceIntent);
        } else {
            Log.d(TAG, "@@@@ Just ran time : " + System.currentTimeMillis());
            // Toast.makeText(context.getApplicationContext(), "Just ran", Toast.LENGTH_LONG).show();

            // Creating Notification Channel
            createNotificationChannel(context);
            notificationManager = NotificationManagerCompat.from(context);

            if (!(appInForeground(context)) && !(isNotificationActive(context))){
                showNotification(context, intent);
            }
        }
    }

    private boolean isNotificationActive(Context context){
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        StatusBarNotification[] notifications = mNotificationManager.getActiveNotifications();
        for (StatusBarNotification notification : notifications) {
            if (notification.getId() == NOTIFICATION_ID_BROADCAST) {
                Log.d(TAG, "@@@@ Notification is active ");
                return true;
            }
        }
        return false;
    }

    /**
     * Helper function to check whether app is already in foreground or not
     * @param context
     * @return
     */
    private boolean appInForeground(@NonNull Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {
            if (runningAppProcess.processName.equals(context.getPackageName()) &&
                    runningAppProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                Log.d(TAG, "@@@@ App is already running ");
                return true;
            }
        }
        return false;
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Example Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    private void showNotification(Context context, Intent intent) {
        RemoteViews collapsedView = new RemoteViews(intent.getComponent().getPackageName(), R.layout.notification_collapsed);
        collapsedView.setTextViewText(R.id.notificationTitle, " Asthma Risk Warning ");
        collapsedView.setTextViewText(R.id.notificationText, " Currently the air is at a bad state - please take precautions ");

        Intent clickIntent = new Intent(context, MainActivity.class);
        clickIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        clickIntent.putExtra(intent.getComponent().getPackageName(), NOTIFICATION_ID_BROADCAST);

        PendingIntent clickPendingIntent = PendingIntent.getActivity(context, 0, clickIntent, 0);

        RemoteViews expandedView = new RemoteViews(intent.getComponent().getPackageName(), R.layout.notification_expanded);
        expandedView.setTextViewText(R.id.notificationExpandedTitle, " Asthma Risk Warning ");
        expandedView.setTextViewText(R.id.notificationExpandedText, " Currently the air is at a bad state - please take precautions");
        expandedView.setImageViewResource(R.id.ivExpanded, R.drawable.notif_img);
        expandedView.setOnClickPendingIntent(R.id.ivExpanded, clickPendingIntent);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications_active_24)
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
                .build();
        notificationManager.notify(NOTIFICATION_ID_BROADCAST, notification);
    }
}
