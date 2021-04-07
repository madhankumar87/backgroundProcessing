package com.mvi.backgroundprocessing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button buttonStartThread;
    private volatile boolean stopThread = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStartThread = findViewById(R.id.button_start_thread);
    }

    public void startThread(View view) {
        stopThread = false;
        BackgroundThread backgroundThread = new BackgroundThread(100);
        new Thread(backgroundThread).start();
    }

    public void stopThread(View view) {
        stopThread = true;
    }

    class BackgroundThread implements Runnable {

        int progress;

        BackgroundThread(int progress) {
            this.progress = progress;
        }


        @Override
        public void run() {
            Handler threadHandler = new Handler(Looper.getMainLooper());


            for (int i = 0; i <= progress; i = i + 10) {
                final int progressCompleted = i;
                if (stopThread) {

                    threadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Progress Stopped", Toast.LENGTH_SHORT).show();
                            buttonStartThread.setText("Start");

                        }
                    });
                    return;
                }
                threadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        String percentageCompleted = String.valueOf(progressCompleted).concat("%");
                        buttonStartThread.setText(percentageCompleted);
                        if (progressCompleted == progress) {
                            Toast.makeText(MainActivity.this, "Progress Completed", Toast.LENGTH_SHORT).show();
                            buttonStartThread.setText("Start");
                        }
                    }
                });
                Log.d(TAG, "startThread: " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}