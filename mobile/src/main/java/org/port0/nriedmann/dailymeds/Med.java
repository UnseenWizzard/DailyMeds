package org.port0.nriedmann.dailymeds;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by captain on 27.08.2015.
 */
public class Med {

    private int id;
    private String name, description;

    private boolean[] takeDays;
    private int takeTimeH, takeTimeM;
    private int waitTime;
    private int iconID, colorID;

    private boolean takeTimeSet;
    private boolean alarmsSet;
    private boolean silenced;

    //private Drawable image;
    //private int color;

    public static String MED_ID = "org.port0.nriemdann.DailyMeds.medID";
    public static String MED_NAME = "org.port0.nriemdann.DailyMeds.medName";
    public static String MED_COLOR = "org.port0.nriemdann.DailyMeds.medColor";
    //public static String MED_TIME = "org.port0.nriemdann.DailyMeds.medTime";
    public static String MED_WAIT = "org.port0.nriemdann.DailyMeds.medWait";
    //public static String MED_IMAGE = "org.port0.nriemdann.DailyMeds.medImage";

    private Med(){

    }

    public Med(int id, Context c){
        this.id=id;
        this.name=c.getString(R.string.med_name);
        this.description=c.getString(R.string.med_desc_text);;
        this.takeDays = new boolean[]{false,false,false,false,false,false,false};
        this.takeTimeH=8;
        this.takeTimeM=0;
        this.waitTime=0;
        this.iconID=0;
        this.colorID=0;
        this.takeTimeSet=false;
        this.alarmsSet=false;
        this.silenced=false;
        //this.image= c.getResources().getDrawable(R.drawable.med_icon_1);
        //this.color= c.getResources().getColor(R.color.red);
    }

    public Med(int id, String name, String description, boolean[] takeDays, int takeTimeH, int takeTimeM, int waitTime, int iconID, int colorID, boolean takeTimeSet,boolean alarmsSet,boolean silenced) {
        this.id=id;
        this.name=name;
        this.description=description;
        this.takeDays = takeDays;
        this.takeTimeH=takeTimeH;
        this.takeTimeM=takeTimeM;
        this.waitTime=waitTime;
        this.iconID=iconID;
        this.colorID=colorID;
        this.takeTimeSet=takeTimeSet;
        this.alarmsSet=alarmsSet;
        this.silenced=silenced;
    }

    public boolean isTakeTimeSet() {
        return takeTimeSet;
    }

    public void setTakeTimeSet(boolean takeTimeSet) {
        this.takeTimeSet = takeTimeSet;
    }

    public boolean areAlarmsSet() {
        return alarmsSet;
    }

    public void setAlarmsSet(boolean alarmsSet) {
        this.alarmsSet = alarmsSet;
    }

    public boolean isSilenced() {
        return silenced;
    }

    public void setSilenced(boolean silenced) {
        this.silenced = silenced;
    }

    public boolean[] getTakeDayArray(){
        return this.takeDays;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTakeHour() {
        return takeTimeH;
    }

    public void setTakeTimeH(int hour) {
        this.takeTimeH = hour;
    }

    public int getTakeMinute() {
        return takeTimeM;
    }

    public void setTakeTimeM(int minute) {
        this.takeTimeM = minute;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public int getImageID(){
       return this.iconID;
    }
    public Drawable getImage(Context c) {
        Drawable image;
        switch (iconID) {
            case (1):
                image = c.getResources().getDrawable(R.drawable.med_icon_2);
                break;
            case (2):
                image = c.getResources().getDrawable(R.drawable.med_icon_3);
                break;
            case (3):
                image = c.getResources().getDrawable(R.drawable.med_icon_4);
                break;
            default:
                image = c.getResources().getDrawable(R.drawable.med_icon_1);
                break;
        }
        return image;
    }

    public void setImage(int imageID) {
        this.iconID = imageID;
    }

    public int getColorID(){
        return this.colorID;
    }
    public int getColor(Context c)
    {
        int color;
        switch (colorID) {
            case (1):
                color = c.getResources().getColor(R.color.orange);
                break;
            case (2):
                color = c.getResources().getColor(R.color.lime);
                break;
            case (3):
                color = c.getResources().getColor(R.color.teal);
                break;
            case (4):
                color = c.getResources().getColor(R.color.lightblue);
                break;
            case (5):
                color = c.getResources().getColor(R.color.blue);
                break;
            default:
                color = c.getResources().getColor(R.color.red);
                break;
        }
        return color;
    }

    public void setColor(int colorID) {
        this.colorID = colorID;
    }

    public String getTakeDays(Context c) {
        String[] dayStrings = c.getResources().getStringArray(R.array.days_array);
        String days ="";
        boolean daily=true;
        for (int i=0;i<takeDays.length;i++){
            if (takeDays[i]) {
                days+=dayStrings[i];
                if (i < 6) {
                    days+=", ";
                }
            } else {
                daily=false;
            }
        }
        if (daily){
            days = c.getResources().getString(R.string.daily);
        }
        return days;
    }

    public void setTakeDays(boolean[] takeDay) {
        this.takeDays = takeDay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Med clone(){
        Med newMed = new Med();
        newMed.id=this.id;
        newMed.takeDays = this.takeDays;
        newMed.name=this.name;
        newMed.description=this.description;
        newMed.takeTimeH=this.takeTimeH;
        newMed.takeTimeM=this.takeTimeM;
        newMed.waitTime=this.waitTime;
        newMed.iconID=this.iconID;
        newMed.colorID=this.colorID;
        newMed.takeTimeSet=this.takeTimeSet;
        return newMed;
    }
}
