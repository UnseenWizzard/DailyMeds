package org.port0.nriedmann.dailymeds;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static ArrayList<Med> medList;
    private static int currentID;
    private boolean alarmsSet;

    private static RecyclerView medListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        medList=new ArrayList<Med>();
        int loadedID = DataTools.load(medList,this);
        currentID = (loadedID<0)?0:loadedID;
        medListView = (RecyclerView) findViewById(R.id.medListRecycler);
        medListView.setHasFixedSize(true);
        LinearLayoutManager linLayManager = new LinearLayoutManager(this);
        linLayManager.setOrientation(LinearLayoutManager.VERTICAL);
        medListView.setLayoutManager(linLayManager);
        medListView.setAdapter(new MedViewAdapter(this, medList));
        alarmsSet = DataTools.getAlarmsSetInfo(this);
        scheduleTakeAlarms();
    }

    @Override
    public void onResume(){
        super.onResume();
        ((MedViewAdapter)medListView.getAdapter()).updateMedList(medList);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop(){
        super.onStop();
        DataTools.save(medList, currentID, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about){
//            SpannableString message = new SpannableString(getString(R.string.about_text));
//            Linkify.addLinks(message,Linkify.ALL);
            TextView messageView = new TextView(this);
            messageView.setText(Html.fromHtml(getString(R.string.about_text)));
            messageView.setMovementMethod(LinkMovementMethod.getInstance());
            messageView.setClickable(true);
            new AlertDialog.Builder(this)
                    .setView(messageView)
                    .setCancelable(true)
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void addMedClick(View view){
        Intent intent = new Intent(this,EditActivity.class);
        startActivityForResult(intent, 1);
    }


    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode==1) {
            int id = data.getIntExtra(Med.MED_ID, -1);
            if (id > -1) {
                scheduleTakeAlarm(getMedFromListByID(id));
            }
        }
    }

    public static boolean addMed(Med newMed){
        Med exists = getMedFromListByID(newMed.getId());
        if (exists==null){
            medList.add(newMed);
            currentID+=1;
        } else {
            medList.set(medList.indexOf(exists), newMed);
        }
        medListView.getAdapter().notifyDataSetChanged();
        return true;
    }
    private int deleteMedID=-1;
    public void deleteMed(int id){
        deleteMedID=id;
        DeleteDialog deleteDialog = new DeleteDialog();
        deleteDialog.show(getSupportFragmentManager(), "DeleteFragment");
    }
    public void resolveDelete(boolean delete){
        if (delete){
            //stop alarms
            cancelTakeAlarm(MainActivity.getMedFromListByID(deleteMedID));
            // delete from list, update view
            medList.remove(MainActivity.getMedFromListByID(deleteMedID));
            medListView.getAdapter().notifyDataSetChanged();
        } else {
            deleteMedID=-1;
        }
    }

    public void scheduleTakeAlarms(){
        Calendar checkCal = Calendar.getInstance();
        int current = checkCal.get(Calendar.DAY_OF_WEEK);
        int firstDay = checkCal.getFirstDayOfWeek();
        if ( current==firstDay || !alarmsSet)  {
            for (Med m : medList) {
                scheduleTakeAlarm(m);
            }
            alarmsSet=true;
            DataTools.setAlarmSetInfo(this,alarmsSet);
            Toast.makeText(this, getString(R.string.timers_set), Toast.LENGTH_LONG).show();
        }
    }

    public void scheduleTakeAlarm(Med m){
        if (m.isTakeTimeSet() && !m.areAlarmsSet() && !m.isSilenced()) {

            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, TakeNotificationSender.class);
            intent.putExtra(Med.MED_ID, m.getId());
            intent.putExtra(Med.MED_NAME, m.getName());
            intent.putExtra(Med.MED_COLOR, m.getColor(this));
            intent.putExtra(Med.MED_WAIT, m.getWaitTime());
            Calendar cal = Calendar.getInstance();
            boolean todaysTimePassed = false;
            if (cal.get(Calendar.HOUR_OF_DAY)>m.getTakeHour() || ( cal.get(Calendar.HOUR_OF_DAY)==m.getTakeHour() && cal.get(Calendar.MINUTE)>=m.getTakeMinute() ) ) {
                todaysTimePassed = true;
            }
            int current = cal.get(Calendar.DAY_OF_WEEK);
            cal.set(Calendar.HOUR_OF_DAY, m.getTakeHour());
            cal.set(Calendar.MINUTE, m.getTakeMinute());
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND,0);
            boolean[] days = m.getTakeDayArray();
            if (days[0]){
                cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                PendingIntent pending = PendingIntent.getBroadcast(this, m.getId()*1000 + 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                long millis = cal.getTimeInMillis();
                if ( Calendar.MONDAY<current || Calendar.MONDAY == current && todaysTimePassed) {
                    //start next week
                    millis += AlarmManager.INTERVAL_DAY * 7;
                }
                alarm.setRepeating(AlarmManager.RTC_WAKEUP,millis,AlarmManager.INTERVAL_DAY*7, pending);
            }
            if (days[1]){
                cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                PendingIntent pending = PendingIntent.getBroadcast(this, m.getId()*1000 + 2, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                long millis = cal.getTimeInMillis();
                if ( Calendar.TUESDAY<current || Calendar.TUESDAY == current && todaysTimePassed) {
                    //start next week
                    millis += AlarmManager.INTERVAL_DAY * 7;
                }
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, millis, AlarmManager.INTERVAL_DAY * 7, pending);
            }
            if (days[2]){
                cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                PendingIntent pending = PendingIntent.getBroadcast(this, m.getId()*1000 + 3, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                long millis = cal.getTimeInMillis();
                if ( Calendar.WEDNESDAY<current || Calendar.WEDNESDAY == current && todaysTimePassed) {
                    //start next week
                    millis += AlarmManager.INTERVAL_DAY * 7;
                }
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, millis, AlarmManager.INTERVAL_DAY * 7, pending);
            }
            if (days[3]){
                cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                PendingIntent pending = PendingIntent.getBroadcast(this, m.getId()*1000 + 4, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                long millis = cal.getTimeInMillis();
                if ( Calendar.THURSDAY<current || Calendar.THURSDAY == current && todaysTimePassed) {
                    //start next week
                    millis += AlarmManager.INTERVAL_DAY * 7;
                }
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, millis, AlarmManager.INTERVAL_DAY * 7, pending);
            }
            if (days[4]){
                cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                PendingIntent pending = PendingIntent.getBroadcast(this, m.getId()*1000 + 5, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                long millis = cal.getTimeInMillis();
                if ( Calendar.FRIDAY<current || Calendar.FRIDAY == current && todaysTimePassed) {
                    //start next week
                    millis += AlarmManager.INTERVAL_DAY * 7;
                }
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, millis, AlarmManager.INTERVAL_DAY * 7, pending);
            }
            if (days[5]){
                cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                PendingIntent pending = PendingIntent.getBroadcast(this, m.getId()*1000 + 6, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                long millis = cal.getTimeInMillis();
                if ( Calendar.SATURDAY<current || Calendar.SATURDAY == current && todaysTimePassed) {
                    //start next week
                    millis += AlarmManager.INTERVAL_DAY * 7;
                }
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, millis, AlarmManager.INTERVAL_DAY * 7, pending);
            }
            if (days[6]){
                cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                PendingIntent pending = PendingIntent.getBroadcast(this, m.getId()*1000 + 7, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                long millis = cal.getTimeInMillis();
                if ( Calendar.SUNDAY<current || Calendar.SUNDAY == current && todaysTimePassed) {
                    //start next week
                    millis += AlarmManager.INTERVAL_DAY * 7;
                }
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, millis, AlarmManager.INTERVAL_DAY * 7, pending);
            }
            m.setAlarmsSet(true);
        }
    }

    public void cancelTakeAlarm(Med m){
        if (m.isTakeTimeSet() && m.areAlarmsSet()) {

            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, TakeNotificationSender.class);
            intent.putExtra(Med.MED_ID, m.getId());
            intent.putExtra(Med.MED_NAME, m.getName());
            intent.putExtra(Med.MED_COLOR, m.getColor(this));
            intent.putExtra(Med.MED_WAIT, m.getWaitTime());

            boolean[] days = m.getTakeDayArray();

            for (int i=0;i<days.length;i++){
                if (days[i]) {
                    PendingIntent pending = PendingIntent.getBroadcast(this, m.getId() * 1000 + i + 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    alarm.cancel(pending);
                }
            }
            m.setAlarmsSet(false);
        }
    }

    public static Med getMedFromListByID(int id){
        for (Med m : medList){
            if (m.getId()==id){
                return m;
            }
        }
        return null;
    }
    public static Med getMedFromListByIndex(int position){
        return medList.get(position);
    }

    public static int getCurrentID(){
        return currentID;
    }
}
