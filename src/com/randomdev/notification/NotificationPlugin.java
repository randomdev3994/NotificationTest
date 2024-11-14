package com.randomdev.notification;

import android.Manifest;
import android.content.pm.PackageManager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.widget.RemoteViews;
import androidx.core.content.ContextCompat;
import androidx.core.app.NotificationCompat;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.myapp.R;

import java.util.Random;

public class NotificationPlugin extends CordovaPlugin {

    private static final String CHANNEL_ID = "custom_notification_channel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("showNotification")) {
            /*JSONObject options = args.getJSONObject(0);
            String title = options.getString("title");
            String text = options.getString("text");*/

            showCustomNotification(args.getJSONObject(0));
            callbackContext.success("Notification displayed.");
            return true;
        }
        return false;
    }

    private void showCustomNotification(JSONObject options) throws JSONException{

        Context context = cordova.getActivity().getApplicationContext();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(cordova.getActivity(), "android.permission.POST_NOTIFICATIONS")
                != PackageManager.PERMISSION_GRANTED) {
                
                cordova.requestPermission(this, 0, "android.permission.POST_NOTIFICATIONS");
                }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Custom Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        if(options.has("left") && options.has("right") && options.has("middle") && options.has("action") && options.has("Progress")){
        RemoteViews notificationLayout = new RemoteViews(context.getPackageName(), R.layout.custom_notification);
        notificationLayout.setTextViewText(R.id.left_text, options.getString("left")); 
        notificationLayout.setTextViewText(R.id.right_text, options.getString("right"));
        notificationLayout.setTextViewText(R.id.center_text, options.getString("middle"));
        notificationLayout.setTextViewText(R.id.bottom_text, options.getString("action"));
        notificationLayout.setProgressBar(R.id.progress_bar, 100, options.getInt("progress"), false);

        Notification customNotification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContent(notificationLayout)
                .setCustomBigContentView(notificationLayout)
                .setOngoing(false)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        notificationManager.notify(NOTIFICATION_ID, customNotification);}
    }
}
