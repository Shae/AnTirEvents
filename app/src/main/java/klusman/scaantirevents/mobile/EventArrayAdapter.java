package klusman.scaantirevents.mobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import klusman.scaantirevents.R;
import klusman.scaantirevents.mobile.Objects.CellEvent;
import klusman.scaantirevents.mobile.Objects.Event;
import klusman.scaantirevents.mobile.Objects.ListHeader;

/**
 * Created by Narrook on 8/23/14.
 */
public class EventArrayAdapter extends ArrayAdapter<Event>{


    Calendar currentMo;

    private LayoutInflater inflater;
    private View view;
    Context context;
    List<Event> eList = new ArrayList<Event>();
    List<View> vList = new ArrayList<View>();
    int cellLayoutResource;


    public EventArrayAdapter(Context context, int resource, List<Event> objects) throws ParseException {
        super(context, resource, objects);
        this.context = context;
        this.eList = objects;
        this.cellLayoutResource = resource;

        Calendar currentMo = Calendar.getInstance();
        currentMo.add(Calendar.MONTH, -4);

        for(Event e : eList){
            Calendar mCal = calDateFromString(e.getEventStart());
            if(( mCal.get(Calendar.MONTH) > currentMo.get(Calendar.MONTH))
                    || (mCal.get(Calendar.MONTH) > currentMo.get(Calendar.MONTH)))
            {
                currentMo = mCal;

                ListHeader lh = new ListHeader(context, mCal);
                View header = lh.getView();
                vList.add(header);

                CellEvent ce = new CellEvent(context, e);
                View eventCell = ce.getView();
                vList.add(eventCell);

            }else{
                CellEvent ce = new CellEvent(context, e);
                View eventCell = ce.getView();
                vList.add(eventCell);
            }
        }


    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        return view;

    }


    private String dateConverter(String start, String end) throws ParseException {

        String eventRunDate = "";
        String eventStartMini = "";

        if(start != null) {
            Calendar cStart = calDateFromString(start);

            String Day = String.valueOf(cStart.get(Calendar.DAY_OF_MONTH));
            String Month = getMonthString(cStart.get(Calendar.MONTH));
            String Year = String.valueOf( cStart.get(Calendar.YEAR));

            eventRunDate = Month + " " + Day;  //removed the year at this time
            eventStartMini = Month + " " + Day;

            if(end != null ) {
                Calendar cEnd = calDateFromString(end);

                String eDay = String.valueOf(cEnd.get(Calendar.DAY_OF_MONTH));
                String eMonth = getMonthString(cEnd.get(Calendar.MONTH));
                String eYear = String.valueOf( cEnd.get(Calendar.YEAR));

                if(cEnd.get(Calendar.DAY_OF_MONTH) > cStart.get(Calendar.DAY_OF_MONTH)) {
                    eventRunDate = (eventStartMini) + "  to  " + eMonth + " " + eDay;  // removed the year at this time
                }

            }
        }




        return eventRunDate;
    }

    public int getIntMonth(String string) throws ParseException {
        String s = string;

        Calendar cal = stringToCalendar(s);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int eMonth = cal.get(Calendar.MONTH);
        return eMonth;
    }

    public static Calendar calDateFromString(String string) throws ParseException {
        String s = string;
        Calendar cal = stringToCalendar(s);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       // System.err.println(sdf.format(cal.getTime()));
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

    public String getMonthString(int monthInt){
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


    private String getCleanLocation(String location){
        String finalField = "";
        String[] fields = location.split("->");
        finalField = fields[0];

            String[] fieldsAddress = fields[0].split("[0-9]", 2);
            finalField = fieldsAddress[0] ;


        return finalField;

    }
}
