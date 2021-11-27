package com.example.worker_1918128;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.work.ForegroundInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {
    private NotificationManager notificationManager;

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        notificationManager = (NotificationManager)
                context.getSystemService(NOTIFICATION_SERVICE);
    }

    @NonNull
    @Override

    public Result doWork() {

        
        String progress = "Dimulai...";
        setForegroundAsync(createForegroundInfo(progress));

        for(int i=0; i<=100; i+=20){
            try {
                Thread.sleep(1000);
                setForegroundAsync(createForegroundInfo(i+"%"));
                Log.d("LocationUploadWorker", "Log testing...." + i);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return Result.success();
    }

    @NonNull
    private ForegroundInfo createForegroundInfo(@NonNull String progress) {
        

        Context context = getApplicationContext();
        String id = "my_channel";
        String title = "Proses ";
        String cancel = "Cancel";
        
        PendingIntent intent = WorkManager.getInstance(context)
                .createCancelPendingIntent(getId());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }

        Notification notification = new NotificationCompat.Builder(context, id)
                .setContentTitle(title + progress)
                .setTicker(title)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOngoing(true)
                .addAction(android.R.drawable.ic_delete, cancel, intent)
                .build();

        return new ForegroundInfo(4, notification);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel() {
   
        CharSequence name = "Worker channel";
        String description = "Description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("my_channel", name, importance);
        channel.setDescription(description);
        notificationManager.createNotificationChannel(channel);
    }
}
