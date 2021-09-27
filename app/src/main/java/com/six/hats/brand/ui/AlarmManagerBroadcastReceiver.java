package com.six.hats.brand.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;

import com.six.hats.brand.util.PrefsWrapper;


public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    final public String ALARM_TYPE = "alarmType";
    final public String BOOKING_ID = "bookingID";
    final public String META_ID = "metaID";

    private MediaPlayer mMediaPlayer;
    Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "testapp:testing");
        //Acquire the lock
        // wl.acquire();
        WakeLocker.acquire(context);

        mContext = context;
        //You can do the processing here update the widget/remote
        // views.
        Bundle extras = intent.getExtras();
        StringBuilder msgStr = new StringBuilder();

        if (extras != null) {


            String bookingID = extras.getString(BOOKING_ID, "0");
            String time = extras.getString("time", "0");


           /* switch (alarmType) {
                case JullayConstants.STATUS_CONFIRMED:
                    //This is START alarm
                    playSound();*/
            // Toast.makeText(context, "type -" + alarmType, Toast.LENGTH_SHORT).show();
            PrefsWrapper.with(context).save("trigger", true);
            PrefsWrapper.with(context).save("trigger_id", bookingID);
            PrefsWrapper.with(context).save("trigger_time", time);

            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            launchIntent.putExtra(BOOKING_ID, bookingID);
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(launchIntent);
            // showEndAlarmDetails(alarmType);

                /*    break;
                case JullayConstants.STATUS_START:
                    //This is FINISH alarm
                    playSound();
                    //showEndAlarmDetails(alarmType);
                    Intent launchIntent1 = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
                    launchIntent1.putExtra("alarmType",alarmType);
                    launchIntent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(launchIntent1);
                    break;
            }*/
            msgStr.append("One time Timer : ");


        }
       /* Format formatter = new SimpleDateFormat("hh:mm:ss a");
        msgStr.append(formatter.format(new Date()));

        Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();
*/


        //Release the lock
        //  wl.release();
        WakeLocker.release();
    }


}
