package com.example.services;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Задание 1
//        Button startServiceButton = findViewById(R.id.start_service_button);
//        startServiceButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent serviceIntent = new Intent(MainActivity.this, LoggingService.class);
//                startService(serviceIntent);
//            }
//        });

        // Задание 2
        Button startButton = findViewById(R.id.start_button);
        Button stopButton = findViewById(R.id.stop_button);

        startButton.setOnClickListener(v -> startTimerService());
        stopButton.setOnClickListener(v -> stopTimerService());
    }

    private void startTimerService() {
        Intent serviceIntent = new Intent(this, TimerService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    private void stopTimerService() {
        Intent serviceIntent = new Intent(this, TimerService.class);
        stopService(serviceIntent);
    }
}