package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.*;

import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar el Handler para comunicar con el hilo principal
        handler = new Handler(Looper.getMainLooper());

        // Configurar los botones existentes
        findViewById(R.id.blockingTaskButton).setOnClickListener(v -> startBlockingTask());
        findViewById(R.id.backgroundTaskButton).setOnClickListener(v -> startBackgroundTask());
        findViewById(R.id.clickMeButton).setOnClickListener(v -> incrementCounter());

        // Configurar los botones para nuevas técnicas
        //findViewById(R.id.asyncTaskButton).setOnClickListener(v -> startAsyncTask());
        findViewById(R.id.workManagerButton).setOnClickListener(v -> startWorkManagerTask());
        findViewById(R.id.serviceButton).setOnClickListener(v -> startServiceTask());
    }

    // Tarea pesada SIN hilos secundarios
    private void startBlockingTask() {
        try {
            Thread.sleep(5000); // Simula una tarea pesada
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Tarea completada (Sin Hilos)", Toast.LENGTH_SHORT).show();
    }

    // Tarea pesada CON hilos secundarios
    private void startBackgroundTask() {
        new Thread(() -> {
            try {
                Thread.sleep(5000); // Simula una tarea pesada
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.post(() -> Toast.makeText(MainActivity.this, "Tarea completada (Con Hilos)", Toast.LENGTH_SHORT).show());
        }).start();
    }

    // Incrementar el contador
    private void incrementCounter() {
        TextView clickCounter = findViewById(R.id.clickCounter);
        int currentCount = Integer.parseInt(clickCounter.getText().toString());
        clickCounter.setText(String.valueOf(currentCount + 1));
    }

    // Tarea pesada usando WorkManager
    private void startWorkManagerTask() {
        WorkRequest workRequest = new OneTimeWorkRequest.Builder(HeavyWorker.class).build();
        WorkManager.getInstance(this).enqueue(workRequest);
    }

    // Worker para realizar la tarea
    public static class HeavyWorker extends androidx.work.Worker {

        public HeavyWorker(@androidx.annotation.NonNull android.content.Context context, @androidx.annotation.NonNull androidx.work.WorkerParameters workerParams) {
            super(context, workerParams);
        }

        @androidx.annotation.NonNull
        @Override
        public Result doWork() {
            try {
                Thread.sleep(5000); // Simula una tarea pesada
            } catch (InterruptedException e) {
                e.printStackTrace();
                return Result.failure();
            }
            handler.post(() -> Toast.makeText(getApplicationContext(), "Tarea completada (WorkManager)", Toast.LENGTH_SHORT).show());
            return Result.success();
        }
    }

    // Tarea pesada usando un Service
    private void startServiceTask() {
        Intent serviceIntent = new Intent(this, HeavyService.class);
        startService(serviceIntent);
    }
}
