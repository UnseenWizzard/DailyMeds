package org.port0.nriedmann.dailymeds;

import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.RemoteViews;

import java.util.ArrayList;

/**
 * Created by captain on 04.09.2015.
 */
public class WidgetConfigActivity extends AppCompatActivity {

    private int chosenMedID;
    private int widgetID;
    private ArrayList<Med> medList;
    private AppWidgetManager appWidgetManager;
    private RemoteViews views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_widget_config);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        widgetID=0;
        chosenMedID=-1;
        if (extras!=null){
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        //Load name List
        medList = new ArrayList<Med>();
        DataTools.load(medList, this);
        appWidgetManager = AppWidgetManager.getInstance(this);
        //ADD CONFIGURE DIALOG
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_dialog_widget);
        builder.setItems(DataTools.getMedNameList(medList), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                chosenMedID = medList.get(which).getId();
                views = new RemoteViews(getPackageName(), R.layout.med_widget_small);
                views.setString(R.id.wMedName,"",medList.get(which).getName());
                views.setString(R.id.wMedTime, "", medList.get(which).getTakeHour() + ":" + medList.get(which).getTakeMinute());
                views.setImageViewBitmap(R.id.wMedIcon,((BitmapDrawable)medList.get(which).getImage(getApplicationContext())).getBitmap() );

                appWidgetManager.updateAppWidget(widgetID, views);
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });
        builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chosenMedID = -1;
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
