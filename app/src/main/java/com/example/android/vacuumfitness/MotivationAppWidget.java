package com.example.android.vacuumfitness;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.example.android.vacuumfitness.database.AppDatabase;
import com.example.android.vacuumfitness.model.Motivator;
import com.example.android.vacuumfitness.ui.MainActivity;
import com.example.android.vacuumfitness.utils.AppExecutors;
import com.example.android.vacuumfitness.utils.KeyUtils;
import com.example.android.vacuumfitness.utils.SharedPrefsUtils;

/**
 * Implementation of App Widget functionality.
 */
public class MotivationAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //Set the widget text
        //Get a Motivator with null check
        Motivator motivator = SharedPrefsUtils.getCurrentMotivator(context);
        CharSequence widgetText;

        //Get motivation Text handle possibility that motivator is null or has no text
        if(motivator != null && !TextUtils.isEmpty(motivator.getMotivationText())){
            widgetText = motivator.getMotivationText();
        } else {
            widgetText = context.getString(R.string.default_motivator_string);
        }

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.motivation_app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        //Now we run the function to have a new motivator next time
        getNewMotivator(context);

        //Intent to launch Training activity
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(KeyUtils.WIDGET_INTENT, true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.appwidget_start, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(final Context context) {
        // Enter relevant functionality for when the first widget is created
        //Here we save the motivators row count to our shared prefs the first time
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                int rowCount = AppDatabase.getInstance(context).motivatorDao().getMotivatorsRowCount();
                SharedPrefsUtils.saveMotivatorsRowCount(context, rowCount);
            }
        });
        //Noe we put our first Motivator to Shared prefs
        getNewMotivator(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static void getNewMotivator(final Context context){
        //Get which Motivator was uses last time
        int lastMotivatorId = SharedPrefsUtils.getLastMotivatorId(context);
        //Chang motivator id
        if(lastMotivatorId <= SharedPrefsUtils.getMotivatorsRowCount(context)){
            lastMotivatorId++;
            //Save in shared prefs
            SharedPrefsUtils.saveLastMotivatorId(context, lastMotivatorId);
        } else {
            lastMotivatorId = 1;
            //Save in shared prefs
            SharedPrefsUtils.saveLastMotivatorId(context, lastMotivatorId);
        }
        //Now we get the Motivator from database and save it to shared prefs
        //I am using async task because i want the onPostExecute to save in shared prefs
        new GetNewMotivatorFromDb(context).execute(lastMotivatorId);
    }

    private static class GetNewMotivatorFromDb extends AsyncTask<Integer, Void, Motivator>{

        private static Context mContext;

        private GetNewMotivatorFromDb(Context context){
            mContext = context;
        }

        @Override
        protected Motivator doInBackground(Integer... integers) {
            Motivator motivator = AppDatabase.getInstance(mContext).motivatorDao().loadMotivatorById(integers[0]);
            return motivator;
        }

        @Override
        protected void onPostExecute(Motivator motivator) {
            super.onPostExecute(motivator);
            SharedPrefsUtils.saveCurrentMotivator(mContext, motivator);
        }
    }
}