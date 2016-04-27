package org.port0.nriedmann.dailymeds;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by captain on 16.09.2015.
 */
public class DataTools {

    public static boolean save(List<Med> medList, int currentID, Activity caller) {
        //TODO save to database
        JSONObject output = new JSONObject();
        JSONArray jsonMedList = new JSONArray();
        try {
            for (Med m : medList) {
                JSONObject jsonMed = new JSONObject();
                jsonMed.put("id", m.getId());
                jsonMed.put("name", m.getName());
                jsonMed.put("desc", m.getDescription());
                boolean[] days = m.getTakeDayArray();
                jsonMed.put("mo", days[0]);
                jsonMed.put("tu", days[1]);
                jsonMed.put("we", days[2]);
                jsonMed.put("th", days[3]);
                jsonMed.put("fr", days[4]);
                jsonMed.put("sa", days[5]);
                jsonMed.put("so", days[6]);
                jsonMed.put("takeTh", m.getTakeHour());
                jsonMed.put("takeTm", m.getTakeMinute());
                jsonMed.put("waitT", m.getWaitTime());
                jsonMed.put("iconID", m.getImageID());
                jsonMed.put("colorID", m.getColorID());
                jsonMed.put("takeTimeSet",m.isTakeTimeSet());
                jsonMed.put("alarmsSet",m.areAlarmsSet());
                jsonMed.put("silenced",m.isSilenced());
                jsonMedList.put(jsonMed);
            }
            output.put("medList", jsonMedList);
            output.put("currentID", currentID);
        } catch (JSONException e){
            e.printStackTrace();
            return false;
        }

        SharedPreferences pref = caller.getSharedPreferences(caller.getString(R.string.pref_file_id), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putString(caller.getString(R.string.pref_file_data), output.toString());
        prefEditor.commit();
        return true;
    }

    public static void setAlarmSetInfo(Activity caller, boolean alarmsSet){
        SharedPreferences pref = caller.getSharedPreferences(caller.getString(R.string.pref_file_id), Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putBoolean(caller.getString(R.string.pref_file_alarms_set),alarmsSet);
        prefEditor.commit();
    }

    public static boolean getAlarmsSetInfo(Activity caller){
        SharedPreferences pref = caller.getSharedPreferences(caller.getString(R.string.pref_file_id), Context.MODE_PRIVATE);
        return pref.getBoolean(caller.getString(R.string.pref_file_alarms_set),false);
    }

    public static int load(List<Med> medList, Activity caller){
        Log.w("DailyMeds", "loading data");
        SharedPreferences pref = caller.getSharedPreferences(caller.getString(R.string.pref_file_id),Context.MODE_PRIVATE);
        //TODO load med data (from database?)
        String jsonString = pref.getString(caller.getString(R.string.pref_file_data), "");
        if (jsonString.isEmpty()){
            return -1;
        }
        try {
            JSONObject input = new JSONObject(jsonString);
            int currentID = input.getInt("currentID");
            JSONArray jsonMedList = input.getJSONArray("medList");
            for (int i = 0;i<jsonMedList.length();i++){
                JSONObject o = jsonMedList.getJSONObject(i);
                boolean[] takeDays = {o.getBoolean("mo"),o.getBoolean("tu"),o.getBoolean("we"),o.getBoolean("th"),o.getBoolean("fr"),o.getBoolean("sa"),o.getBoolean("so")};
                Med storedMed = new Med(o.getInt("id"),o.getString("name"),o.getString("desc"),takeDays,o.getInt("takeTh"),o.getInt("takeTm"),o.getInt("waitT"),o.getInt("iconID"),o.getInt("colorID"),o.getBoolean("takeTimeSet"),o.getBoolean("alarmsSet"),o.getBoolean("silenced"));
                medList.add(storedMed);
            }
            return currentID;
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }


    public static String[] getMedNameList(List<Med> medList){
        String[] list= new String[medList.size()];
        //TODO FILL WITH NAMES OF MEDS
        for (int i=0;i<medList.size();i++){
            list[i]=medList.get(i).getName();
        }
        return list;
    }
}
