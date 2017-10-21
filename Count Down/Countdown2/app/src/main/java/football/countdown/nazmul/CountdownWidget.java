package football.countdown.nazmul;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Implementation of App Widget functionality.
 */
public class CountdownWidget extends AppWidgetProvider {

    public  int i = 1;


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);


            // Construct the RemoteViews object

          final  RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.countdown_widget);


        //    DateFormat df = DateFormat.getTimeInstance();
          //  df.setTimeZone(TimeZone.getTimeZone("gmt"));
            //String gmtTime = df.format(new Date());


                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                "yyyy-MM-dd");

                        dateFormat.setTimeZone(TimeZone.getTimeZone("gmt"));
                        // Here Set your Event Date
                        Date futureDate = dateFormat.parse("2017-02-07");
                        Date currentDate = new Date();
                        if (!currentDate.after(futureDate)) {
                            long diff = futureDate.getTime()
                                    - currentDate.getTime();
                            long days = diff / (24 * 60 * 60 * 1000);
                            diff -= days * (24 * 60 * 60 * 1000);
                            long hours = diff / (60 * 60 * 1000);
                            diff -= hours * (60 * 60 * 1000);
                            long minutes = diff / (60 * 1000);
                            diff -= minutes * (60 * 1000);
                            long seconds = diff / 1000;

                            //views.setTextViewText(R.id.appwidget_text, widgetText);
                            views.setTextViewText(R.id.txtWTimerDay, "" + String.format("%02d", days));
                            views.setTextViewText(R.id.txtWTimerHour, "" + String.format("%02d", hours));
                            views.setTextViewText(R.id.txtWTimerMinute,"" + String.format("%02d", minutes));
                            views.setTextViewText(R.id.txtWTimerSecond,"" + String.format("%02d", seconds));

                        } else {
                            /*linearLayout1.setVisibility(View.VISIBLE);
                            linearLayout2.setVisibility(View.GONE);
                            tvEvent.setText("Android Event Start");
                            handler.removeCallbacks(runnable);*/

                            views.setTextViewText(R.id.wevent, "Available");
                            views.setViewVisibility(R.id.wevent, View.VISIBLE);

                            // handler.removeMessages(0);


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }





            Intent intent = new Intent(context, CountdownWidget.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            /// Make Widget Clickable
            Intent main = new Intent(context, MainActivity.class);
            PendingIntent pendingIntentformain = PendingIntent.getActivity(context, 0, main, 0);
            views.setOnClickPendingIntent(R.id.full, pendingIntentformain);


            appWidgetManager.updateAppWidget(appWidgetId, views);


            //views.setOnClickPendingIntent(R.id.increment, pendingIntent);
            try {
                try {


                    Thread.sleep(i);
                    i=1000;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }



}

