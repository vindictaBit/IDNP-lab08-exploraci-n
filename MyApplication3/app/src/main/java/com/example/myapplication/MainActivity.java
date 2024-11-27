package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar el Handler para comunicar con el hilo principal
        handler = new Handler(Looper.getMainLooper());

        // Configurar los botones
        Button blockingTaskButton = findViewById(R.id.blockingTaskButton);
        blockingTaskButton.setOnClickListener(v -> startBlockingTask());

        Button backgroundTaskButton = findViewById(R.id.backgroundTaskButton);
        backgroundTaskButton.setOnClickListener(v -> startBackgroundTask());

        Button clickMeButton = findViewById(R.id.clickMeButton);
        clickMeButton.setOnClickListener(v -> incrementCounter());
    }

    // Método para la tarea pesada SIN hilos secundarios
    private void startBlockingTask() {
        try {
            Thread.sleep(5000); // Simula una tarea pesada
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Tarea completada (Sin Hilos)", Toast.LENGTH_SHORT).show();
    }

    // Método para la tarea pesada CON hilos secundarios
    private void startBackgroundTask() {
        new Thread(() -> {
            try {
                Thread.sleep(5000); // Simula una tarea pesada
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Usar el Handler para actualizar la UI
            handler.post(() -> Toast.makeText(MainActivity.this, "Tarea completada (Con Hilos)", Toast.LENGTH_SHORT).show());
        }).start();
    }

    // Método para incrementar el contador
    private void incrementCounter() {
        TextView clickCounter = findViewById(R.id.clickCounter);
        int currentCount = Integer.parseInt(clickCounter.getText().toString());
        clickCounter.setText(String.valueOf(currentCount + 1));
    }
}
