package sk.stavona.contacts2;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ContactsWidget extends AppWidgetProvider {

    private static final String SYNC_CLICKED    = "automaticWidgetSyncButtonClick";
    private static final String LOAD_STATS_CLICKED = "buttonWidgetLoadStatisticsClick";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        final int N = appWidgetIds.length;
        for (int i = 0; i < N; i++) {

            //Initialize
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.contacts_widget);
            ComponentName watchWidget = new ComponentName(context, ContactsWidget.class);

            //Starting (core) changes
            views.setTextViewText(R.id.textViewRandomOne, "[nothing loaded]");

            // Make changes by action from user
            views.setOnClickPendingIntent(R.id.buttonOpenApp, getPendingSelfIntent(context, SYNC_CLICKED));
            views.setOnClickPendingIntent(R.id.buttonWidgetLoadStatistics, getPendingSelfIntent(context, LOAD_STATS_CLICKED));

            //commit
            appWidgetManager.updateAppWidget(watchWidget, views);
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Important starting code?
        super.onReceive(context, intent);

        //Initialize
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.contacts_widget);
        ComponentName watchWidget = new ComponentName(context, ContactsWidget.class);

        //PreparedStatement
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        //Cases
        if (SYNC_CLICKED.equals(intent.getAction())) {

            Intent openAppIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, openAppIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.buttonOpenApp, pendingIntent);

        } else if (LOAD_STATS_CLICKED.equals(intent.getAction())) {

            remoteViews.setTextViewText(R.id.textViewRandomOne, "Loading...");

            DatabaseContactHelper myDb = new DatabaseContactHelper(context);
            List<EntityContactStatsTBL> listOfStats = new ArrayList();
            Cursor res = myDb.getAllData();

            if (res.getCount() > 0) {
                while (res.moveToNext()) {
                    listOfStats.add(new EntityContactStatsTBL(null,res.getString(1),res.getString(2),res.getString(3),null));
                }
                Random rand = new Random();
                int randomNumber = rand.nextInt(listOfStats.size());
                remoteViews.setTextViewText(R.id.textViewRandomOne, "phoneNo: "
                        +listOfStats.get(randomNumber).getNumber()
                        + ", duration of calls: " + listOfStats.get(randomNumber).getDuration()
                        +", count of SMS "+listOfStats.get(randomNumber).getCountOfSms());
            }
        }
            //commit
            appWidgetManager.updateAppWidget(watchWidget, remoteViews);

    }
}


