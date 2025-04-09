package com.example.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class LoggingService extends Service {
    Handler handler;
    Runnable loggingRunnable;
    int counter = 0;

    @Override
    public void onCreate() {
        Toast.makeText(this, "Сервис создан", Toast.LENGTH_SHORT).show();
        Log.d("LoggingService", "Сервис создан");

        handler = new Handler();
        loggingRunnable = new Runnable() {
            @Override
            public void run() {
                counter++;
                Log.d("LoggingService", "Сервис работает... (" + counter + "/" + 5 + ")");

                if (counter >= 5) {
                    stopSelf();
                } else {
                    handler.postDelayed(this, 2000);
                }
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("LoggingService", "Сервис запущен");
        handler.post(loggingRunnable);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(loggingRunnable);
        Log.d("LoggingService", "Сервис остановлен");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}