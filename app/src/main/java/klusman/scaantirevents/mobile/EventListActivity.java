package klusman.scaantirevents.mobile;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

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
public class EventListActivity extends ListActivity{
    EventDao eDao;
    DaoSession sDao;

    //ListView mainLV;
    List<Event> eList = new ArrayList<Event>();
    List<View> vList = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list_activity);
        getActionBar().setHomeButtonEnabled(true);
        sDao = MyApp.getInstance().getDaoSession();
        eDao = sDao.getEventDao();
        eList.addAll(eDao.queryBuilder().orderAsc(EventDao.Properties.EventStart).listLazy());

        Log.i("TAG", "eListSize = " + eList.size());


        Calendar currentMo = Calendar.getInstance();
        currentMo.add(Calendar.MONTH, -4);

        for(Event e : eList){
            Calendar mCal = null;

            try {
                mCal = calDateFromString(e.getEventStart());
            } catch (ParseException e1) {
                e1.printStackTrace();
            }

           // Log.i("TAG","mCal month/year = " + String.valueOf(mCal.get(Calendar.MONTH)) + "/" + String.valueOf(mCal.get(Calendar.YEAR)));


            if( mCal.get(Calendar.MONTH) > currentMo.get(Calendar.MONTH))
            {


                currentMo = mCal;
                ListHeader lh = new ListHeader(EventListActivity.this, mCal);
                View header = lh.getView();
                vList.add(header);

                CellEvent ce = new CellEvent(EventListActivity.this, e);
                View eventCell = ce.getView();
                vList.add(eventCell);

            }else{
                currentMo = mCal;

                CellEvent ce = new CellEvent(EventListActivity.this, e);
                View eventCell = ce.getView();
                vList.add(eventCell);
            }
        }



        EventArrayAdapter eaa = null;
        try {
            eaa = new EventArrayAdapter(EventListActivity.this, R.layout.main_event_list_cell, vList);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setListAdapter(eaa);

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

}
