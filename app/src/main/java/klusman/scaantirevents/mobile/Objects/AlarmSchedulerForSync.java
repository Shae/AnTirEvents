package klusman.scaantirevents.mobile.Objects;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Narrook on 11/17/14.
 */
public class AlarmSchedulerForSync
{
    Context mContext;

    public AlarmSchedulerForSync(Context context){
        mContext = context;
    }

    public void setAlarmSchedule() {
        Calendar midnightCalendar = Calendar.getInstance();
        //set the time to midnight tonight
        midnightCalendar.set(Calendar.HOUR_OF_DAY, 0);

        midnightCalendar.set(Calendar.MINUTE, 0);
        midnightCalendar.set(Calendar.SECOND, 0);

        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        //create a pending intent to be called at midnight
        PendingIntent midnightPI = PendingIntent.getService(
                mContext,
                0,
                new Intent("klusman.scaantirevents.mobile.Tasks.SyncDataBroadcastReceiver"),
                PendingIntent.FLAG_UPDATE_CURRENT);

        //schedule time for pending intent, and set the interval to day so that this event
        // will repeat at the selected time every day
        am.setRepeating(
                AlarmManager.RTC_WAKEUP,
                midnightCalendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                midnightPI);
        Log.i("TAG", "Sync Alarm SET!");
    }

    public void cancelPendingAlarms(){
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        PendingIntent midnightPI = PendingIntent.getService(
                mContext,
                0,
                new Intent("klusman.scaantirevents.mobile.Tasks.SyncDataBroadcastReceiver"),
                PendingIntent.FLAG_UPDATE_CURRENT);
        //cancel it
        am.cancel(midnightPI);

        Log.i("TAG", "Alarm Canceled!");
    }

    public boolean checkIfAlarmIsActive(){
        boolean alarmUp = (PendingIntent.getBroadcast(mContext, 0,
                new Intent("klusman.scaantirevents.mobile.Tasks.SyncDataBroadcastReceiver"),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (alarmUp)
        {
            Log.i("TAG", "SYNC SCHEDULE is already active");
            return true;
        }else{
            Log.i("TAG", "SYNC SCHEDULE is NOT active");
            return false;
        }
    }

}
