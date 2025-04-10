package com.example.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class TimerService extends Service {
    static final String CHANNEL_ID = "TimerChannel";
    static final int NOTIFICATION_ID = 1;

    Handler handler;
    Runnable timerRunnable;
    int seconds = 0;
    NotificationManager notificationManager;

    // Инициализирует менеджер уведомлений и создает канал уведомлений для Android 8 и выше
    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = getSystemService(NotificationManager.class);
        createNotificationChannel();
        handler = new Handler(Looper.getMainLooper());
    }

    // Запускает таймер в фоновом режиме
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForegroundTimer();
        return START_STICKY; // Сервис перезапускается при его завершении
    }

    // Запускает таймер и отображает уведомление
    @SuppressLint("ForegroundServiceType")
    private void startForegroundTimer() {
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                seconds++;
                updateNotification();
                handler.postDelayed(this, 1000);// Вызывает себя каждую секунду
            }
        };
        handler.post(timerRunnable);

        Notification notification = createNotification();
        startForeground(NOTIFICATION_ID, notification); // Отображает уведомление
    }

    // Обновляет текст уведомления каждую секунду
    private void updateNotification() {
        Notification notification = createNotification();
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    // Создает уведомление с текущим временем работы таймера
    private Notification createNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Таймер работает")
                .setContentText("Таймер работает: " + seconds + " секунд")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setOngoing(true)
                .build();
    }

    // Создает канал уведомлений для Android 8 и выше
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Таймер",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel.setDescription("Канал для отображения работы таймера");
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Останавливает таймер и убирает уведомление
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    // Останавливает таймер и убирает уведомление
    private void stopTimer() {
        if (handler != null && timerRunnable != null) {
            handler.removeCallbacks(timerRunnable);
        }
        stopForeground(true);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
