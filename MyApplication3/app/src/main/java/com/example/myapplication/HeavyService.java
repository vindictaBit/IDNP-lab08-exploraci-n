package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

public class HeavyService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> {
            try {
                Thread.sleep(5000); // Simula una tarea pesada
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Mostrar mensaje al usuario
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> Toast.makeText(HeavyService.this, "Tarea completada (Service)", Toast.LENGTH_SHORT).show());

            stopSelf(); // Detener el servicio
        }).start();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null; // No se requiere para este ejemplo
    }
}