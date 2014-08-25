package klusman.scaantirevents.mobile;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import klusman.scaantirevents.R;
import klusman.scaantirevents.mobile.Dao.DaoSession;
import klusman.scaantirevents.mobile.Dao.EventDao;
import klusman.scaantirevents.mobile.Objects.CellEvent;
import klusman.scaantirevents.mobile.Objects.Event;
import klusman.scaantirevents.mobile.Objects.ListHeader;

/**
 * Created by Narrook on 8/23/14.
 */
public class EventListActivity extends ListActivity {
    EventDao eDao;
    DaoSession sDao;

    //ListView mainLV;
    List<Event> eList = new ArrayList<Event>();
    List<View> vList = new ArrayList<View>();
    ListView mainListView;
    Calendar currentMo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list_activity);


        mainListView = getListView();
        getActionBar().setHomeButtonEnabled(true);

        sDao = MyApp.getInstance().getDaoSession();
        eDao = sDao.getEventDao();
        eList.addAll(eDao.queryBuilder().orderAsc(EventDao.Properties.EventStart).listLazy());

        currentMo = Calendar.getInstance();
        currentMo.add(Calendar.MONTH, -4);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            getAll();
        } else if (extras.get("TYPE").toString().compareToIgnoreCase("FAV") == 0) {
            getFavorites();
        }

        Log.i("TAG", "eListSize = " + eList.size());


        EventArrayAdapter eaa = null;
        try {
            eaa = new EventArrayAdapter(EventListActivity.this, R.layout.main_event_list_cell, vList);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setListAdapter(eaa);

    }

    public void getAll() {
        for (Event e : eList) {
            Calendar mCal = Calendar.getInstance();
            Calendar minStorageDate = Calendar.getInstance();
            long millSecDaysBack = 35 * 86400000;
            long newDate = minStorageDate.getTimeInMillis() - millSecDaysBack;
            minStorageDate.setTimeInMillis(newDate);  // should set the date back 35 days

            try {
                mCal = calDateFromString(e.getEventStart());
            } catch (ParseException e1) {
                e1.printStackTrace();
            }

            if(mCal.before(minStorageDate)){
                removeOldEvents(e);
                continue;  // should skip this one and move on
            }


            if (mCal.get(Calendar.MONTH) != currentMo.get(Calendar.MONTH)) {
                if(mCal.get(Calendar.MONTH) > currentMo.get(Calendar.MONTH)) {
                    currentMo = mCal;
                    ListHeader lh = new ListHeader(EventListActivity.this, mCal);
                    View header = lh.getView();
                    vList.add(header);

                    CellEvent ce = new CellEvent(EventListActivity.this, e);
                    View eventCell = ce.getView();
                    vList.add(eventCell);
                }else{
                    currentMo = mCal;
                    ListHeader lh = new ListHeader(EventListActivity.this, mCal);
                    View header = lh.getView();
                    vList.add(header);

                    CellEvent ce = new CellEvent(EventListActivity.this, e);
                    View eventCell = ce.getView();
                    vList.add(eventCell);
                }

            } else {
                currentMo = mCal;
                CellEvent ce = new CellEvent(EventListActivity.this, e);
                View eventCell = ce.getView();
                vList.add(eventCell);
            }

        }
    }

    public void getFavorites() {
        for (Event e : eList) {
            if (e.getEventFavorite() != null
                    && e.getEventFavorite().compareToIgnoreCase("True") == 0) {
                Calendar mCal = null;

                try {
                    mCal = calDateFromString(e.getEventStart());
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }

                // Log.i("TAG","mCal month/year = " + String.valueOf(mCal.get(Calendar.MONTH)) + "/" + String.valueOf(mCal.get(Calendar.YEAR)));


                if (mCal.get(Calendar.MONTH) != currentMo.get(Calendar.MONTH)) {
                    if(mCal.get(Calendar.MONTH) > currentMo.get(Calendar.MONTH)) {
                        currentMo = mCal;
                        ListHeader lh = new ListHeader(EventListActivity.this, mCal);
                        View header = lh.getView();
                        vList.add(header);

                        CellEvent ce = new CellEvent(EventListActivity.this, e);
                        View eventCell = ce.getView();
                        vList.add(eventCell);
                    }else{
                        currentMo = mCal;
                        ListHeader lh = new ListHeader(EventListActivity.this, mCal);
                        View header = lh.getView();
                        vList.add(header);

                        CellEvent ce = new CellEvent(EventListActivity.this, e);
                        View eventCell = ce.getView();
                        vList.add(eventCell);
                    }
                } else {
                    currentMo = mCal;

                    CellEvent ce = new CellEvent(EventListActivity.this, e);
                    View eventCell = ce.getView();
                    vList.add(eventCell);
                }
            }
        }
    }

    public static Calendar calDateFromString(String string) throws ParseException {
        String s = string;
        Calendar cal = stringToCalendar(s);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return cal;
    }

    public static Calendar stringToCalendar(String strDate) throws ParseException {
        String FORMAT_DATETIME = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATETIME);
        Date date = sdf.parse(strDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent goHome = new Intent(EventListActivity.this, MyActivity.class);
                startActivity(goHome);
                return true;
            default:
                return super.onMenuItemSelected(featureId, item);
        }
    }


    public void removeOldEvents(Event oldEvent){
        eDao.delete(oldEvent);
    }
}
