package org.port0.nriedmann.dailymeds;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.Bundle;
import android.widget.RemoteViews;

/**
 * Created by captain on 04.09.2015.
 */
public class MedWidgetProvider extends AppWidgetProvider {

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        if (newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH)>40){
            //SHOW HIDDEN STUFF
            appWidgetManager.updateAppWidget(appWidgetId,new RemoteViews(context.getPackageName(),R.layout.med_widget_large));
        } else {
            //HIDE STUFF
            appWidgetManager.updateAppWidget(appWidgetId,new RemoteViews(context.getPackageName(),R.layout.med_widget_small));
        }
    }
}
