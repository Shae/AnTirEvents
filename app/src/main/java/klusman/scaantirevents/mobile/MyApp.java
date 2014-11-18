package klusman.scaantirevents.mobile;

import android.app.Application;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import klusman.scaantirevents.mobile.Dao.DaoMaster;
import klusman.scaantirevents.mobile.Dao.DaoSession;
import klusman.scaantirevents.mobile.Objects.AlarmSchedulerForSync;


public class MyApp extends Application {
    private DaoSession daoSession = null;

    private static MyApp sInstance;
    public static DaoMaster daoMaster;

    AlarmSchedulerForSync syncAlarm;


    @Override
    public void onCreate() {
        Log.i("TAG", "Application Started");
        super.onCreate();
        sInstance = this;
        getDaoSession();

        syncAlarm = new AlarmSchedulerForSync(this);

        if (!syncAlarm.checkIfAlarmIsActive()){
            syncAlarm.setAlarmSchedule();
            // If sync Schedule is not set... set it
        }

    }

    public static MyApp getInstance() {
        return sInstance;
    }

    public DaoSession getDaoSession() {
        if (daoSession != null) return daoSession;

        DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(getApplicationContext(), getPackageName(), null);
        SQLiteDatabase db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
       // daoMaster.dropAllTables(db, true);
        daoSession = daoMaster.newSession();
        Log.i("TAG", "getDaoSession Started");
        return daoSession;
    }


}
