package org.port0.nriedmann.dailymeds;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by captain on 29.09.2015.
 */
public class TakeTimerScheduler extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getIntExtra(Med.MED_ID, -1) > -1) {
            scheduleWaitAlarm(context,intent.getIntExtra(Med.MED_ID,0),intent.getStringExtra(Med.MED_NAME),intent.getIntExtra(Med.MED_COLOR,0),intent.getIntExtra(Med.MED_WAIT,0));
            ((NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE)).cancel(intent.getIntExtra(Med.MED_ID,0)+110);
        }
    }

    public static void scheduleWaitAlarm(Context context, int medID, String medName, int color, int waitTime){

        if (waitTime>0){
            AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, TimerNotificationSender.class);
            intent.putExtra(Med.MED_ID, medID);
            intent.putExtra(Med.MED_NAME, medName);
            intent.putExtra(Med.MED_COLOR, color);
            PendingIntent pending = PendingIntent.getBroadcast(context, medID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (waitTime * 60000), pending);
            Toast.makeText(context, context.getString(R.string.toast_med_taken), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getString(R.string.toast_no_wait), Toast.LENGTH_SHORT).show();
        }
    }
}
