package com.sellgirl.castScreen.android;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;

public class MediaProjectionService extends Service {
    private static final String CHANNEL_ID = "media_projection_channel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        startForeground(NOTIFICATION_ID, buildNotification());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 服务启动后保持运行
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // 不需要绑定，仅作为前台服务运行
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "屏幕投射服务",
                NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("用于屏幕投射的前台服务");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private Notification buildNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("屏幕投射中")
            .setContentText("正在投射手机屏幕到电视")
            .setSmallIcon(android.R.drawable.ic_menu_camera) // 可更换为你的图标
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build();
    }
}
