package com.iseva.app.source;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.ComponentName;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.TelephonyManager;
import android.telephony.PhoneStateListener;
import android.text.Html;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Call_PhoneListener extends PhoneStateListener
{
    private Context context;
    private Boolean lastStateRinging = false;
    public Call_PhoneListener(Context c) {
        Log.i("SUSHIL", "Call_PhoneListener constructor");
        context = c;
    }

    public void onCallStateChanged (int state, String incomingNumber)
    {
        Log.i("SUSHIL", "Call_PhoneListener::onCallStateChanged state:" + state + " incomingNumber:" + incomingNumber);

        switch (state) {
        case TelephonyManager.CALL_STATE_IDLE:
            Log.i("SUSHIL", "CALL_STATE_IDLE");

            break;
        case TelephonyManager.CALL_STATE_RINGING:
            Log.i("SUSHIL", "CALL_STATE_RINGING");
            Object_AppConfig config = new Object_AppConfig(context);
            if(config.isMerchant())
                 checkNumber(incomingNumber);
            lastStateRinging = true;
            break;
        case TelephonyManager.CALL_STATE_OFFHOOK:
            /*if(!lastStateRinging){
                TelephonyManager TMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
                String mPhoneNumber = TMgr.getLine1Number();
                registerNumber(mPhoneNumber);
            }
            lastStateRinging = false;*/
            break;
        }
    }



    public void registerNumber(String num){
        try{
            if(!num.equals("")) {
                Custom_ConnectionDetector con = new Custom_ConnectionDetector(context);
                if (con.isConnectingToInternet()) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("number", num);
                   // map.put("name", name);
                    map.put("deviceid", Globals.getdeviceId(context));
                    Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                            Request.Method.POST,
                            Custom_URLs_Params.getURL_CallNumberRegister(),
                            map, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("SUSHIL", "json Response recieved !!"+response);

                        }


                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError err) {
                            Log.i("SUSHIL", "ERROR VolleyError");

                        }
                    });

                    Custom_VolleyAppController.getInstance().addToRequestQueue(
                            jsonObjectRQST);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void checkNumber(String num){
        try{
            if(!num.equals("")) {
                Custom_ConnectionDetector con = new Custom_ConnectionDetector(context);
                if (con.isConnectingToInternet()) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("number", num);
                    // map.put("name", name);
                    map.put("deviceid", Globals.getdeviceId(context));
                    Custom_VolleyObjectRequest jsonObjectRQST = new Custom_VolleyObjectRequest(
                            Request.Method.POST,
                            Custom_URLs_Params.getURL_CallNumberCheck(),
                            map, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("SUSHIL", "json Response recieved !!"+response);
                            parser(response);
                        }


                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError err) {
                            Log.i("SUSHIL", "ERROR VolleyError");

                        }
                    });

                    Custom_VolleyAppController.getInstance().addToRequestQueue(
                            jsonObjectRQST);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void parser(JSONObject obj){
        if(obj==null){
            return;
        }else {
            try{
                if (obj.has("success")) {
                    if (obj.getInt("success") == 1) {
                         noti();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void noti() {

        Intent intent = new Intent(context, Activity_Home.class);
        if (android.os.Build.VERSION.SDK_INT < 11) {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    context).setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle(context.getResources().getString(R.string.app_name)).setContentText(context.getResources().getString(R.string.app_name));

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(Activity_Home.class);
            stackBuilder.addNextIntent(intent);
            PendingIntent resultPendingIntent = stackBuilder
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on.
            mNotificationManager.notify(999, mBuilder.build());

            return;
        }

        // Starts the activity on notification click
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Create the notification with a notification builder
        Notification notification;

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(context.getResources().getString(R.string.app_name)).setContentText("Call from iSeva")
                    .setContentIntent(pIntent)
                            // .setContentInfo(
                            // String.valueOf(++MainActivity.numOfNotifications) )
                    .setLights(0xFFFF0000, 500, 500).getNotification();
        } else {
            notification = new Notification.Builder(context)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setWhen(System.currentTimeMillis())
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setContentText("Call from iSeva").setContentIntent(pIntent)
                            // .setContentInfo(
                            // String.valueOf(++MainActivity.numOfNotifications) )
                    .setLights(0xFFFF0000, 500, 500).build();
        }

        // Remove the notification on click
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager manager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        // manager.notify(R.string.app_name, notification);

        {
            // Wake Android Device when notification received
            PowerManager pm = (PowerManager) context
                    .getSystemService(Context.POWER_SERVICE);
            final PowerManager.WakeLock mWakelock = pm.newWakeLock(
                    PowerManager.FULL_WAKE_LOCK
                            | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                    "GCM_PUSH");
            mWakelock.acquire();

            // Play default notification sound
            // notification.defaults |= Notification.DEFAULT_SOUND;

            Uri notificationuri = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone ringer = RingtoneManager.getRingtone(
                    context.getApplicationContext(), notificationuri);
            ringer.play();

            // Vibrate if vibrate is enabled
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            manager.notify(0, notification);

            // Timer before putting Android Device to sleep mode.
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                public void run() {
                    mWakelock.release();
                }
            };
            timer.schedule(task, 5000);
        }
    }
}
