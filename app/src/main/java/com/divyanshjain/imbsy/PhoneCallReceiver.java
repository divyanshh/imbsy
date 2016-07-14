package com.divyanshjain.imbsy;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Divyansh Jain on 6/27/2016.
 */
public class PhoneCallReceiver extends PhoneStateListener {

    private Context context;
    private NotificationManager nMN1;
    private static int NOTIFICATION_ID1 = 1234567;
    private Database database;
    public Boolean switchOnOrOff;

    public PhoneCallReceiver(Context context) {
        this.context = context;
        this.database = new Database(context);
        this.nMN1 = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        switchOnOrOff = false;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        final List<String> blockedNumbers = database.open().getValues();
        database.close();

        switch (state) {

            case TelephonyManager.CALL_STATE_RINGING:

                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                //Turn ON the mute
                audioManager.setStreamMute(AudioManager.STREAM_RING, true);
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                try {
                    Class clazz = Class.forName(telephonyManager.getClass().getName());
                    Method method = clazz.getDeclaredMethod("getITelephony");
                    method.setAccessible(true);
                    ITelephony telephonyService = (ITelephony) method.invoke(telephonyManager);
                    //Checking incoming call number

                    if (matches(incomingNumber, blockedNumbers) || switchOnOrOff) {
                        //telephonyService.silenceRinger();//Security exception problem
                        telephonyService = (ITelephony) method.invoke(telephonyManager);
                        telephonyService.silenceRinger();
                        telephonyService.endCall();
                        if (matches(incomingNumber, blockedNumbers)) {
                            showBlockedNotification(incomingNumber);
                        } else if (switchOnOrOff) {
                            showNotification(incomingNumber);
                            sendMessage(incomingNumber);

                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
                }
                //Turn OFF the mute
                audioManager.setStreamMute(AudioManager.STREAM_RING, false);
                break;
            case PhoneStateListener.LISTEN_CALL_STATE:
        }
        super.onCallStateChanged(state, incomingNumber);
    }

    private void sendMessage(String phoneNo) {

        StringBuffer stringBuffer = new StringBuffer();
        try {
            //Attaching BufferedReader to the FileInputStream by the help of InputStreamReader
            InputStream is = context.openFileInput("message.txt");
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(is));
            String inputString;
            //Reading data line by line and storing it into the stringbuffer
            while ((inputString = inputReader.readLine()) != null) {
                stringBuffer.append(inputString + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, stringBuffer.toString(), null, null);
    }


    private void showNotification(String phoneNo) {

        Notification n1 = new Notification.Builder(context)
                .setContentTitle("Imbsy")
                .setContentText("Missed Call from " + phoneNo)
                .setSmallIcon(R.drawable.ic_stat_new_message)
                .build();
        n1.vibrate = new long[]{100, 200, 100, 500};
        nMN1.notify(NOTIFICATION_ID1, n1);

    }

    private void showBlockedNotification(String phoneNo) {

        Notification n1 = new Notification.Builder(context)
                .setContentTitle("Imbsy")
                .setContentText("Missed Call Blocked No " + phoneNo)
                .setSmallIcon(R.drawable.ic_stat_new_message)
                .build();
        n1.vibrate = new long[]{100, 200, 100, 500};
        nMN1.notify(NOTIFICATION_ID1, n1);

    }

    private boolean matches(String incomingNumber, List<String> blockedNumbers) {
        for (String blockedNumber : blockedNumbers) {
            if (incomingNumber.contains("+91" + blockedNumber)) {
                return true;
            }
        }
        return false;
    }

}
