package klusman.scaantirevents.mobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import klusman.scaantirevents.mobile.Dao.DaoSession;
import klusman.scaantirevents.mobile.Dao.EventDao;
import klusman.scaantirevents.mobile.MyApp;
import klusman.scaantirevents.mobile.Objects.Event;

/**
 * Created by Narrook on 11/17/14.
 */
public class SyncDataBroadcastReceiver extends BroadcastReceiver
{

    List<JSONObject> JsonList = new ArrayList<JSONObject>();
    String serverPullResult = "";
    Context mContext;

    EventDao eDao;
    DaoSession sDao;

    Date minDate;
    Date maxDate;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i("TAG", "Received Broadcast - Sync");
        mContext =  context;
        sDao = MyApp.getInstance().getDaoSession();
        eDao = sDao.getEventDao();

        minDate = new Date();
        maxDate = new Date();

        pullAll();
    }

    public void pullAll() {
        createPullDates();
        Log.i("TAG", "Start Event Sync");
        MyAsyncTask mat = new MyAsyncTask();
        mat.execute();
    }

    class MyAsyncTask extends AsyncTask<String, String, String>
    {

       // InputStream inputStream = null;
      //  String result = "";

        protected void onPreExecute() {
            Log.i("TAG", "Pre pull");
        }


        @Override
        protected String doInBackground(String... params) {
            Log.i("TAG", "Start pull");

            StringBuilder builder = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://scalac.herokuapp.com");
            try {
                HttpResponse response = client.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }

                } else {
                    Log.e("TAG", "Failed to download file");
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return builder.toString();
        } // protected Void doInBackground(String... params)


        protected void onProgressUpdate(Long... values) {
                Log.i("TAG", "End server pull");
        }

        protected void onPostExecute(String result) {
            Log.i("TAG", "Post Execute");
            //  workMagic(result);
            serverPullResult = result;
            new addDataToDBTask().execute();

        } // protected void onPostExecute(Void v)
    } //class MyAsyncTask extends AsyncTask<String, String, Void>


    class addDataToDBTask extends AsyncTask<Void, Void, Void>
    {

        protected void onPreExecute()
        {
            Log.i("TAG", "Pre pull");
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            Log.i("TAG", "parse JSON Data Started");
            try {
                JSONArray jsonArray = new JSONArray(serverPullResult);
                Log.i("TAG", "Number of entries in JSON Array " + jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.has("type")) {
                        if (jsonObject.getString("type").compareToIgnoreCase("VEVENT") == 0) {
                            Date mDate = new Date();
                            mDate.setTime(calDateFromString(jsonObject.getString("start")).getTimeInMillis());

                            if (mDate.after(minDate) && mDate.before(maxDate)) {
                                JsonList.add(jsonObject);
                            }
                        }
                    }
                }
                List<Event> eList = eDao.loadAll();
                try {

                    for (JSONObject jObj : JsonList) {
                        boolean match = false;

                        if (jObj.has("uid")) {
                            String strID = jObj.get("uid").toString();
                            for (Event e : eList) {

                                if (e.getEventId().compareToIgnoreCase(strID) == 0) {
                                    match = true;
                                }
                            }
                            if (match) {
                                upDateEvent(jObj);
                            } else {
                                makeNewEvent(jObj);
                            }
                        }
                    }
                    Log.i("TAG", "Event Dao length = " + eDao.count());

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        } // protected Void doInBackground(String... params)


        protected void onPostExecute(String result) {
            Log.i("TAG", "Post Execute Push to DB");
        } // protected void onPostExecute(Void v)
    } //class MyAsyncTask extends AsyncTask<String, String, Void>



    public void upDateEvent(JSONObject jObject) {
         Log.i("TAG", "Update Event");

        try {
            Event ev = eDao.load(jObject.getString("uid"));

            if (jObject.has("summary"))
                ev.setEventName(jObject.getString("summary"));

            if (jObject.has("description"))
                ev.setEventDescription(jObject.getString("description"));

            if (jObject.has("start"))
                ev.setEventStart(jObject.getString("start"));

            if (jObject.has("end"))
                ev.setEventEnd(jObject.getString("end"));

            if (jObject.has("location"))
                ev.setEventLocation(jObject.getString("location"));

            eDao.update(ev);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void makeNewEvent(JSONObject jObject) {
        Log.i("TAG", "Make New Event");
        Event event = new Event();
        event.__setDaoSession(sDao);

        try {
            event.setEventId(jObject.getString("uid"));

            if (jObject.has("summary"))
                event.setEventName(jObject.getString("summary"));

            if (jObject.has("description"))
                event.setEventDescription(jObject.getString("description"));

            if (jObject.has("start"))
                event.setEventStart(jObject.getString("start"));

            if (jObject.has("end"))
                event.setEventEnd(jObject.getString("end"));

            if (jObject.has("location"))
                event.setEventLocation(jObject.getString("location"));

            eDao.insert(event);
        } catch (JSONException e) {
            Log.i("TAG", "Event with error: " + event.getEventName() + "  Id: " + event.getEventId());
            e.printStackTrace();
        }

    }

    public static Calendar calDateFromString(String string) throws ParseException {
        String s = string;
        Calendar cal = stringToCalendar(s);
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

    private void createPullDates() {

        int monthsToAdd = 13;
        int monthsToSubtract = 1;

        Calendar minCal = Calendar.getInstance();
        minCal.clear(Calendar.MILLISECOND);
        minCal.clear(Calendar.SECOND);
        minCal.clear(Calendar.MINUTE);
        minCal.clear(Calendar.HOUR);
        minCal.add(Calendar.MONTH, monthsToSubtract);

        Calendar maxCal = Calendar.getInstance();
        maxCal.clear(Calendar.MILLISECOND);
        maxCal.clear(Calendar.SECOND);
        maxCal.clear(Calendar.MINUTE);
        maxCal.clear(Calendar.HOUR);
        maxCal.add(Calendar.MONTH, monthsToAdd);

        minDate = minCal.getTime();
        maxDate = maxCal.getTime();

    }

}
