package com.iseva.app.source;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.telephony.PhoneStateListener;
import android.util.Log;

public class Call_BroadcastReceiver extends BroadcastReceiver
{
    public void onReceive(Context context, Intent intent) {
        Log.i("SUSHIL", "Call_BroadcastReceiver::onReceive got Intent: " + intent.toString());
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            String numberToCall = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.i("SUSHIL", "Call_BroadcastReceiver intent has EXTRA_PHONE_NUMBER: " + numberToCall);
        }

        Call_PhoneListener phoneListener = new Call_PhoneListener(context);
        TelephonyManager telephony = (TelephonyManager)
            context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        Log.i("SUSHIL", "PhoneStateListener");
    }
}
