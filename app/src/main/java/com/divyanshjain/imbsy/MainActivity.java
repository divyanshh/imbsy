package com.divyanshjain.imbsy;

import android.app.ActionBar;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Switch mySwitch;
    private TextView switchStatus;
    private PhoneCallReceiver phoneListener;
    private TelephonyManager telephonyManager;
    NotificationManager nMN;
    private static int NOTIFICATION_ID = 123456;
    Button permanentBlocklist, setMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_layout);

        permanentBlocklist = (Button) findViewById(R.id.button);
        //howToUse = (Button) findViewById(R.id.button100);
        //howToUse.setOnClickListener(this);
        permanentBlocklist.setOnClickListener(this);
        setMessage = (Button) findViewById(R.id.button2);
        setMessage.setOnClickListener(this);

        phoneListener = new PhoneCallReceiver(this.getApplicationContext());
        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        switchStatus = (TextView) findViewById(R.id.textView);
        mySwitch = (Switch) findViewById(R.id.switch1);
        telephonyManager.listen(phoneListener, PhoneCallReceiver.LISTEN_CALL_STATE);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    phoneListener.switchOnOrOff = true;
                    switchStatus.setText("SERVICE ON");

                    // Setting up notification
                    showOnNotification();

                } else {
                    switchStatus.setText("SERVICE OFF");
                    //telephonyManager.listen(phoneListener, PhoneCallReceiver.LISTEN_NONE);
                    phoneListener.switchOnOrOff = false;
                    showOffNotification();
                }

            }
        });

    }

    private void showOnNotification() {

        // TODO Auto-generated method stub
        nMN = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification n = new Notification.Builder(this)
                .setContentTitle("Imbsy")
                .setContentText("Imbsy Is On!")
                .setSmallIcon(R.drawable.ic_stat_new_message)
                .setOngoing(true)
                .build();
        nMN.notify(NOTIFICATION_ID, n);
        n.flags |= Notification.FLAG_NO_CLEAR;

    }

    private void showOffNotification() {

        // TODO Auto-generated method stub
        nMN = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification n = new Notification.Builder(this)
                .setContentTitle("Imbsy")
                .setContentText("Imbsy is Off!")
                .setSmallIcon(R.drawable.ic_stat_new_message)
                .build();
        nMN.notify(NOTIFICATION_ID, n);
        n.flags |= Notification.FLAG_NO_CLEAR;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.button:
                Intent openac = new Intent(MainActivity.this, BlockList.class);
                MainActivity.this.startActivity(openac);
                break;

            case R.id.button2:
                Intent opena = new Intent(MainActivity.this, SetMessage.class);
                MainActivity.this.startActivity(opena);
                break;

        }
    }
}