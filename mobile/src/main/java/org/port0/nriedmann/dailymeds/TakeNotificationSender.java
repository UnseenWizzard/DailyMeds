package org.port0.nriedmann.dailymeds;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

/**
 * Created by captain on 29.09.2015.
 */
public class TakeNotificationSender extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getIntExtra(Med.MED_ID,-1)>-1){
            //Med med=MainActivity.getMedFromListByID(intent.getIntExtra(Med.MED_ID, -1));
            Intent takenIntent = new Intent(context, TakeTimerScheduler.class);
            takenIntent.putExtra(Med.MED_ID, intent.getIntExtra(Med.MED_ID,-1));
            takenIntent.putExtra(Med.MED_NAME, intent.getStringExtra(Med.MED_NAME));
            takenIntent.putExtra(Med.MED_COLOR, intent.getIntExtra(Med.MED_COLOR, 0));
            takenIntent.putExtra(Med.MED_WAIT, intent.getIntExtra(Med.MED_WAIT,0));
            PendingIntent pending = PendingIntent.getBroadcast(context, intent.getIntExtra(Med.MED_ID,-1), takenIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            //show notification
            NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
            notification.setSmallIcon(R.drawable.notification_icon)
            //TODO set large Icon to med image notification.setLargeIcon
            .setCategory(Notification.CATEGORY_ALARM)
            .setDefaults(Notification.DEFAULT_ALL)
            .setColor(intent.getIntExtra(Med.MED_COLOR, 0))
            .setContentTitle(context.getString(R.string.notification_take_title))
            .setContentText(context.getString(R.string.notification_take_text) + " " + intent.getStringExtra(Med.MED_NAME))
            .addAction(R.drawable.notification_icon,context.getString(R.string.button_taken),pending)
            .setAutoCancel(true);
            ((NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE)).notify(intent.getIntExtra(Med.MED_ID, -1)+110, notification.build());
        }
    }
}
