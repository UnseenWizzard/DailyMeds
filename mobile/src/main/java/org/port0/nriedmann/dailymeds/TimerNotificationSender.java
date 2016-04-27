package org.port0.nriedmann.dailymeds;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;


/**
 * Created by captain on 03.09.2015.
 */
public class TimerNotificationSender extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getIntExtra(Med.MED_ID,-1)>-1){
            //Med med=MainActivity.getMedFromListByID(intent.getIntExtra(Med.MED_ID, -1));
            //do show notification
            NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
            notification.setSmallIcon(R.drawable.notification_icon);
            //TODO set large Icon to med image notification.setLargeIcon
            notification.setCategory(Notification.CATEGORY_ALARM);
            notification.setDefaults(Notification.DEFAULT_ALL);
            notification.setColor(intent.getIntExtra(Med.MED_COLOR,0));
            notification.setContentTitle(context.getString(R.string.notification_wait_title));
            notification.setContentText(intent.getStringExtra(Med.MED_NAME) + ": " + context.getString(R.string.notification_wait_text));
            ((NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE)).notify(intent.getIntExtra(Med.MED_ID,-1)+101,notification.build());
        } else {
        }
    }
}
