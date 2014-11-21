package klusman.scaantirevents.mobile.Objects;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import klusman.scaantirevents.mobile.SyncDataBroadcastReceiver;
import klusman.scaantirevents.mobile.Tasks.MyAlarmReceiver;

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
//        Calendar midnightCalendar = Calendar.getInstance();
//        //set the time to midnight tonight
//        midnightCalendar.set(Calendar.HOUR_OF_DAY, 0);
//        midnightCalendar.set(Calendar.MINUTE, 0);
//        midnightCalendar.set(Calendar.SECOND, 0);
//
//        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
//        //create a pending intent to be called at midnight
//
//        Intent update = new Intent(mContext, SyncDataBroadcastReceiver.class);
//        update.setAction("klusman.scaantirevents.SYNC_ALARM");
//        PendingIntent pendingUpdateIntent = PendingIntent.getService(mContext, 0, update, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        // Cancel alarms
//        try {
//            am.setRepeating(
//                    AlarmManager.RTC_WAKEUP,
//                    midnightCalendar.getTimeInMillis(),
//                    AlarmManager.INTERVAL_DAY,
//                    pendingUpdateIntent);
//            Log.i("TAG", "Sync was Activated");
//        } catch (Exception e) {
//            Log.e("TAG", "AlarmManager setup was not failed. " + e.toString());
//        }
//
//        //schedule time for pending intent, and set the interval to day so that this event
//        // will repeat at the selected time every day
//
//        Log.i("TAG", "Sync Alarm SET!");


        AlarmManager alarmMgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, SyncDataBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
        //update 2x a day 12hrs apart
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,AlarmManager.INTERVAL_HALF_DAY, AlarmManager.INTERVAL_HALF_DAY , pendingIntent);



    }

    public void cancelPendingAlarms(){
        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Log.i("TAG", "Alarm Canceled!");

        Intent update = new Intent(mContext, SyncDataBroadcastReceiver.class);
        PendingIntent pendingUpdateIntent = PendingIntent.getBroadcast(
                mContext,
                0,
                update,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Cancel alarms
        try {
            am.cancel(pendingUpdateIntent);
            Log.i("TAG", "Sync Alarm was canceled");
        } catch (Exception e) {
            Log.e("TAG", "AlarmManager update was not canceled. " + e.toString());
        }
    }

    public boolean checkIfAlarmIsActive(){

        Log.i("TAG", "Check if alarm is active function");
        Intent update = new Intent(mContext, SyncDataBroadcastReceiver.class);
      //  PendingIntent pendingUpdateIntent = PendingIntent.getService(mContext, 0, update, PendingIntent.FLAG_UPDATE_CURRENT);

        boolean alarmUp = (PendingIntent.getBroadcast(
                mContext,
                0,
                update,
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
















