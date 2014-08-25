package klusman.scaantirevents.mobile;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import klusman.scaantirevents.R;
import klusman.scaantirevents.mobile.Dao.DaoMaster;
import klusman.scaantirevents.mobile.Dao.DaoSession;
import klusman.scaantirevents.mobile.Dao.EventDao;
import klusman.scaantirevents.mobile.Objects.Event;

/**
 * Created by Narrook on 8/24/14.
 */
public class EventDetail extends Activity
{
    DaoMaster mDao;
    EventDao eDao;
    DaoSession sDao;
    String id;
    Event e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail);

        getActionBar().setHomeButtonEnabled(true);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            id = extras.getString("ID");
        }

        mDao = MyApp.daoMaster;
        sDao = MyApp.getInstance().getDaoSession();
        eDao = sDao.getEventDao();

        e = eDao.load(id);

        TextView eventName = (TextView)findViewById(R.id.detail_event_name);
            eventName.setText(e.getEventName());

        TextView eventLocation = (TextView)findViewById(R.id.detail_location);

            String loc = getCleanLocation(e.getEventLocation());
            eventLocation.setText(loc);

        TextView eventDate = (TextView)findViewById(R.id.detail_date_range);

        TextView eventDesc = (TextView)findViewById(R.id.detail_description);
            eventDesc.setText(e.getEventDescription());

        String sDate = "";
        try {
            sDate = dateConverter(e.getEventStart(), e.getEventEnd());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        eventDate.setText(sDate);
    }

    private String getCleanLocation(String location){
        String finalField = "";
        String[] fields = location.split("->");
        finalField = fields[0];

        String[] fieldsAddress = fields[0].split("[0-9]", 2);
        if(fieldsAddress.length > 1){
            finalField = fieldsAddress[0] + "\n" +  fieldsAddress[1];
        }else{
            finalField = fieldsAddress[0];
        }



        return finalField;

    }

    private String dateConverter(String start, String end) throws ParseException
    {

        String eventRunDate = "";
        String eventStartMini = "";

        if (start != null)
        {
            Calendar cStart = calDateFromString(start);

            String Day = String.valueOf(cStart.get(Calendar.DAY_OF_MONTH));
            String Month = getMonthString(cStart.get(Calendar.MONTH));
            String Year = String.valueOf(cStart.get(Calendar.YEAR));

            eventRunDate = Month + " " + Day + ", " + Year;
            eventStartMini = Month + " " + Day;

            if (end != null)
            {
                Calendar cEnd = calDateFromString(end);

                String eDay = String.valueOf(cEnd.get(Calendar.DAY_OF_MONTH));
                String eMonth = getMonthString(cEnd.get(Calendar.MONTH));
                String eYear = String.valueOf(cEnd.get(Calendar.YEAR));

                if (cEnd.get(Calendar.DAY_OF_MONTH) > cStart.get(Calendar.DAY_OF_MONTH))
                {
                    eventRunDate = (eventStartMini) + "  to  " + eMonth + " " + eDay + ", " + eYear;
                }

            }
        }
        return eventRunDate;
    }


    public static Calendar calDateFromString(String string) throws ParseException
    {
        String s = string;
        Calendar cal = stringToCalendar(s);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // System.err.println(sdf.format(cal.getTime()));
        return cal;
    }

    public static Calendar stringToCalendar(String strDate) throws ParseException
    {
        String FORMAT_DATETIME = "yyyy-MM-dd'T'HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATETIME);
        Date date = sdf.parse(strDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public String getMonthString(int monthInt)
    {
        String month = "";
        switch (monthInt){
            case 0:
                month = "Jan";
                break;
            case 1:
                month = "Feb";
                break;
            case 2:
                month = "Mar";
                break;
            case 3:
                month = "Apr";
                break;
            case 4:
                month = "May";
                break;
            case 5:
                month = "Jun";
                break;
            case 6:
                month = "Jul";
                break;
            case 7:
                month = "Aug";
                break;
            case 8:
                month = "Sep";
                break;
            case 9:
                month = "Oct";
                break;
            case 10:
                month = "Nov";
                break;
            case 11:
                month = "Dec";
                break;

            default:
                month = "TBD";
                break;

        }
        return month;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.event_detail_menu, menu);

        MenuItem item = menu.findItem(R.id.action_toggle_favorite);
        String fav ;
        if(e.getEventFavorite() == null){
            fav = "False";
        }else{
            fav = e.getEventFavorite();
        }


        if(fav.compareToIgnoreCase("False") == 0){
            item.setIcon(android.R.drawable.star_big_off);
        }else{
            item.setIcon(android.R.drawable.star_big_on);
        }
        return true;

    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                Intent goHome = new Intent(EventDetail.this , MyActivity.class);
                startActivity(goHome);
                return true;

            case R.id.action_toggle_favorite:

                String fav ;
                if(e.getEventFavorite() == null){
                    fav = "False";
                }else{
                    fav = e.getEventFavorite();
                }


                if(fav.compareToIgnoreCase("False") == 0){
                    e.setEventFavorite("True");
                    item.setIcon(android.R.drawable.star_big_on);
                    eDao.update(e);
                }else{
                    e.setEventFavorite("False");
                    item.setIcon(android.R.drawable.star_big_off);
                    eDao.update(e);
                }

            default:
                return super.onMenuItemSelected(featureId, item);
        }
    }
}
