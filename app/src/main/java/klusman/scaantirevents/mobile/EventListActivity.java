package klusman.scaantirevents.mobile;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import klusman.scaantirevents.R;
import klusman.scaantirevents.mobile.Dao.DaoSession;
import klusman.scaantirevents.mobile.Dao.EventDao;
import klusman.scaantirevents.mobile.Objects.Event;

/**
 * Created by Narrook on 8/23/14.
 */
public class EventListActivity extends ListActivity{
    EventDao eDao;
    DaoSession sDao;

    //ListView mainLV;
    List<Event> eList = new ArrayList<Event>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list_activity);

        sDao = MyApp.getInstance().getDaoSession();
        eDao = sDao.getEventDao();
        eDao.queryBuilder().orderAsc(EventDao.Properties.EventStart);

        //mainLV = (ListView)findViewById(R.id.list);

        eList.addAll(eDao.loadAll());

        EventArrayAdapter eaa = new EventArrayAdapter(EventListActivity.this, R.layout.main_event_list_cell, eList);
        setListAdapter(eaa);



    }
}
