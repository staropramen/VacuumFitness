package com.example.android.vacuumfitness.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.android.vacuumfitness.MotivationAppWidget;
import com.example.android.vacuumfitness.R;
import com.example.android.vacuumfitness.model.Motivator;
import com.example.android.vacuumfitness.ui.MainActivity;

public class NotificationUtils {

    private static final int TRAINING_MOTIVATOR_NOTIFICATION_ID = 7632;
    private static final int TRAINING_MOTIVATOR_PENDING_INTENT_ID = 4867;
    private static final String TRAINING_MOTIVATOR_NOTIFICATION_CHANNEL_ID = "training-motivator-notification-channel-id";

    public static void motivateUserForTraining(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel mChannel = new NotificationChannel(
                    TRAINING_MOTIVATOR_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);

            notificationManager.createNotificationChannel(mChannel);
        }

        Motivator motivator = SharedPrefsUtils.getCurrentMotivator(context);

        //Change motivator
        MotivationAppWidget.getNewMotivator(context);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, TRAINING_MOTIVATOR_NOTIFICATION_CHANNEL_ID)
                        .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                        .setSmallIcon(R.drawable.ic_training_notification)
                        .setLargeIcon(notificationIcon(context))
                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText(motivator.getMotivationText())
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(motivator.getMotivationText()))
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .setContentIntent(contentIntent(context))
                        .setAutoCancel(true);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(TRAINING_MOTIVATOR_NOTIFICATION_ID, notificationBuilder.build());

    }

    private static PendingIntent contentIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(KeyUtils.WIDGET_INTENT, true);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                TRAINING_MOTIVATOR_PENDING_INTENT_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        return pendingIntent;
    }

    private static Bitmap notificationIcon(Context context) {
        Resources res = context.getResources();
        Bitmap notificationIcon = BitmapFactory.decodeResource(res, R.drawable.ic_big_notification_icon);
        return notificationIcon;
    }

}
